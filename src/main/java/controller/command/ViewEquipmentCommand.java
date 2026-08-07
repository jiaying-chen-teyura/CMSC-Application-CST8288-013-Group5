package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.EquipmentBusinessLogic;

/** FR-02: View Equipment (list of all equipment, including inactive, for Shop-Tech management). */
public class ViewEquipmentCommand implements Command {

    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("equipmentList", equipmentBL.getAllEquipment());
        return "forward:/WEB-INF/views/equipment/equipment.jsp";
    }
}
