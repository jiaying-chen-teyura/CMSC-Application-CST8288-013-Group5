package businesslayer.builder;

import transferobjects.WorkOrderDTO;

/**
 * Builder Pattern (required pattern).
 * A work order can come from a member (memberUserId set, clientId null) or
 * an external client (clientId set, memberUserId null), with three separate
 * cost estimates that are usually filled in incrementally as a Shop-Tech
 * reviews the job. The builder lets SubmitWorkOrderCommand and
 * ExternalJobRequestAdapter both assemble a WorkOrderDTO the same way
 * without duplicating validation.
 *
 * Used by: WorkOrderBusinessLogic.submitWorkOrder(...), ExternalJobRequestAdapter
 * @author Le Bao Thach Nguyen 
 */
public class WorkOrderBuilder {

    private final WorkOrderDTO workOrder = new WorkOrderDTO();

    public WorkOrderBuilder forMember(int memberUserId) {
        workOrder.setMemberUserId(memberUserId);
        return this;
    }

    public WorkOrderBuilder forExternalClient(int clientId) {
        workOrder.setClientId(clientId);
        return this;
    }

    public WorkOrderBuilder description(String description) {
        workOrder.setDescription(description);
        return this;
    }

    public WorkOrderBuilder priority(WorkOrderDTO.Priority priority) {
        workOrder.setPriority(priority);
        return this;
    }

    public WorkOrderBuilder estimatedEquipmentCost(double cost) {
        workOrder.setEstimatedEquipmentCost(cost);
        return this;
    }

    public WorkOrderBuilder estimatedMaterialCost(double cost) {
        workOrder.setEstimatedMaterialCost(cost);
        return this;
    }

    public WorkOrderBuilder estimatedLabourCost(double cost) {
        workOrder.setEstimatedLabourCost(cost);
        return this;
    }

    public WorkOrderDTO build() {
        if (workOrder.getDescription() == null || workOrder.getDescription().isBlank()) {
            throw new IllegalStateException("WorkOrderBuilder: description is required");
        }
        if (workOrder.getMemberUserId() == null && workOrder.getClientId() == null) {
            throw new IllegalStateException("WorkOrderBuilder: either a member or an external client is required");
        }
        if (workOrder.getPriority() == null) {
            workOrder.setPriority(WorkOrderDTO.Priority.STANDARD);
        }
        return workOrder;
    }
}
