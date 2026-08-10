package controller.command;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiaying Chen
 * Maps action names to concrete command implementations.
 */
public class CommandFactory {

    private static final Map<String, Command> COMMANDS = new HashMap<>();

    static {
        // Auth / dashboard
        COMMANDS.put("login", new LoginCommand());
        COMMANDS.put("logout", new LogoutCommand());
        COMMANDS.put("register", new RegisterCommand());
        COMMANDS.put("dashboard", new DashboardCommand());

        // Equipment management (FR-02)
        COMMANDS.put("viewEquipment", new ViewEquipmentCommand());
        COMMANDS.put("registerEquipment", new RegisterEquipmentCommand());
        COMMANDS.put("editEquipment", new EditEquipmentCommand());
        COMMANDS.put("deleteEquipment", new DeleteEquipmentCommand());

        // Equipment booking
        COMMANDS.put("viewEquipmentAvailability", new ViewEquipmentAvailabilityCommand());
        COMMANDS.put("bookEquipment", new BookEquipmentCommand());
        COMMANDS.put("cancelBooking", new CancelBookingCommand());

        // Equipment session tracking (FR-03)
        COMMANDS.put("checkInEquipment", new CheckInEquipmentCommand());
        COMMANDS.put("checkOutEquipment", new CheckOutEquipmentCommand());
        COMMANDS.put("viewActiveSessions", new ViewActiveSessionsCommand());

        // Consumables & donations (FR-04)
        COMMANDS.put("viewInventory", new ViewInventoryCommand());
        COMMANDS.put("donateConsumable", new DonateConsumableCommand());
        COMMANDS.put("registerConsumable", new RegisterConsumableCommand());
        COMMANDS.put("editConsumable", new EditConsumableCommand());
        COMMANDS.put("deleteConsumable", new DeleteConsumableCommand());

        // Work orders
        COMMANDS.put("viewWorkOrders", new ViewWorkOrdersCommand());
        COMMANDS.put("submitWorkOrder", new SubmitWorkOrderCommand());
        COMMANDS.put("acceptWorkOrder", new AcceptWorkOrderCommand());
        COMMANDS.put("startWorkOrder", new StartWorkOrderCommand());
        COMMANDS.put("completeWorkOrder", new CompleteWorkOrderCommand());

        // Predictive maintenance (FR-05)
        COMMANDS.put("viewMaintenanceAlerts", new ViewMaintenanceAlertsCommand());
        COMMANDS.put("scheduleMaintenance", new ScheduleMaintenanceCommand());
        COMMANDS.put("startMaintenance", new StartMaintenanceCommand());
        COMMANDS.put("performMaintenance", new PerformMaintenanceCommand());

        // Credits & ledger
        COMMANDS.put("viewLedger", new ViewLedgerCommand());
        COMMANDS.put("settleAccount", new SettleAccountCommand());

        // Training
        COMMANDS.put("scheduleTraining", new ScheduleTrainingCommand());
        COMMANDS.put("conductTraining", new ConductTrainingCommand());
        COMMANDS.put("viewTrainerReport", new ViewTrainerReportCommand());

        // Reports (FR-06)
        COMMANDS.put("viewShopTechReport", new ViewShopTechReportCommand());
        COMMANDS.put("viewEquipmentInventoryStatusReport", new ViewEquipmentInventoryStatusReportCommand());
    }

    private CommandFactory() { }

    /**

     * Resolves an action name to the appropriate command.

     *

     * @param action the action identifier from the request

     * @return the matching command implementation

     */

    public static Command getCommand(String action) {
        if (action == null) return COMMANDS.get("dashboard");
        return COMMANDS.getOrDefault(action, new UnknownActionCommand());
    }
}
