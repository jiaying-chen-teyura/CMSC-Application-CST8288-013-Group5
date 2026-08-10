package dataaccesslayer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import transferobjects.EquipmentBookingDTO;

public class EquipmentBookingDaoImpl implements EquipmentBookingDao {

    private EquipmentBookingDTO map(ResultSet rs) throws SQLException {
        EquipmentBookingDTO b = new EquipmentBookingDTO();
        b.setBookingId(rs.getInt("booking_id"));
        b.setUserId(rs.getInt("user_id"));
        b.setAssetTag(rs.getString("asset_tag"));
        b.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        b.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        b.setBookingStatus(EquipmentBookingDTO.BookingStatus.valueOf(rs.getString("booking_status")));
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) b.setCreatedAt(created.toLocalDateTime());
        return b;
    }

    @Override
    public int addBooking(EquipmentBookingDTO booking) {
        String sql = "INSERT INTO equipment_bookings (user_id, asset_tag, start_time, end_time) VALUES (?,?,?,?)";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getUserId());
            ps.setString(2, booking.getAssetTag());
            ps.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("addBooking failed", e);
        }
    }

    @Override
    public EquipmentBookingDTO getBookingById(int bookingId) {
        String sql = "SELECT * FROM equipment_bookings WHERE booking_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("getBookingById failed", e);
        }
    }

    @Override
    public List<EquipmentBookingDTO> getBookingsForUser(int userId) {
        String sql = "SELECT * FROM equipment_bookings WHERE user_id = ? ORDER BY start_time DESC";
        List<EquipmentBookingDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("getBookingsForUser failed", e);
        }
        return list;
    }

    @Override
    public List<EquipmentBookingDTO> getBookingsForEquipment(String assetTag) {
        String sql = "SELECT * FROM equipment_bookings WHERE asset_tag = ? ORDER BY start_time DESC";
        List<EquipmentBookingDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, assetTag);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("getBookingsForEquipment failed", e);
        }
        return list;
    }

    @Override
    public List<EquipmentBookingDTO> getOverlappingBookings(String assetTag, LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT * FROM equipment_bookings WHERE asset_tag = ? AND booking_status IN ('BOOKED', 'IN_PROGRESS') "
                + "AND start_time < ? AND end_time > ?";
        List<EquipmentBookingDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, assetTag);
            ps.setTimestamp(2, Timestamp.valueOf(end));
            ps.setTimestamp(3, Timestamp.valueOf(start));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("getOverlappingBookings failed", e);
        }
        return list;
    }

    @Override
    public void updateStatus(int bookingId, EquipmentBookingDTO.BookingStatus status) {
        String sql = "UPDATE equipment_bookings SET booking_status = ? WHERE booking_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("updateStatus failed", e);
        }
    }
}
