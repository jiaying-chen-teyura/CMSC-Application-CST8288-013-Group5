package controller.command;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.ConsumableBusinessLogic;
import businesslayer.EquipmentBusinessLogic;
import businesslayer.ValidationException;
import controller.SessionUtil;
import transferobjects.EquipmentDTO;
import transferobjects.UserDTO;

/** FR-02: Register Equipment (Shop-Tech only - Shop-Tech is the administrative role). */
public class RegisterEquipmentCommand implements Command {

    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();
    private final ConsumableBusinessLogic consumableBL = new ConsumableBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserDTO user = SessionUtil.getCurrentUser(request);
        if (!user.isShopTech()) {
            request.setAttribute("errorMessage", "Only a Shop-Tech can register equipment.");
            request.setAttribute("equipmentList", equipmentBL.getAllEquipmentWithConsumables());
            request.setAttribute("allConsumables", consumableBL.getAllConsumables());
            return "forward:/WEB-INF/views/equipment/equipment.jsp";
        }
        try {
            List<Integer> consumableIds = new ArrayList<>();
            String[] consumableIdParams = request.getParameterValues("consumableId");
            if (consumableIdParams != null) {
                for (String id : consumableIdParams) consumableIds.add(Integer.parseInt(id));
            }
            equipmentBL.registerEquipment(
                    request.getParameter("assetTag"),
                    request.getParameter("make"),
                    request.getParameter("model"),
                    EquipmentDTO.Category.valueOf(request.getParameter("category")),
                    request.getParameter("equipmentName"),
                    Double.parseDouble(request.getParameter("accessCreditRate")),
                    request.getParameter("location"),
                    user.getUserId(),
                    consumableIds);
        } catch (ValidationException | IllegalArgumentException | NullPointerException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("equipmentList", equipmentBL.getAllEquipmentWithConsumables());
        request.setAttribute("allConsumables", consumableBL.getAllConsumables());
        return "forward:/WEB-INF/views/equipment/equipment.jsp";
    }
}
