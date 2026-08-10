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
 * @author Jiaying Chen
 * Checks out equipment for an active usage session.
 */
public class CheckOutEquipmentCommand implements Command {

    private final UsageSessionBusinessLogic sessionBL = new UsageSessionBusinessLogic();

    @Override
    /**
     * Executes the controller command for the current request.
     *
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @return the path used to forward or redirect the request
     */
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
        return "redirect:/controller?action=viewEquipmentAvailability";
    }
}
