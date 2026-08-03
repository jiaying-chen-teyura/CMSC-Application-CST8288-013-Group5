package dao;

import dto.EquipmentCategory;
import dto.EquipmentDTO;
import dto.EquipmentStatus;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import util.DataSource;

/**
 * JDBC implementation of {@link EquipmentDao}. All SQL for the
 * {@code equipment} table is isolated here; no other class in the
 * Equipment module (and no Servlet/JSP anywhere in the app) should know
 * this table's structure. Every method uses {@link PreparedStatement} —
 * never string-concatenated SQL.
 *
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public class EquipmentDaoImpl implements EquipmentDao {

    private static final String SELECT_COLUMNS =
            "asset_tag, make, model, category, equipment_name, status, "
            + "access_credit_rate, total_usage_hours, location, registered_by, "
            + "registered_at, active";

    /** {@inheritDoc} */
    @Override
    public EquipmentDTO findByAssetTag(String assetTag) throws SQLException {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM equipment WHERE asset_tag = ?";
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assetTag);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;   // caller (business layer) decides what null means
    }

    /** {@inheritDoc} */
    @Override
    public List<EquipmentDTO> findAll() throws SQLException {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM equipment ORDER BY equipment_name";
        List<EquipmentDTO> equipmentList = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                equipmentList.add(mapRow(rs));
            }
        }
        return equipmentList;
    }

    /** {@inheritDoc} */
    @Override
    public List<EquipmentDTO> findByStatus(EquipmentStatus status) throws SQLException {
        String sql = "SELECT " + SELECT_COLUMNS
                + " FROM equipment WHERE status = ? ORDER BY equipment_name";
        List<EquipmentDTO> equipmentList = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    equipmentList.add(mapRow(rs));
                }
            }
        }
        return equipmentList;
    }

    /** {@inheritDoc} */
    @Override
    public List<EquipmentDTO> findByCategory(EquipmentCategory category) throws SQLException {
        String sql = "SELECT " + SELECT_COLUMNS
                + " FROM equipment WHERE category = ? ORDER BY equipment_name";
        List<EquipmentDTO> equipmentList = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    equipmentList.add(mapRow(rs));
                }
            }
        }
        return equipmentList;
    }

    /** {@inheritDoc} */
    @Override
    public boolean insert(EquipmentDTO equipment) throws SQLException {
        String sql = "INSERT INTO equipment "
                + "(asset_tag, make, model, category, equipment_name, status, "
                + "access_credit_rate, total_usage_hours, location, registered_by) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipment.getAssetTag());
            ps.setString(2, equipment.getMake());
            ps.setString(3, equipment.getModel());
            ps.setString(4, equipment.getCategory().name());
            ps.setString(5, equipment.getEquipmentName());
            EquipmentStatus status = equipment.getStatus() != null
                    ? equipment.getStatus() : EquipmentStatus.AVAILABLE;
            ps.setString(6, status.name());
            ps.setBigDecimal(7, equipment.getAccessCreditRate());
            BigDecimal usageHours = equipment.getTotalUsageHours() != null
                    ? equipment.getTotalUsageHours() : BigDecimal.ZERO;
            ps.setBigDecimal(8, usageHours);
            ps.setString(9, equipment.getLocation());
            ps.setInt(10, equipment.getRegisteredBy());
            return ps.executeUpdate() == 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean update(EquipmentDTO equipment) throws SQLException {
        String sql = "UPDATE equipment SET make = ?, model = ?, category = ?, "
                + "equipment_name = ?, status = ?, access_credit_rate = ?, location = ? "
                + "WHERE asset_tag = ?";
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipment.getMake());
            ps.setString(2, equipment.getModel());
            ps.setString(3, equipment.getCategory().name());
            ps.setString(4, equipment.getEquipmentName());
            ps.setString(5, equipment.getStatus().name());
            ps.setBigDecimal(6, equipment.getAccessCreditRate());
            ps.setString(7, equipment.getLocation());
            ps.setString(8, equipment.getAssetTag());
            return ps.executeUpdate() == 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean updateStatus(String assetTag, EquipmentStatus newStatus) throws SQLException {
        String sql = "UPDATE equipment SET status = ? WHERE asset_tag = ?";
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setString(2, assetTag);
            return ps.executeUpdate() == 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean incrementUsageHours(String assetTag, BigDecimal hoursToAdd) throws SQLException {
        String sql = "UPDATE equipment SET total_usage_hours = total_usage_hours + ? "
                + "WHERE asset_tag = ?";
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, hoursToAdd);
            ps.setString(2, assetTag);
            return ps.executeUpdate() == 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean deactivate(String assetTag) throws SQLException {
        String sql = "UPDATE equipment SET active = FALSE, status = 'UNAVAILABLE' "
                + "WHERE asset_tag = ?";
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assetTag);
            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Maps the current row of {@code rs} to a new {@link EquipmentDTO}.
     *
     * @param rs a {@link ResultSet} positioned on a valid row, whose
     *           columns include everything in {@link #SELECT_COLUMNS}
     * @return the mapped DTO
     * @throws SQLException if a column cannot be read
     */
    private EquipmentDTO mapRow(ResultSet rs) throws SQLException {
        EquipmentDTO equipment = new EquipmentDTO();
        equipment.setAssetTag(rs.getString("asset_tag"));
        equipment.setMake(rs.getString("make"));
        equipment.setModel(rs.getString("model"));
        equipment.setCategory(EquipmentCategory.valueOf(rs.getString("category")));
        equipment.setEquipmentName(rs.getString("equipment_name"));
        equipment.setStatus(EquipmentStatus.valueOf(rs.getString("status")));
        equipment.setAccessCreditRate(rs.getBigDecimal("access_credit_rate"));
        equipment.setTotalUsageHours(rs.getBigDecimal("total_usage_hours"));
        equipment.setLocation(rs.getString("location"));
        equipment.setRegisteredBy(rs.getInt("registered_by"));
        Timestamp registeredAt = rs.getTimestamp("registered_at");
        if (registeredAt != null) {
            equipment.setRegisteredAt(registeredAt.toLocalDateTime());
        }
        equipment.setActive(rs.getBoolean("active"));
        return equipment;
    }
}
