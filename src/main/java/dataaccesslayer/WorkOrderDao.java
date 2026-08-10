package dataaccesslayer;

import java.util.List;
import transferobjects.WorkOrderDTO;

/**
 * Defines persistence operations for work order records.
 * @author Le Bao Thach Nguyen 
 */
public interface WorkOrderDao {
    /**
     * Submits a new work order for persistence.
     *
     * @param workOrder the work order to save
     * @return the generated work order identifier
     */
    int submitWorkOrder(WorkOrderDTO workOrder);

    /**
     * Retrieves a work order by its identifier.
     *
     * @param workOrderId the work order identifier to look up
     * @return the matching work order, or null if none exists
     */
    WorkOrderDTO getWorkOrderById(int workOrderId);

    /**
     * Retrieves all work orders that are still open.
     *
     * @return a list of open work orders
     */
    List<WorkOrderDTO> getOpenWorkOrders();

    /**
     * Retrieves all work orders assigned to a specific shop technician.
     *
     * @param shopTechId the shop technician identifier
     * @return a list of work orders for the technician
     */
    List<WorkOrderDTO> getWorkOrdersForShopTech(int shopTechId);

    /**
     * Retrieves all work orders submitted by a specific member.
     *
     * @param memberUserId the member user identifier
     * @return a list of work orders created by the member
     */
    List<WorkOrderDTO> getWorkOrdersForMember(int memberUserId);

    /**
     * Accepts a work order and assigns it to a shop technician.
     *
     * @param workOrderId the work order identifier
     * @param shopTechId the shop technician identifier
     */
    void acceptWorkOrder(int workOrderId, int shopTechId);

    /**
     * Marks a work order as in progress.
     *
     * @param workOrderId the work order identifier
     */
    void startWorkOrder(int workOrderId);

    /**
     * Completes a work order and records the earned credit.
     *
     * @param workOrderId the work order identifier
     * @param creditEarned the credit earned from completing the work order
     */
    void completeWorkOrder(int workOrderId, double creditEarned);
}
