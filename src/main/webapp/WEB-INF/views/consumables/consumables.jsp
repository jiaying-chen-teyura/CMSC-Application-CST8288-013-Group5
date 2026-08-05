<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% request.setAttribute("pageTitle", "Consumables"); request.setAttribute("activeNav", "consumables"); %>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/WEB-INF/views/common/head.jsp" %>
</head>
<body>
  <div class="app-shell">
    <%@ include file="/WEB-INF/views/common/nav.jsp" %>
    <div class="app-main">
      <%@ include file="/WEB-INF/views/common/header.jsp" %>
      <main class="content">
        <div class="content-header"><p>Donate materials and check the co-op's inventory (FR-04).</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="card">
          <h2 class="section-title">Donate Materials</h2>
          <form class="inline-form" action="${pageContext.request.contextPath}/controller" method="POST">
            <input type="hidden" name="action" value="donateConsumable">
            <div class="field"><label>Material</label>
              <select name="consumableId" required>
                <c:forEach var="c" items="${inventoryReport}">
                  <option value="${c.consumableId}">${c.materialName} (${c.unit})</option>
                </c:forEach>
              </select>
            </div>
            <div class="field"><label>Quantity</label><input type="number" step="0.01" min="0.01" name="quantity" required></div>
            <div class="field"><button type="submit" class="btn btn-primary">Donate</button></div>
          </form>
          <p class="hint mt-4">Donations earn credit automatically (Strategy pattern: DonationCreditStrategy).</p>
        </div>

        <div class="card">
          <h2 class="section-title">Inventory Report (FR-04)</h2>
          <c:choose>
            <c:when test="${empty inventoryReport}"><p class="text-muted">No consumables registered.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>Material</th><th>Unit</th><th>Current Stock</th><th>Restock Level</th><th>Status</th><th>Avg Daily Use</th><th>Days Until Depletion</th></tr></thead>
                <tbody>
                  <c:forEach var="c" items="${inventoryReport}">
                    <tr>
                      <td>${c.materialName}</td><td>${c.unit}</td><td>${c.currentStock}</td><td>${c.restockLevel}</td>
                      <td><span class="badge badge-${fn:toLowerCase(c.stockStatus)}">${c.stockStatus}</span></td>
                      <td>${c.averageDailyConsumption}</td>
                      <td>${empty c.projectedDaysUntilDepletion ? 'n/a' : c.projectedDaysUntilDepletion}</td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </c:otherwise>
          </c:choose>
        </div>
      </main>
    </div>
  </div>
</body>
</html>
