<!-- Author: Jiaying Chen -->
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

        <c:if test="${sessionScope.currentUser.shopTech}">
          <div class="card">
            <h2 class="section-title">Add Consumable (Shop-Tech)</h2>
            <form class="form-grid" action="${pageContext.request.contextPath}/controller" method="POST">
              <input type="hidden" name="action" value="registerConsumable">
              <div class="field"><label>Material Name</label><input type="text" name="materialName" required></div>
              <div class="field"><label>Unit</label>
                <select name="unit">
                  <option value="GRAM">Gram</option>
                  <option value="MILLILITRE">Millilitre</option>
                  <option value="SHEET">Sheet</option>
                  <option value="PIECE">Piece</option>
                </select>
              </div>
              <div class="field"><label>Starting Stock</label><input type="number" step="0.01" min="0" name="currentStock" value="0" required></div>
              <div class="field"><label>Restock Level</label><input type="number" step="0.01" min="0" name="restockLevel" required></div>
              <div class="field"><label>Unit Debit Rate ($)</label><input type="number" step="0.01" min="0" name="unitDebitRate" required></div>
              <div class="field"><button type="submit" class="btn btn-primary">Add Consumable</button></div>
            </form>
          </div>

          <div class="card">
            <h2 class="section-title">Manage Consumables (Shop-Tech)</h2>
            <c:choose>
              <c:when test="${empty inventoryReport}"><p class="text-muted">No consumables registered.</p></c:when>
              <c:otherwise>
                <table class="data-table">
                  <thead><tr><th>Material</th><th>Unit</th><th>Restock Level</th><th>Unit Rate</th><th></th></tr></thead>
                  <tbody>
                    <c:forEach var="c" items="${inventoryReport}">
                      <%-- Each row's inputs bind to a <form> below the table via the HTML5 form="" attribute,
                           since a <form> element cannot itself wrap table cells. --%>
                      <tr>
                        <td><input type="text" name="materialName" form="editConsumable-${c.consumableId}" value="${c.materialName}" class="table-input" required></td>
                        <td>
                          <select name="unit" form="editConsumable-${c.consumableId}">
                            <option value="GRAM" ${c.unit == 'GRAM' ? 'selected' : ''}>Gram</option>
                            <option value="MILLILITRE" ${c.unit == 'MILLILITRE' ? 'selected' : ''}>Millilitre</option>
                            <option value="SHEET" ${c.unit == 'SHEET' ? 'selected' : ''}>Sheet</option>
                            <option value="PIECE" ${c.unit == 'PIECE' ? 'selected' : ''}>Piece</option>
                          </select>
                        </td>
                        <td><input type="number" step="0.01" min="0" name="restockLevel" form="editConsumable-${c.consumableId}" value="${c.restockLevel}" class="table-input"></td>
                        <td><input type="number" step="0.01" min="0" name="unitDebitRate" form="editConsumable-${c.consumableId}" value="${c.unitDebitRate}" class="table-input"></td>
                        <td>
                          <button type="submit" form="editConsumable-${c.consumableId}" class="btn btn-secondary btn-small">Save</button>
                          <form id="editConsumable-${c.consumableId}" style="display:inline" action="${pageContext.request.contextPath}/controller" method="POST">
                            <input type="hidden" name="action" value="editConsumable">
                            <input type="hidden" name="consumableId" value="${c.consumableId}">
                          </form>
                          <form style="display:inline" action="${pageContext.request.contextPath}/controller" method="POST"
                                onsubmit="return confirm('Retire this consumable?');">
                            <input type="hidden" name="action" value="deleteConsumable">
                            <input type="hidden" name="consumableId" value="${c.consumableId}">
                            <button type="submit" class="btn btn-secondary btn-small">Retire</button>
                          </form>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </c:otherwise>
            </c:choose>
          </div>
        </c:if>

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

        <div class="card">
          <h2 class="section-title">My Donations</h2>
          <c:choose>
            <c:when test="${empty myDonations}"><p class="text-muted">You haven't donated anything yet.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>When</th><th>Material</th><th>Quantity</th><th>Credit Earned</th></tr></thead>
                <tbody>
                  <c:forEach var="d" items="${myDonations}">
                    <tr>
                      <td>${d.transactionTime}</td>
                      <td>${d.materialName}</td>
                      <td>${d.quantityChange}</td>
                      <td>
                        <c:choose>
                          <c:when test="${empty d.creditEarned}">n/a</c:when>
                          <c:otherwise>$${d.creditEarned}</c:otherwise>
                        </c:choose>
                      </td>
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
