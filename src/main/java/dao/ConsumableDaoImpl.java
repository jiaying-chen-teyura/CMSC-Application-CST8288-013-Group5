package dao;

import dto.ConsumableDTO;
import dto.ConsumableUnit;
import dto.InventoryReportDTO;
import dto.InventoryTransactionDTO;
import dto.InventoryTransactionType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import util.DataSource;

/**
 * JDBC implementation of {@link ConsumableDao}. All SQL for the
 * {@code consumables} and {@code inventory_transactions} tables is isolated
 * here; no other class in the Consumable module (and no Servlet/JSP anywhere
 * in the app) should know these tables' structure. Every method uses
 * {@link java.sql.PreparedStatement} — never string-concatenated SQL.
 *
 * @author Le Bao Thach Nguyen
 * @version 1.0
 */
public class ConsumableDaoImpl implements ConsumableDao {

    // -------------------------------------------------------------------------
    // Consumable CRUD
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public ConsumableDTO findById(int consumableId) throws SQLException {
        String sql = "SELECT consumable_id, material_name, unit, current_stock, "
                + "restock_level, unit_debit_rate, active "
                + "FROM consumables WHERE consumable_id = ?";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, consumableId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapConsumable(rs);
                }
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public ConsumableDTO findByMaterialName(String materialName) throws SQLException {
        String sql = "SELECT consumable_id, material_name, unit, current_stock, "
                + "restock_level, unit_debit_rate, active "
                + "FROM consumables WHERE material_name = ?";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, materialName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapConsumable(rs);
                }
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public List<ConsumableDTO> findAll() throws SQLException {
        String sql = "SELECT consumable_id, material_name, unit, current_stock, "
                + "restock_level, unit_debit_rate, active "
                + "FROM consumables ORDER BY material_name";

        List<ConsumableDTO> result = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapConsumable(rs));
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<ConsumableDTO> findAllActive() throws SQLException {
        String sql = "SELECT consumable_id, material_name, unit, current_stock, "
                + "restock_level, unit_debit_rate, active "
                + "FROM consumables WHERE active = TRUE ORDER BY material_name";

        List<ConsumableDTO> result = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapConsumable(rs));
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<ConsumableDTO> findBelowRestockLevel() throws SQLException {
        String sql = "SELECT consumable_id, material_name, unit, current_stock, "
                + "restock_level, unit_debit_rate, active "
                + "FROM consumables "
                + "WHERE active = TRUE AND current_stock <= restock_level "
                + "ORDER BY material_name";

        List<ConsumableDTO> result = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapConsumable(rs));
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean insert(ConsumableDTO consumable) throws SQLException {
        String sql = "INSERT INTO consumables "
                + "(material_name, unit, current_stock, restock_level, unit_debit_rate, active) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, consumable.getMaterialName());
            ps.setString(2, consumable.getUnit().name());
            ps.setBigDecimal(3, consumable.getCurrentStock() != null
                    ? consumable.getCurrentStock() : BigDecimal.ZERO);
            ps.setBigDecimal(4, consumable.getRestockLevel());
            ps.setBigDecimal(5, consumable.getUnitDebitRate());
            ps.setBoolean(6, consumable.isActive());

            int affected = ps.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        consumable.setConsumableId(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean update(ConsumableDTO consumable) throws SQLException {
        String sql = "UPDATE consumables "
                + "SET material_name = ?, unit = ?, restock_level = ?, unit_debit_rate = ? "
                + "WHERE consumable_id = ?";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, consumable.getMaterialName());
            ps.setString(2, consumable.getUnit().name());
            ps.setBigDecimal(3, consumable.getRestockLevel());
            ps.setBigDecimal(4, consumable.getUnitDebitRate());
            ps.setInt(5, consumable.getConsumableId());

            return ps.executeUpdate() == 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean incrementStock(int consumableId, BigDecimal quantityToAdd) throws SQLException {
        String sql = "UPDATE consumables "
                + "SET current_stock = current_stock + ? "
                + "WHERE consumable_id = ? AND active = TRUE";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, quantityToAdd);
            ps.setInt(2, consumableId);
            return ps.executeUpdate() == 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean decrementStock(int consumableId, BigDecimal quantityToRemove) throws SQLException {
        // The subtraction is done in SQL so the DB-level CHECK constraint
        // (current_stock >= 0) acts as the absolute last safety net.
        String sql = "UPDATE consumables "
                + "SET current_stock = current_stock - ? "
                + "WHERE consumable_id = ? AND active = TRUE AND current_stock >= ?";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, quantityToRemove);
            ps.setInt(2, consumableId);
            ps.setBigDecimal(3, quantityToRemove);  // WHERE guard: enough stock?
            return ps.executeUpdate() == 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean deactivate(int consumableId) throws SQLException {
        String sql = "UPDATE consumables SET active = FALSE WHERE consumable_id = ?";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, consumableId);
            return ps.executeUpdate() == 1;
        }
    }

    // -------------------------------------------------------------------------
    // Inventory transactions
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public boolean insertTransaction(InventoryTransactionDTO transaction) throws SQLException {
        String sql = "INSERT INTO inventory_transactions "
                + "(consumable_id, transaction_type, quantity_change, performed_by, notes) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, transaction.getConsumableId());
            ps.setString(2, transaction.getTransactionType().name());
            ps.setBigDecimal(3, transaction.getQuantityChange());
            ps.setInt(4, transaction.getPerformedBy());
            ps.setString(5, transaction.getNotes());  // setString handles null correctly

            int affected = ps.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        transaction.setInventoryTransactionId(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<InventoryTransactionDTO> findTransactionsByConsumable(int consumableId)
            throws SQLException {
        String sql = "SELECT inventory_transaction_id, consumable_id, transaction_type, "
                + "quantity_change, performed_by, transaction_time, notes "
                + "FROM inventory_transactions "
                + "WHERE consumable_id = ? "
                + "ORDER BY transaction_time DESC";

        List<InventoryTransactionDTO> result = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, consumableId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapTransaction(rs));
                }
            }
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // Reporting
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public List<InventoryReportDTO> getInventoryReport() throws SQLException {
        String sql = "SELECT consumable_id, material_name, unit, current_stock, "
                + "restock_level, stock_status, average_daily_consumption, "
                + "projected_days_until_depletion "
                + "FROM v_consumable_inventory_report "
                + "ORDER BY material_name";

        List<InventoryReportDTO> result = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                InventoryReportDTO report = new InventoryReportDTO();
                report.setConsumableId(rs.getInt("consumable_id"));
                report.setMaterialName(rs.getString("material_name"));
                report.setUnit(ConsumableUnit.valueOf(rs.getString("unit")));
                report.setCurrentStock(rs.getBigDecimal("current_stock"));
                report.setRestockLevel(rs.getBigDecimal("restock_level"));
                report.setRestockRequired("RESTOCK_REQUIRED".equals(rs.getString("stock_status")));

                BigDecimal avgDaily = rs.getBigDecimal("average_daily_consumption");
                report.setAverageDailyConsumption(rs.wasNull() ? null : avgDaily);

                BigDecimal projDays = rs.getBigDecimal("projected_days_until_depletion");
                report.setProjectedDaysUntilDepletion(rs.wasNull() ? null : projDays);

                result.add(report);
            }
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // Private mapping helpers
    // -------------------------------------------------------------------------

    /**
     * Maps the current row of a {@link ResultSet} to a {@link ConsumableDTO}.
     * Caller is responsible for cursor positioning.
     */
    private ConsumableDTO mapConsumable(ResultSet rs) throws SQLException {
        ConsumableDTO dto = new ConsumableDTO();
        dto.setConsumableId(rs.getInt("consumable_id"));
        dto.setMaterialName(rs.getString("material_name"));
        dto.setUnit(ConsumableUnit.valueOf(rs.getString("unit")));
        dto.setCurrentStock(rs.getBigDecimal("current_stock"));
        dto.setRestockLevel(rs.getBigDecimal("restock_level"));
        dto.setUnitDebitRate(rs.getBigDecimal("unit_debit_rate"));
        dto.setActive(rs.getBoolean("active"));
        return dto;
    }

    /**
     * Maps the current row of a {@link ResultSet} to an
     * {@link InventoryTransactionDTO}. Caller is responsible for cursor positioning.
     */
    private InventoryTransactionDTO mapTransaction(ResultSet rs) throws SQLException {
        InventoryTransactionDTO dto = new InventoryTransactionDTO();
        dto.setInventoryTransactionId(rs.getInt("inventory_transaction_id"));
        dto.setConsumableId(rs.getInt("consumable_id"));
        dto.setTransactionType(InventoryTransactionType.valueOf(rs.getString("transaction_type")));
        dto.setQuantityChange(rs.getBigDecimal("quantity_change"));
        dto.setPerformedBy(rs.getInt("performed_by"));

        Timestamp ts = rs.getTimestamp("transaction_time");
        dto.setTransactionTime(ts != null ? ts.toLocalDateTime() : null);
        dto.setNotes(rs.getString("notes"));
        return dto;
    }
}
