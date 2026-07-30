package model;

import java.util.ArrayList;
import java.util.List;

public class WorkOrderDao {

    private static final List<WorkOrder> WORK_ORDERS = new ArrayList<>(List.of(
        new WorkOrder(1, "Cut 10 acrylic panels 12x12in", "External Client - Acme Signs", "Pending"),
        new WorkOrder(2, "Print replacement gear housing", "Jane Student",                  "Pending")
    ));

    public List<WorkOrder> getAllWorkOrders() {
        return WORK_ORDERS;
    }

    public boolean acceptWorkOrder(int id) {
        for (WorkOrder w : WORK_ORDERS) {
            if (w.getId() == id) {
                w.setStatus("In Progress");
                return true;
            }
        }
        return false;
    }
}
