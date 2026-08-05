package controller.command;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import businesslayer.UsageSessionBusinessLogic;
import businesslayer.UsageSessionBusinessLogic.MaterialUsageRequest;
import businesslayer.ValidationException;
import controller.SessionUtil;

/**
 * FR-03: Check Out Equipment. Accepts parallel arrays consumableId[]/quantity[]
 * from the checkout form so a member can report materials consumed
 * (e.g. filament grams) in the same step, per FR-04.
 */
public class CheckOutEquipmentCommand implements Command {

    private final UsageSessionBusinessLogic sessionBL = new UsageSessionBusinessLogic();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int userId = SessionUtil.getCurrentUser(request).getUserId();
        int sessionId = Integer.parseInt(request.getParameter("usageSessionId"));

        String[] consumableIds = request.getParameterValues("consumableId");
        String[] quantities = request.getParameterValues("quantity");
        List<MaterialUsageRequest> materials = new ArrayList<>();
        if (consumableIds != null && quantities != null) {
            for (int i = 0; i < consumableIds.length; i++) {
                if (consumableIds[i] == null || consumableIds[i].isBlank()) continue;
                double qty = Double.parseDouble(quantities[i]);
                if (qty <= 0) continue;
                materials.add(new MaterialUsageRequest(Integer.parseInt(consumableIds[i]), qty));
            }
        }

        try {
            sessionBL.checkOut(sessionId, userId, materials);
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/controller?action=viewActiveSessions";
    }
}
