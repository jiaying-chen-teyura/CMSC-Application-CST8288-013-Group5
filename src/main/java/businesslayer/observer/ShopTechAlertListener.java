package businesslayer.observer;

import java.util.List;
import dataaccesslayer.NotificationDao;
import dataaccesslayer.NotificationDaoImpl;
import dataaccesslayer.UserDao;
import dataaccesslayer.UserDaoImpl;
import transferobjects.NotificationDTO;
import transferobjects.UserDTO;

/**
 * Concrete Observer: when a component crosses its wear threshold, push a
 * MAINTENANCE notification to every Shop-Tech so they see it under
 * "View Maintenance Alerts" (FR-05) without polling.
 */
public class ShopTechAlertListener implements MaintenanceListener {

    private final UserDao userDao = new UserDaoImpl();
    private final NotificationDao notificationDao = new NotificationDaoImpl();

    @Override
    public void onMaintenanceAlert(MaintenanceAlertEvent event) {
        List<UserDTO> shopTechs = userDao.getUsersByType(UserDTO.UserType.SHOP_TECH);
        String title = "Maintenance required: " + event.getEquipmentName();
        String message = String.format(
            "%s on %s (%s) has reached %.1f of %.1f wear hours and needs servicing.",
            event.getComponentName(), event.getEquipmentName(), event.getAssetTag(),
            event.getUsageHours(), event.getThresholdHours());

        for (UserDTO tech : shopTechs) {
            NotificationDTO n = new NotificationDTO();
            n.setUserId(tech.getUserId());
            n.setNotificationType(NotificationDTO.NotificationType.MAINTENANCE);
            n.setTitle(title);
            n.setMessage(message);
            notificationDao.addNotification(n);
        }
    }
}
