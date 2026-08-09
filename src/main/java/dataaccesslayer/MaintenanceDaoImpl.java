package dataaccesslayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transferobjects.EquipmentComponentDTO;
import transferobjects.MaintenanceTaskDTO;

/** DAO backing FR-05 (predictive maintenance alerts). */
public class MaintenanceDaoImpl implements MaintenanceDao {

    private EquipmentComponentDTO mapComponent(ResultSet rs) throws SQLException {
        EquipmentComponentDTO c = new EquipmentComponentDTO();
        c.setComponentId(rs.getInt("component_id"));
        c.setAssetTag(rs.getString("asset_tag"));
        c.setComponentName(rs.getString("component_name"));
        c.setUsageHours(rs.getDouble("usage_hours"));
        c.setMaintenanceThresholdHours(rs.getDouble("maintenance_threshold_hours"));
        c.setComponentStatus(EquipmentComponentDTO.ComponentStatus.valueOf(rs.getString("component_status")));
        Timestamp t = rs.getTimestamp("last_maintained_at");
        if (t != null) c.setLastMaintainedAt(t.toLocalDateTime());
        return c;
    }

    private MaintenanceTaskDTO mapTask(ResultSet rs) throws SQLException {
        MaintenanceTaskDTO m = new MaintenanceTaskDTO();
        m.setMaintenanceId(rs.getInt("maintenance_id"));
        m.setAssetTag(rs.getString("asset_tag"));
        int compId = rs.getInt("component_id");
        if (!rs.wasNull()) m.setComponentId(compId);
        int techId = rs.getInt("assigned_shop_tech_id");
        if (!rs.wasNull()) m.setAssignedShopTechId(techId);
        m.setMaintenanceType(MaintenanceTaskDTO.MaintenanceType.valueOf(rs.getString("maintenance_type")));
        m.setDescription(rs.getString("description"));
        m.setPriority(MaintenanceTaskDTO.Priority.valueOf(rs.getString("priority")));
        Timestamp sched = rs.getTimestamp("scheduled_start");
        if (sched != null) m.setScheduledStart(sched.toLocalDateTime());
        Timestamp started = rs.getTimestamp("started_at");
        if (started != null) m.setStartedAt(started.toLocalDateTime());
        Timestamp completed = rs.getTimestamp("completed_at");
        if (completed != null) m.setCompletedAt(completed.toLocalDateTime());
        double hrs = rs.getDouble("maintenance_hours");
        if (!rs.wasNull()) m.setMaintenanceHours(hrs);
        m.setStatus(MaintenanceTaskDTO.Status.valueOf(rs.getString("status")));
        m.setCreditEarned(rs.getDouble("credit_earned"));
        try { m.setEquipmentName(rs.getString("equipment_name")); } catch (SQLException ignored) { }
        try { m.setComponentName(rs.getString("component_name_join")); } catch (SQLException ignored) { }
        return m;
    }

    @Override
    public void addComponent(EquipmentComponentDTO component) {
        String sql = "INSERT INTO equipment_components (asset_tag, component_name, maintenance_threshold_hours) "
                + "VALUES (?,?,?)";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, component.getAssetTag());
            ps.setString(2, component.getComponentName());
            ps.setDouble(3, component.getMaintenanceThresholdHours());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("addComponent failed", e);
        }
    }

    @Override
    public List<EquipmentComponentDTO> getComponentsForEquipment(String assetTag) {
        String sql = "SELECT * FROM equipment_components WHERE asset_tag = ?";
        List<EquipmentComponentDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, assetTag);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapComponent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("getComponentsForEquipment failed", e);
        }
        return list;
    }

    @Override
    public EquipmentComponentDTO getComponentById(int componentId) {
        String sql = "SELECT * FROM equipment_components WHERE component_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, componentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapComponent(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("getComponentById failed", e);
        }
    }

    @Override
    public void addWearHours(int componentId, double hours) {
        String sql = "UPDATE equipment_components SET usage_hours = usage_hours + ? WHERE component_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, hours);
            ps.setInt(2, componentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("addWearHours failed", e);
        }
    }

    @Override
    public void setComponentStatus(int componentId, EquipmentComponentDTO.ComponentStatus status) {
        String sql = "UPDATE equipment_components SET component_status = ? WHERE component_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, componentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("setComponentStatus failed", e);
        }
    }

    @Override
    public void resetComponentAfterMaintenance(int componentId) {
        String sql = "UPDATE equipment_components SET usage_hours = 0, component_status = 'HEALTHY', "
                + "last_maintained_at = NOW() WHERE component_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, componentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("resetComponentAfterMaintenance failed", e);
        }
    }

    @Override
    public int createMaintenanceTask(MaintenanceTaskDTO task) {
        String sql = "INSERT INTO maintenance_tasks (asset_tag, component_id, assigned_shop_tech_id, maintenance_type, "
                + "description, priority, scheduled_start, status) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, task.getAssetTag());
            if (task.getComponentId() != null) ps.setInt(2, task.getComponentId()); else ps.setNull(2, Types.INTEGER);
            if (task.getAssignedShopTechId() != null) ps.setInt(3, task.getAssignedShopTechId()); else ps.setNull(3, Types.INTEGER);
            ps.setString(4, task.getMaintenanceType().name());
            ps.setString(5, task.getDescription());
            ps.setString(6, task.getPriority().name());
            if (task.getScheduledStart() != null) ps.setTimestamp(7, Timestamp.valueOf(task.getScheduledStart()));
            else ps.setNull(7, Types.TIMESTAMP);
            ps.setString(8, task.getStatus().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("createMaintenanceTask failed", e);
        }
    }

    private static final String TASK_JOIN_SELECT =
            "SELECT t.*, e.equipment_name AS equipment_name, c.component_name AS component_name_join "
            + "FROM maintenance_tasks t "
            + "JOIN equipment e ON e.asset_tag = t.asset_tag "
            + "LEFT JOIN equipment_components c ON c.component_id = t.component_id ";

    @Override
    public List<MaintenanceTaskDTO> getOpenMaintenanceTasks() {
        String sql = TASK_JOIN_SELECT + "WHERE t.status IN ('ALERTED','SCHEDULED','IN_PROGRESS') "
                + "ORDER BY FIELD(t.priority,'URGENT','HIGH','MEDIUM','LOW'), t.scheduled_start";
        List<MaintenanceTaskDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapTask(rs));
        } catch (SQLException e) {
            throw new RuntimeException("getOpenMaintenanceTasks failed", e);
        }
        return list;
    }

    @Override
    public List<MaintenanceTaskDTO> getTasksForShopTech(int shopTechId) {
        String sql = TASK_JOIN_SELECT + "WHERE t.assigned_shop_tech_id = ? ORDER BY t.scheduled_start";
        List<MaintenanceTaskDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, shopTechId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapTask(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("getTasksForShopTech failed", e);
        }
        return list;
    }

    @Override
    public MaintenanceTaskDTO getTaskById(int maintenanceId) {
        String sql = TASK_JOIN_SELECT + "WHERE t.maintenance_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maintenanceId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapTask(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("getTaskById failed", e);
        }
    }

    @Override
    public void scheduleTask(int maintenanceId, int shopTechId, java.time.LocalDateTime scheduledStart) {
        String sql = "UPDATE maintenance_tasks SET assigned_shop_tech_id=?, scheduled_start=?, status='SCHEDULED' "
                + "WHERE maintenance_id=?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, shopTechId);
            ps.setTimestamp(2, Timestamp.valueOf(scheduledStart));
            ps.setInt(3, maintenanceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("scheduleTask failed", e);
        }
    }

    @Override
    public void completeTask(int maintenanceId, double maintenanceHours, double creditEarned) {
        String sql = "UPDATE maintenance_tasks SET status='COMPLETED', started_at=COALESCE(started_at, NOW()), "
                + "completed_at=NOW(), maintenance_hours=?, credit_earned=? WHERE maintenance_id=?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, maintenanceHours);
            ps.setDouble(2, creditEarned);
            ps.setInt(3, maintenanceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("completeTask failed", e);
        }
    }
}
