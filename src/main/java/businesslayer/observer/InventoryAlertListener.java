package businesslayer.observer;

import java.util.List;
import dataaccesslayer.NotificationDao;
import dataaccesslayer.NotificationDaoImpl;
import dataaccesslayer.UserDao;
import dataaccesslayer.UserDaoImpl;
import transferobjects.NotificationDTO;
import transferobjects.UserDTO;

/**
 * Concrete Observer: when a consumable's stock drops to/below its restock
 * level, push a LOW_STOCK notification to every Shop-Tech (FR-04 restocking).
 * @author Le Bao Thach Nguyen 
 */
public class InventoryAlertListener implements InventoryListener {

    private final UserDao userDao = new UserDaoImpl();
    private final NotificationDao notificationDao = new NotificationDaoImpl();

    @Override
    public void onLowStock(InventoryAlertEvent event) {
        List<UserDTO> shopTechs = userDao.getUsersByType(UserDTO.UserType.SHOP_TECH);
        String title = "Low stock: " + event.getMaterialName();
        String message = String.format(
            "%s is at %.2f, at or below the restock level of %.2f. Please reorder.",
            event.getMaterialName(), event.getCurrentStock(), event.getRestockLevel());

        for (UserDTO tech : shopTechs) {
            NotificationDTO n = new NotificationDTO();
            n.setUserId(tech.getUserId());
            n.setNotificationType(NotificationDTO.NotificationType.LOW_STOCK);
            n.setTitle(title);
            n.setMessage(message);
            notificationDao.addNotification(n);
        }
    }
}
