package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.EquipmentBusinessLogic;
import controller.SessionUtil;
import transferobjects.UserDTO;

/** FR-02: Delete (retire) Equipment - Shop-Tech only. Soft delete so booking/session/maintenance history is preserved. */
public class DeleteEquipmentCommand implements Command {

    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();

    @Override
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
