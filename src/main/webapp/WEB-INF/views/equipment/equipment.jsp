<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% request.setAttribute("pageTitle", "Manage Equipment"); request.setAttribute("activeNav", "equipmentManage"); %>
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
        <div class="content-header"><p>Register, edit, and retire co-op equipment (FR-02).</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="card">
          <h2 class="section-title">Register New Equipment</h2>
          <form class="form-grid" action="${pageContext.request.contextPath}/controller" method="POST">
            <input type="hidden" name="action" value="registerEquipment">
            <div class="field"><label>Asset Tag</label><input type="text" name="assetTag" required></div>
            <div class="field"><label>Make</label><input type="text" name="make" required></div>
            <div class="field"><label>Model</label><input type="text" name="model" required></div>
            <div class="field"><label>Category</label>
              <select name="category">
                <option value="THREE_D_PRINTER">3D Printer</option>
                <option value="LASER_CUTTER">Laser Cutter</option>
                <option value="CNC">CNC</option>
              </select>
            </div>
            <div class="field"><label>Display Name</label><input type="text" name="equipmentName" required></div>
            <div class="field"><label>Access Credit Rate ($/hr)</label><input type="number" step="0.01" min="0" name="accessCreditRate" required></div>
            <div class="field"><label>Location</label><input type="text" name="location"></div>
            <div class="field"><button type="submit" class="btn btn-primary">Register</button></div>
          </form>
        </div>

        <div class="card">
          <h2 class="section-title">All Equipment</h2>
          <c:choose>
            <c:when test="${empty equipmentList}"><p class="text-muted">No equipment registered yet.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>Asset Tag</th><th>Name</th><th>Category</th><th>Status</th><th>Rate</th><th>Usage Hrs</th><th>Active</th><th></th></tr></thead>
                <tbody>
                  <c:forEach var="e" items="${equipmentList}">
                    <tr>
                      <td>${e.assetTag}</td>
                      <td>${e.equipmentName}<br><span class="text-muted">${e.make} ${e.model}</span></td>
                      <td>${e.category}</td>
                      <td><span class="badge badge-${fn:toLowerCase(e.status)}">${e.status}</span></td>
                      <td>$<fmt:formatNumber value="${e.accessCreditRate}" xmlns:fmt="jakarta.tags.fmt"/></td>
                      <td>${e.totalUsageHours}</td>
                      <td>${e.active ? 'Yes' : 'No'}</td>
                      <td>
                        <c:if test="${e.active}">
                          <form style="display:inline" action="${pageContext.request.contextPath}/controller" method="POST"
                                onsubmit="return confirm('Retire this equipment?');">
                            <input type="hidden" name="action" value="deleteEquipment">
                            <input type="hidden" name="assetTag" value="${e.assetTag}">
                            <button type="submit" class="btn btn-secondary btn-small">Retire</button>
                          </form>
                        </c:if>
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
