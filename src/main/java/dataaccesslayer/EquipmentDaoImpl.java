package dataaccesslayer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import transferobjects.ConsumableDTO;
import transferobjects.EquipmentDTO;

/**
 * Data Access Object (DAO) implementation for Equipment entities.
 * Provides methods for CRUD operations and status updates on equipment.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */

public class EquipmentDaoImpl implements EquipmentDao {

    private EquipmentDTO map(ResultSet rs) throws SQLException {
        EquipmentDTO e = new EquipmentDTO();
        e.setAssetTag(rs.getString("asset_tag"));
        e.setMake(rs.getString("make"));
        e.setModel(rs.getString("model"));
        e.setCategory(EquipmentDTO.Category.valueOf(rs.getString("category")));
        e.setEquipmentName(rs.getString("equipment_name"));
        e.setStatus(EquipmentDTO.Status.valueOf(rs.getString("status")));
        e.setAccessCreditRate(rs.getDouble("access_credit_rate"));
        e.setTotalUsageHours(rs.getDouble("total_usage_hours"));
        e.setLocation(rs.getString("location"));
        e.setRegisteredBy(rs.getInt("registered_by"));
        Timestamp regAt = rs.getTimestamp("registered_at");
        if (regAt != null) e.setRegisteredAt(regAt.toLocalDateTime());
        e.setActive(rs.getBoolean("active"));
        return e;
    }


    /**
     * Retrieves all equipment records from the database, ordered by category and equipment name.
     * @return a list of EquipmentDTO objects representing all equipment
     */
    @Override
    public List<EquipmentDTO> getAllEquipment() {
        String sql = "SELECT * FROM equipment ORDER BY category, equipment_name";
        List<EquipmentDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("getAllEquipment failed", e);
        }
        return list;
    }

    /**
     * Retrieves all active equipment records from the database, ordered by category and equipment name.
     * @return a list of EquipmentDTO objects representing active equipment
     */
    @Override
    public List<EquipmentDTO> getActiveEquipment() {
        String sql = "SELECT * FROM equipment WHERE active = TRUE ORDER BY category, equipment_name";
        List<EquipmentDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("getActiveEquipment failed", e);
        }
        return list;
    }

    /**
     * Retrieves an equipment record by its asset tag.
     * @param assetTag the unique asset tag of the equipment to retrieve
     * @return the EquipmentDTO object representing the equipment, or null if not found
     */
    @Override
    public EquipmentDTO getEquipmentByAssetTag(String assetTag) {
        String sql = "SELECT * FROM equipment WHERE asset_tag = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, assetTag);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("getEquipmentByAssetTag failed", e);
        }
    }

    /**
     * Adds a new equipment record to the database.
     * @param eq the EquipmentDTO object representing the equipment to add
     */
    @Override
    public void addEquipment(EquipmentDTO eq) {
        String sql = "INSERT INTO equipment (asset_tag, make, model, category, equipment_name, status, "
                + "access_credit_rate, location, registered_by) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, eq.getAssetTag());
            ps.setString(2, eq.getMake());
            ps.setString(3, eq.getModel());
            ps.setString(4, eq.getCategory().name());
            ps.setString(5, eq.getEquipmentName());
            ps.setString(6, eq.getStatus().name());
            ps.setDouble(7, eq.getAccessCreditRate());
            ps.setString(8, eq.getLocation());
            ps.setInt(9, eq.getRegisteredBy());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("addEquipment failed", e);
        }
    }

    /**
     * Updates an existing equipment record in the database.
     * @param eq the EquipmentDTO object representing the equipment to update
     */
    @Override
    public void updateEquipment(EquipmentDTO eq) {
        String sql = "UPDATE equipment SET make=?, model=?, category=?, equipment_name=?, status=?, "
                + "access_credit_rate=?, location=?, active=? WHERE asset_tag=?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, eq.getMake());
            ps.setString(2, eq.getModel());
            ps.setString(3, eq.getCategory().name());
            ps.setString(4, eq.getEquipmentName());
            ps.setString(5, eq.getStatus().name());
            ps.setDouble(6, eq.getAccessCreditRate());
            ps.setString(7, eq.getLocation());
            ps.setBoolean(8, eq.isActive());
            ps.setString(9, eq.getAssetTag());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("updateEquipment failed", e);
        }
    }

    /**
     * Updates the status of an equipment record.
     * @param assetTag the unique asset tag of the equipment to update
     * @param status the new status for the equipment
     */
    @Override
    public void updateStatus(String assetTag, EquipmentDTO.Status status) {
        String sql = "UPDATE equipment SET status = ? WHERE asset_tag = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setString(2, assetTag);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("updateStatus failed", e);
        }
    }

    /**
     * Deletes an equipment record from the database.
     * @param assetTag the unique asset tag of the equipment to delete
     */
    @Override
    public void deleteEquipment(String assetTag) {
        // Soft delete: FR-02 equipment history (bookings/sessions/maintenance) must survive.
        String sql = "UPDATE equipment SET active = FALSE, status = 'UNAVAILABLE' WHERE asset_tag = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, assetTag);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("deleteEquipment failed", e);
        }
    }

    /**
     * Adds usage hours to an equipment record.
     * @param assetTag the unique asset tag of the equipment to update
     * @param hours the number of hours to add to the total usage
     */
    @Override
    public void addUsageHours(String assetTag, double hours) {
        String sql = "UPDATE equipment SET total_usage_hours = total_usage_hours + ? WHERE asset_tag = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, hours);
            ps.setString(2, assetTag);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("addUsageHours failed", e);
        }
    }

    /**
     * FR-02 "Consumable type": the consumable(s) a piece of equipment is registered to use.
     * @param assetTag the unique asset tag of the equipment
     * @return the ConsumableDTOs linked to that equipment via equipment_consumables
     */
    @Override
    public List<ConsumableDTO> getConsumablesForEquipment(String assetTag) {
        String sql = "SELECT c.* FROM consumables c "
                + "JOIN equipment_consumables ec ON ec.consumable_id = c.consumable_id "
                + "WHERE ec.asset_tag = ? AND c.active = TRUE ORDER BY c.material_name";
        List<ConsumableDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, assetTag);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ConsumableDTO c = new ConsumableDTO();
                    c.setConsumableId(rs.getInt("consumable_id"));
                    c.setMaterialName(rs.getString("material_name"));
                    c.setUnit(ConsumableDTO.Unit.valueOf(rs.getString("unit")));
                    c.setCurrentStock(rs.getDouble("current_stock"));
                    c.setRestockLevel(rs.getDouble("restock_level"));
                    c.setUnitDebitRate(rs.getDouble("unit_debit_rate"));
                    c.setActive(rs.getBoolean("active"));
                    list.add(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("getConsumablesForEquipment failed", e);
        }
        return list;
    }

    /**
     * Replaces the full set of consumable types linked to a piece of equipment (delete then re-insert,
     * matching the simple "replace the whole set" style already used by this project's builder/report code).
     * @param assetTag the unique asset tag of the equipment
     * @param consumableIds the consumable IDs the equipment should be linked to (may be empty)
     */
    @Override
    public void setEquipmentConsumables(String assetTag, List<Integer> consumableIds) {
        String deleteSql = "DELETE FROM equipment_consumables WHERE asset_tag = ?";
        String insertSql = "INSERT INTO equipment_consumables (asset_tag, consumable_id) VALUES (?,?)";
        try (Connection con = DataSource.getConnection()) {
            try (PreparedStatement del = con.prepareStatement(deleteSql)) {
                del.setString(1, assetTag);
                del.executeUpdate();
            }
            if (consumableIds != null) {
                try (PreparedStatement ins = con.prepareStatement(insertSql)) {
                    for (Integer consumableId : consumableIds) {
                        ins.setString(1, assetTag);
                        ins.setInt(2, consumableId);
                        ins.addBatch();
                    }
                    if (!consumableIds.isEmpty()) ins.executeBatch();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("setEquipmentConsumables failed", e);
        }
    }
}
