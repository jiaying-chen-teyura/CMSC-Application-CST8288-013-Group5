package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import businesslayer.EquipmentBusinessLogic;

/**
 * @author Jiaying Chen
 * Displays equipment records to the user.
 */
public class ViewEquipmentCommand implements Command {

    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();
    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("equipmentList", equipmentBL.getAllEquipmentWithConsumables());
        // FR-02 "Consumable type": every registered consumable, for the Register/Edit Equipment checkboxes.
        request.setAttribute("allConsumables", consumableBL.getAllConsumables());
        return "forward:/WEB-INF/views/equipment/equipment.jsp";
    }
}
