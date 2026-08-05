package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.EquipmentBusinessLogic;
import businesslayer.ValidationException;
import transferobjects.EquipmentDTO;

/** FR-02: Edit Equipment. */
public class EditEquipmentCommand implements Command {

    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            EquipmentDTO equipment = equipmentBL.getByAssetTag(request.getParameter("assetTag"));
            if (equipment == null) throw new ValidationException("Equipment not found.");
            equipment.setMake(request.getParameter("make"));
            equipment.setModel(request.getParameter("model"));
            equipment.setEquipmentName(request.getParameter("equipmentName"));
            equipment.setCategory(EquipmentDTO.Category.valueOf(request.getParameter("category")));
            equipment.setAccessCreditRate(Double.parseDouble(request.getParameter("accessCreditRate")));
            equipment.setLocation(request.getParameter("location"));
            equipment.setStatus(EquipmentDTO.Status.valueOf(request.getParameter("status")));
            equipmentBL.editEquipment(equipment);
        } catch (ValidationException | IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("equipmentList", equipmentBL.getAllEquipment());
        return "forward:/WEB-INF/views/equipment/equipment.jsp";
    }
}
