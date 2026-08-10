package dataaccesslayer;

import java.util.List;
import transferobjects.EquipmentComponentDTO;
import transferobjects.MaintenanceTaskDTO;

/**
 * Data Access Object (DAO) interface for Maintenance entities.
 * Provides methods for managing equipment components and maintenance tasks, including creation, retrieval, and status updates.
 * @author Oladimeji Durojaiye
 * @version 1.0
 */

public interface MaintenanceDao {
    void addComponent(EquipmentComponentDTO component);
    List<EquipmentComponentDTO> getComponentsForEquipment(String assetTag);
    EquipmentComponentDTO getComponentById(int componentId);
    void addWearHours(int componentId, double hours);
    void setComponentStatus(int componentId, EquipmentComponentDTO.ComponentStatus status);
    void resetComponentAfterMaintenance(int componentId);

    int createMaintenanceTask(MaintenanceTaskDTO task);
    List<MaintenanceTaskDTO> getOpenMaintenanceTasks();
    List<MaintenanceTaskDTO> getTasksForShopTech(int shopTechId);
    MaintenanceTaskDTO getTaskById(int maintenanceId);

    /** The still-open (ALERTED/SCHEDULED/IN_PROGRESS) task for a component, if one already exists. */
    MaintenanceTaskDTO getOpenTaskForComponent(int componentId);

    void scheduleTask(int maintenanceId, int shopTechId, java.time.LocalDateTime scheduledStart,
                       MaintenanceTaskDTO.MaintenanceType type, MaintenanceTaskDTO.Priority priority, String description);
    void startTask(int maintenanceId);
    void completeTask(int maintenanceId, double maintenanceHours, double creditEarned);
}
