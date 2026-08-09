package dataaccesslayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transferobjects.ConsumableDTO;
import transferobjects.InventoryTransactionDTO;
import transferobjects.MaterialUsageDTO;

public class ConsumableDaoImpl implements ConsumableDao {

    private ConsumableDTO map(ResultSet rs) throws SQLException {
        ConsumableDTO c = new ConsumableDTO();
        c.setConsumableId(rs.getInt("consumable_id"));
        c.setMaterialName(rs.getString("material_name"));
        c.setUnit(ConsumableDTO.Unit.valueOf(rs.getString("unit")));
        c.setCurrentStock(rs.getDouble("current_stock"));
        c.setRestockLevel(rs.getDouble("restock_level"));
        c.setUnitDebitRate(rs.getDouble("unit_debit_rate"));
        c.setActive(rs.getBoolean("active"));
        return c;
    }

    @Override
    public List<ConsumableDTO> getAllConsumables() {
        String sql = "SELECT * FROM consumables WHERE active = TRUE ORDER BY material_name";
        List<ConsumableDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("getAllConsumables failed", e);
        }
        return list;
    }

    @Override
    public List<ConsumableDTO> getInventoryReport() {
        // Uses the view provided in CMSC_database.sql - fulfils FR-04 directly.
        String sql = "SELECT * FROM v_consumable_inventory_report ORDER BY material_name";
        List<ConsumableDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ConsumableDTO c = new ConsumableDTO();
                c.setConsumableId(rs.getInt("consumable_id"));
                c.setMaterialName(rs.getString("material_name"));
                c.setUnit(ConsumableDTO.Unit.valueOf(rs.getString("unit")));
                c.setCurrentStock(rs.getDouble("current_stock"));
                c.setRestockLevel(rs.getDouble("restock_level"));
                c.setStockStatus(rs.getString("stock_status"));
                c.setAverageDailyConsumption(rs.getDouble("average_daily_consumption"));
                double days = rs.getDouble("projected_days_until_depletion");
                c.setProjectedDaysUntilDepletion(rs.wasNull() ? null : days);
                list.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("getInventoryReport failed", e);
        }
        return list;
    }

    @Override
    public ConsumableDTO getConsumableById(int consumableId) {
        String sql = "SELECT * FROM consumables WHERE consumable_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, consumableId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("getConsumableById failed", e);
        }
    }

    @Override
    public void addConsumable(ConsumableDTO c) {
        String sql = "INSERT INTO consumables (material_name, unit, current_stock, restock_level, unit_debit_rate) "
                + "VALUES (?,?,?,?,?)";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getMaterialName());
            ps.setString(2, c.getUnit().name());
            ps.setDouble(3, c.getCurrentStock());
            ps.setDouble(4, c.getRestockLevel());
            ps.setDouble(5, c.getUnitDebitRate());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setConsumableId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("addConsumable failed", e);
        }
    }

    @Override
    public void updateConsumable(ConsumableDTO c) {
        String sql = "UPDATE consumables SET material_name=?, unit=?, restock_level=?, unit_debit_rate=?, active=? "
                + "WHERE consumable_id=?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getMaterialName());
            ps.setString(2, c.getUnit().name());
            ps.setDouble(3, c.getRestockLevel());
            ps.setDouble(4, c.getUnitDebitRate());
            ps.setBoolean(5, c.isActive());
            ps.setInt(6, c.getConsumableId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("updateConsumable failed", e);
        }
    }

    @Override
    public void deleteConsumable(int consumableId) {
        // Soft delete: FR-04 usage/donation history must survive.
        String sql = "UPDATE consumables SET active = FALSE WHERE consumable_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, consumableId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("deleteConsumable failed", e);
        }
    }

    @Override
    public void adjustStock(int consumableId, double delta) {
        String sql = "UPDATE consumables SET current_stock = current_stock + ? WHERE consumable_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, delta);
            ps.setInt(2, consumableId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("adjustStock failed", e);
        }
    }

    @Override
    public void recordInventoryTransaction(InventoryTransactionDTO tx) {
        String sql = "INSERT INTO inventory_transactions (consumable_id, transaction_type, quantity_change, performed_by, notes, credit_earned) "
                + "VALUES (?,?,?,?,?,?)";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tx.getConsumableId());
            ps.setString(2, tx.getTransactionType().name());
            ps.setDouble(3, tx.getQuantityChange());
            ps.setInt(4, tx.getPerformedBy());
            ps.setString(5, tx.getNotes());
            if (tx.getCreditEarned() != null) {
                ps.setDouble(6, tx.getCreditEarned());
            } else {
                ps.setNull(6, Types.DECIMAL);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("recordInventoryTransaction failed", e);
        }
    }

    @Override
    public void recordMaterialUsage(MaterialUsageDTO usage) {
        String sql = "INSERT INTO material_usage (usage_session_id, consumable_id, quantity_used, unit_rate, material_debit) "
                + "VALUES (?,?,?,?,?)";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usage.getUsageSessionId());
            ps.setInt(2, usage.getConsumableId());
            ps.setDouble(3, usage.getQuantityUsed());
            ps.setDouble(4, usage.getUnitRate());
            ps.setDouble(5, usage.getMaterialDebit());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("recordMaterialUsage failed", e);
        }
    }

    @Override
    public List<MaterialUsageDTO> getMaterialUsageForSession(int usageSessionId) {
        String sql = "SELECT * FROM material_usage WHERE usage_session_id = ?";
        List<MaterialUsageDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usageSessionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialUsageDTO m = new MaterialUsageDTO();
                    m.setMaterialUsageId(rs.getInt("material_usage_id"));
                    m.setUsageSessionId(rs.getInt("usage_session_id"));
                    m.setConsumableId(rs.getInt("consumable_id"));
                    m.setQuantityUsed(rs.getDouble("quantity_used"));
                    m.setUnitRate(rs.getDouble("unit_rate"));
                    m.setMaterialDebit(rs.getDouble("material_debit"));
                    Timestamp t = rs.getTimestamp("recorded_at");
                    if (t != null) m.setRecordedAt(t.toLocalDateTime());
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("getMaterialUsageForSession failed", e);
        }
        return list;
    }

    @Override
    public List<InventoryTransactionDTO> getDonationsForUser(int userId) {
        String sql = "SELECT it.*, c.material_name FROM inventory_transactions it "
                + "JOIN consumables c ON c.consumable_id = it.consumable_id "
                + "WHERE it.transaction_type = 'DONATION' AND it.performed_by = ? "
                + "ORDER BY it.transaction_time DESC";
        List<InventoryTransactionDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventoryTransactionDTO tx = new InventoryTransactionDTO();
                    tx.setInventoryTransactionId(rs.getInt("inventory_transaction_id"));
                    tx.setConsumableId(rs.getInt("consumable_id"));
                    tx.setTransactionType(InventoryTransactionDTO.TransactionType.valueOf(rs.getString("transaction_type")));
                    tx.setQuantityChange(rs.getDouble("quantity_change"));
                    tx.setPerformedBy(rs.getInt("performed_by"));
                    Timestamp t = rs.getTimestamp("transaction_time");
                    if (t != null) tx.setTransactionTime(t.toLocalDateTime());
                    tx.setNotes(rs.getString("notes"));
                    tx.setMaterialName(rs.getString("material_name"));
                    double credit = rs.getDouble("credit_earned");
                    tx.setCreditEarned(rs.wasNull() ? null : credit);
                    list.add(tx);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("getDonationsForUser failed", e);
        }
        return list;
    }
}
