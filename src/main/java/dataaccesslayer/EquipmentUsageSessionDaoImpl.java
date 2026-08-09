package dataaccesslayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transferobjects.EquipmentUsageSessionDTO;

/**
 * DAO for FR-03 (Usage & Session Tracking). getActiveSessions() joins in
 * equipment_name and user name so the "who is using what right now" report
 * required by FR-03 doesn't need N+1 lookups from the business layer.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public class EquipmentUsageSessionDaoImpl implements EquipmentUsageSessionDao {

    private EquipmentUsageSessionDTO map(ResultSet rs) throws SQLException {
        EquipmentUsageSessionDTO s = new EquipmentUsageSessionDTO();
        s.setUsageSessionId(rs.getInt("usage_session_id"));
        int bookingId = rs.getInt("booking_id");
        if (!rs.wasNull()) s.setBookingId(bookingId);
        s.setUserId(rs.getInt("user_id"));
        s.setAssetTag(rs.getString("asset_tag"));
        s.setCheckInTime(rs.getTimestamp("check_in_time").toLocalDateTime());
        Timestamp checkOut = rs.getTimestamp("check_out_time");
        if (checkOut != null) s.setCheckOutTime(checkOut.toLocalDateTime());
        int elapsed = rs.getInt("elapsed_minutes");
        if (!rs.wasNull()) s.setElapsedMinutes(elapsed);
        s.setHourlyRate(rs.getDouble("hourly_rate"));
        s.setEquipmentDebit(rs.getDouble("equipment_debit"));
        s.setSessionStatus(EquipmentUsageSessionDTO.SessionStatus.valueOf(rs.getString("session_status")));
        try { s.setEquipmentName(rs.getString("equipment_name")); } catch (SQLException ignored) { }
        try { s.setUserName(rs.getString("user_name")); } catch (SQLException ignored) { }
        return s;
    }

    /**
     * Inserts a new equipment usage session record into the database.
     * @param session the EquipmentUsageSessionDTO object representing the session to insert
     * @return the generated usage session ID, or -1 if insertion failed
     */
    @Override
    public int checkIn(EquipmentUsageSessionDTO session) {
        String sql = "INSERT INTO equipment_usage_sessions (booking_id, user_id, asset_tag, check_in_time, hourly_rate) "
                + "VALUES (?,?,?,?,?)";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (session.getBookingId() != null) ps.setInt(1, session.getBookingId()); else ps.setNull(1, Types.INTEGER);
            ps.setInt(2, session.getUserId());
            ps.setString(3, session.getAssetTag());
            ps.setTimestamp(4, Timestamp.valueOf(session.getCheckInTime()));
            ps.setDouble(5, session.getHourlyRate());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("checkIn failed", e);
        }
    }

    /**
     * Retrieves the active equipment usage session for a given equipment asset tag.
     * @param assetTag the unique asset tag of the equipment
     * @return the EquipmentUsageSessionDTO object representing the active session, or null if not found
     */
    @Override
    public EquipmentUsageSessionDTO getActiveSessionForEquipment(String assetTag) {
        String sql = "SELECT * FROM equipment_usage_sessions WHERE asset_tag = ? AND session_status = 'ACTIVE'";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, assetTag);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("getActiveSessionForEquipment failed", e);
        }
    }

    /**
     * Retrieves an equipment usage session by its ID.
     * @param usageSessionId the ID of the session to retrieve
     * @return the EquipmentUsageSessionDTO object representing the session, or null if not found
     */
    @Override
    public EquipmentUsageSessionDTO getSessionById(int usageSessionId) {
        String sql = "SELECT * FROM equipment_usage_sessions WHERE usage_session_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usageSessionId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("getSessionById failed", e);
        }
    }

    /**
     * Updates an existing equipment usage session record in the database to mark it as checked out.
     * @param usageSessionId the ID of the session to update
     * @param checkOutTime the check-out time to set
     * @param elapsedMinutes the elapsed minutes to set
     * @param equipmentDebit the equipment debit to set
     */
    @Override
    public void checkOut(int usageSessionId, java.time.LocalDateTime checkOutTime, int elapsedMinutes, double equipmentDebit) {
        String sql = "UPDATE equipment_usage_sessions SET check_out_time=?, elapsed_minutes=?, equipment_debit=?, "
                + "session_status='COMPLETED' WHERE usage_session_id=?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(checkOutTime));
            ps.setInt(2, elapsedMinutes);
            ps.setDouble(3, equipmentDebit);
            ps.setInt(4, usageSessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("checkOut failed", e);
        }
    }

    /**
     * Retrieves a list of all active equipment usage sessions, including equipment names and user names.
     * @return a list of EquipmentUsageSessionDTO objects representing the active sessions
     */
    @Override
    public List<EquipmentUsageSessionDTO> getActiveSessions() {
        String sql = "SELECT s.*, e.equipment_name AS equipment_name, u.name AS user_name "
                + "FROM equipment_usage_sessions s "
                + "JOIN equipment e ON e.asset_tag = s.asset_tag "
                + "JOIN users u ON u.user_id = s.user_id "
                + "WHERE s.session_status = 'ACTIVE' ORDER BY s.check_in_time";
        List<EquipmentUsageSessionDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("getActiveSessions failed", e);
        }
        return list;
    }

    /**
     * Retrieves a list of all equipment usage sessions for a specific user, including equipment names and user names.
     * @param userId the ID of the user whose sessions to retrieve
     * @return a list of EquipmentUsageSessionDTO objects representing the user's sessions
     */
    @Override
    public List<EquipmentUsageSessionDTO> getSessionsForUser(int userId) {
        String sql = "SELECT s.*, e.equipment_name AS equipment_name, u.name AS user_name "
                + "FROM equipment_usage_sessions s "
                + "JOIN equipment e ON e.asset_tag = s.asset_tag "
                + "JOIN users u ON u.user_id = s.user_id "
                + "WHERE s.user_id = ? ORDER BY s.check_in_time DESC";
        List<EquipmentUsageSessionDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("getSessionsForUser failed", e);
        }
        return list;
    }
}
