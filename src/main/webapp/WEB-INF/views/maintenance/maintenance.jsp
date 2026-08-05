<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% request.setAttribute("pageTitle", "Maintenance"); request.setAttribute("activeNav", "maintenance"); %>
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
        <div class="content-header"><p>Predictive maintenance alerts and scheduling (FR-05).</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="card">
          <h2 class="section-title">Schedule Maintenance</h2>
          <form class="form-grid" action="${pageContext.request.contextPath}/controller" method="POST">
            <input type="hidden" name="action" value="scheduleMaintenance">
            <div class="field"><label>Equipment</label>
              <select name="assetTag" required>
                <c:forEach var="e" items="${equipmentList}">
                  <option value="${e.assetTag}">${e.equipmentName} (${e.assetTag})</option>
                </c:forEach>
              </select>
            </div>
            <div class="field"><label>Type</label>
              <select name="maintenanceType">
                <option value="PREVENTIVE">Preventive</option>
                <option value="REPAIR">Repair</option>
                <option value="INSPECTION">Inspection</option>
              </select>
            </div>
            <div class="field"><label>Priority</label>
              <select name="priority">
                <option value="LOW">Low</option><option value="MEDIUM" selected>Medium</option>
                <option value="HIGH">High</option><option value="URGENT">Urgent</option>
              </select>
            </div>
            <div class="field"><label>Scheduled Start</label><input type="datetime-local" name="scheduledStart" required></div>
            <div class="field" style="grid-column: 1 / -1;"><label>Description</label><input type="text" name="description" required></div>
            <div class="field"><button type="submit" class="btn btn-primary">Schedule</button></div>
          </form>
        </div>

        <div class="card">
          <h2 class="section-title">Open Alerts &amp; Tasks (Observer pattern: MaintenanceAlertService &rarr; ShopTechAlertListener)</h2>
          <c:choose>
            <c:when test="${empty openAlerts}"><p class="text-muted">No open maintenance tasks.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>#</th><th>Equipment</th><th>Component</th><th>Type</th><th>Priority</th><th>Status</th><th></th></tr></thead>
                <tbody>
                  <c:forEach var="t" items="${openAlerts}">
                    <tr>
                      <td>${t.maintenanceId}</td>
                      <td>${t.equipmentName} (${t.assetTag})</td>
                      <td>${t.componentName}</td>
                      <td>${t.maintenanceType}</td>
                      <td><span class="badge badge-${fn:toLowerCase(t.priority)}">${t.priority}</span></td>
                      <td><span class="badge badge-${fn:toLowerCase(t.status)}">${t.status}</span></td>
                      <td>
                        <form style="display:flex; gap:6px;" action="${pageContext.request.contextPath}/controller" method="POST">
                          <input type="hidden" name="action" value="performMaintenance">
                          <input type="hidden" name="maintenanceId" value="${t.maintenanceId}">
                          <input type="number" step="0.1" min="0.1" name="hoursSpent" placeholder="hrs" style="width:70px;" required>
                          <button type="submit" class="btn btn-primary btn-small">Complete</button>
                        </form>
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
