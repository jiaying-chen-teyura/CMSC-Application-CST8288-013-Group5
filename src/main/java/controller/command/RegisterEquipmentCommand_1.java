package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.EquipmentBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import transferobjects.EquipmentDTO;
import transferobjects.UserDTO;

/** FR-02: Register Equipment (Shop-Tech / Admin). */
public class RegisterEquipmentCommand implements Command {

    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserDTO user = SessionUtil.getCurrentUser(request);
        if (!user.isShopTech() && !user.isAdmin()) {
            request.setAttribute("errorMessage", "Only a Shop-Tech or Admin can register equipment.");
            return "forward:/WEB-INF/views/equipment/equipment.jsp";
        }
        try {
            equipmentBL.registerEquipment(
                    request.getParameter("assetTag"),
                    request.getParameter("make"),
                    request.getParameter("model"),
                    EquipmentDTO.Category.valueOf(request.getParameter("category")),
                    request.getParameter("equipmentName"),
                    Double.parseDouble(request.getParameter("accessCreditRate")),
                    request.getParameter("location"),
                    user.getUserId());
        } catch (ValidationException | IllegalArgumentException | NullPointerException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("equipmentList", equipmentBL.getAllEquipment());
        return "forward:/WEB-INF/views/equipment/equipment.jsp";
    }
}
