package dataaccesslayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transferobjects.WorkOrderDTO;

/** Backs FR (job requests submitted/prioritized/fulfilled - Work Order use cases). */
public class WorkOrderDaoImpl implements WorkOrderDao {

    private WorkOrderDTO map(ResultSet rs) throws SQLException {
        WorkOrderDTO w = new WorkOrderDTO();
        w.setWorkOrderId(rs.getInt("work_order_id"));
        int clientId = rs.getInt("client_id");
        if (!rs.wasNull()) w.setClientId(clientId);
        int memberId = rs.getInt("member_user_id");
        if (!rs.wasNull()) w.setMemberUserId(memberId);
        int techId = rs.getInt("assigned_shop_tech_id");
        if (!rs.wasNull()) w.setAssignedShopTechId(techId);
        w.setDescription(rs.getString("description"));
        w.setPriority(WorkOrderDTO.Priority.valueOf(rs.getString("priority")));
        w.setStatus(WorkOrderDTO.Status.valueOf(rs.getString("status")));
        w.setEstimatedEquipmentCost(rs.getDouble("estimated_equipment_cost"));
        w.setEstimatedMaterialCost(rs.getDouble("estimated_material_cost"));
        w.setEstimatedLabourCost(rs.getDouble("estimated_labour_cost"));
        double quoted = rs.getDouble("quoted_price");
        if (!rs.wasNull()) w.setQuotedPrice(quoted);
        w.setCreditEarned(rs.getDouble("credit_earned"));
        w.setAgreementAccepted(rs.getBoolean("agreement_accepted"));
        Timestamp submitted = rs.getTimestamp("submitted_at");
        if (submitted != null) w.setSubmittedAt(submitted.toLocalDateTime());
        Timestamp completed = rs.getTimestamp("completed_at");
        if (completed != null) w.setCompletedAt(completed.toLocalDateTime());
        try { w.setRequesterLabel(rs.getString("requester_label")); } catch (SQLException ignored) { }
        return w;
    }

    @Override
    public int submitWorkOrder(WorkOrderDTO wo) {
        String sql = "INSERT INTO work_orders (client_id, member_user_id, description, priority, "
                + "estimated_equipment_cost, estimated_material_cost, estimated_labour_cost) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (wo.getClientId() != null) ps.setInt(1, wo.getClientId()); else ps.setNull(1, Types.INTEGER);
            if (wo.getMemberUserId() != null) ps.setInt(2, wo.getMemberUserId()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, wo.getDescription());
            ps.setString(4, wo.getPriority().name());
            ps.setDouble(5, wo.getEstimatedEquipmentCost());
            ps.setDouble(6, wo.getEstimatedMaterialCost());
            ps.setDouble(7, wo.getEstimatedLabourCost());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("submitWorkOrder failed", e);
        }
    }

    private static final String JOIN_SELECT =
            "SELECT w.*, COALESCE(u.name, c.client_name, 'External Client') AS requester_label "
            + "FROM work_orders w "
            + "LEFT JOIN users u ON u.user_id = w.member_user_id "
            + "LEFT JOIN external_clients c ON c.client_id = w.client_id ";

    @Override
    public WorkOrderDTO getWorkOrderById(int workOrderId) {
        String sql = JOIN_SELECT + "WHERE w.work_order_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workOrderId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("getWorkOrderById failed", e);
        }
    }

    @Override
    public List<WorkOrderDTO> getOpenWorkOrders() {
        String sql = JOIN_SELECT + "WHERE w.status IN ('SUBMITTED','QUOTED','ACCEPTED','IN_PROGRESS') "
                + "ORDER BY FIELD(w.priority,'RUSH','STANDARD'), w.submitted_at";
        List<WorkOrderDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("getOpenWorkOrders failed", e);
        }
        return list;
    }

    @Override
    public List<WorkOrderDTO> getWorkOrdersForShopTech(int shopTechId) {
        String sql = JOIN_SELECT + "WHERE w.assigned_shop_tech_id = ? ORDER BY w.submitted_at DESC";
        List<WorkOrderDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, shopTechId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("getWorkOrdersForShopTech failed", e);
        }
        return list;
    }

    @Override
    public List<WorkOrderDTO> getWorkOrdersForMember(int memberUserId) {
        String sql = JOIN_SELECT + "WHERE w.member_user_id = ? ORDER BY w.submitted_at DESC";
        List<WorkOrderDTO> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, memberUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("getWorkOrdersForMember failed", e);
        }
        return list;
    }

    @Override
    public void acceptWorkOrder(int workOrderId, int shopTechId) {
        String sql = "UPDATE work_orders SET assigned_shop_tech_id=?, status='ACCEPTED', "
                + "agreement_accepted=TRUE, agreement_accepted_at=NOW() WHERE work_order_id=?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, shopTechId);
            ps.setInt(2, workOrderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("acceptWorkOrder failed", e);
        }
    }

    @Override
    public void completeWorkOrder(int workOrderId, double creditEarned) {
        String sql = "UPDATE work_orders SET status='COMPLETED', completed_at=NOW(), credit_earned=? WHERE work_order_id=?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, creditEarned);
            ps.setInt(2, workOrderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("completeWorkOrder failed", e);
        }
    }
}
