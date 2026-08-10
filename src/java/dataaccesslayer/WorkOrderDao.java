package dataaccesslayer;

import java.util.List;
import transferobjects.WorkOrderDTO;

public interface WorkOrderDao {
    int submitWorkOrder(WorkOrderDTO workOrder);
    WorkOrderDTO getWorkOrderById(int workOrderId);
    List<WorkOrderDTO> getOpenWorkOrders();
    List<WorkOrderDTO> getWorkOrdersForShopTech(int shopTechId);
    List<WorkOrderDTO> getWorkOrdersForMember(int memberUserId);
    void acceptWorkOrder(int workOrderId, int shopTechId);
    void startWorkOrder(int workOrderId);
    void completeWorkOrder(int workOrderId, double creditEarned);
}
