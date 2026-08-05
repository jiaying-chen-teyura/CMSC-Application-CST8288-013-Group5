package controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.EquipmentBusinessLogic;

/** FR-02: Delete (retire) Equipment - soft delete so booking/session/maintenance history is preserved. */
public class DeleteEquipmentCommand implements Command {

    private final EquipmentBusinessLogic equipmentBL = new EquipmentBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        equipmentBL.deleteEquipment(request.getParameter("assetTag"));
        return "redirect:/controller?action=viewEquipment";
    }
}
