package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.EquipmentBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import transferobjects.EquipmentDTO;
import transferobjects.UserDTO;

/** FR-02: Register Equipment (Shop-Tech only - Shop-Tech is the administrative role). */
public class RegisterEquipmentCommand implements Command {

    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserDTO user = SessionUtil.getCurrentUser(request);
        if (!user.isShopTech()) {
            request.setAttribute("errorMessage", "Only a Shop-Tech can register equipment.");
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
