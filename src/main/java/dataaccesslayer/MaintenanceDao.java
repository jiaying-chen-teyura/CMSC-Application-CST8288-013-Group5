package dataaccesslayer;

import java.util.List;
import transferobjects.EquipmentComponentDTO;
import transferobjects.MaintenanceTaskDTO;

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
    void scheduleTask(int maintenanceId, int shopTechId, java.time.LocalDateTime scheduledStart);
    void completeTask(int maintenanceId, double maintenanceHours, double creditEarned);
}
