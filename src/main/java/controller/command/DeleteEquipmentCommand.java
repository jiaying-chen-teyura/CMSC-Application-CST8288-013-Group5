package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.EquipmentBusinessLogic;
import controller.SessionUtil;
import transferobjects.UserDTO;

/**
 * @author Jiaying Chen
 * Deletes an existing equipment record.
 */
public class DeleteEquipmentCommand implements Command {

    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();

    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserDTO user = SessionUtil.getCurrentUser(request);
        if (!user.isShopTech()) {
            request.setAttribute("errorMessage", "Only a Shop-Tech can retire equipment.");
            request.setAttribute("equipmentList", equipmentBL.getAllEquipmentWithConsumables());
            return "forward:/WEB-INF/views/equipment/equipment.jsp";
        }
        equipmentBL.deleteEquipment(request.getParameter("assetTag"));
        return "redirect:/controller?action=viewEquipment";
    }
}
