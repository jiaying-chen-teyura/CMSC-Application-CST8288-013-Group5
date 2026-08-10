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
          <h2 class="section-title">Equipment Needing Attention</h2>
          <p class="hint">Equipment currently flagged for predictive maintenance, or already taken UNAVAILABLE because its working-hours limit was reached before maintenance was completed.</p>
          <c:choose>
            <c:when test="${empty attentionEquipment}"><p class="text-muted">Everything is healthy right now.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>Asset Tag</th><th>Equipment</th><th>Category</th><th>Booking Status</th><th>Health Status</th></tr></thead>
                <tbody>
                  <c:forEach var="e" items="${attentionEquipment}">
                    <tr>
                      <td>${e.assetTag}</td>
                      <td>${e.equipmentName}</td>
                      <td>${e.category}</td>
                      <td><span class="badge badge-${fn:toLowerCase(e.status)}">${e.status}</span></td>
                      <td>
                        <c:choose>
                          <c:when test="${e.status == 'UNAVAILABLE'}">
                            <span class="badge badge-unavailable">Unavailable</span>
                          </c:when>
                          <c:when test="${e.status == 'MAINTENANCE'}">
                            <span class="badge badge-maintenance">Maintenance In Progress</span>
                          </c:when>
                          <c:when test="${e.needsMaintenance}">
                            <span class="badge badge-alerted">Needs Maintenance</span>
                          </c:when>
                          <c:otherwise>
                            <span class="badge badge-available">Healthy</span>
                          </c:otherwise>
                        </c:choose>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </c:otherwise>
          </c:choose>
        </div>

        <div class="card">
          <h2 class="section-title">Schedule Maintenance</h2>
          <p class="hint">Only equipment with an open predictive-maintenance alert is listed below. Pick a date, then an hour, then a minute — minutes are always :00, :15, :30, or :45.</p>
          <c:choose>
            <c:when test="${empty pendingAlerts}">
              <p class="text-muted">No unclaimed alerts right now — nothing to schedule.</p>
            </c:when>
            <c:otherwise>
              <form class="form-grid" action="${pageContext.request.contextPath}/controller" method="POST">
                <input type="hidden" name="action" value="scheduleMaintenance">
                <div class="field"><label>Equipment (alert)</label>
                  <select name="maintenanceId" required>
                    <c:forEach var="a" items="${pendingAlerts}">
                      <option value="${a.maintenanceId}">${a.equipmentName} (${a.assetTag}) &ndash; ${a.componentName}</option>
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
                <div class="field"><label>Scheduled Start</label>
                  <div class="qh-picker" id="scheduledStartField" data-name="scheduledStart"></div>
                </div>
                <div class="field" style="grid-column: 1 / -1;"><label>Description</label><input type="text" name="description" required></div>
                <div class="field"><button type="submit" class="btn btn-primary">Schedule</button></div>
              </form>
            </c:otherwise>
          </c:choose>
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
                        <c:if test="${t.status == 'ALERTED'}">
                          <span class="text-muted">Schedule it above</span>
                        </c:if>
                        <c:if test="${t.status == 'SCHEDULED'}">
                          <form action="${pageContext.request.contextPath}/controller" method="POST">
                            <input type="hidden" name="action" value="startMaintenance">
                            <input type="hidden" name="maintenanceId" value="${t.maintenanceId}">
                            <button type="submit" class="btn btn-primary btn-small">Start</button>
                          </form>
                        </c:if>
                        <c:if test="${t.status == 'IN_PROGRESS'}">
                          <form action="${pageContext.request.contextPath}/controller" method="POST">
                            <input type="hidden" name="action" value="performMaintenance">
                            <input type="hidden" name="maintenanceId" value="${t.maintenanceId}">
                            <button type="submit" class="btn btn-primary btn-small">Complete</button>
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

        <div class="card">
          <h2 class="section-title">My Completed Maintenance</h2>
          <p class="hint">Jobs you've finished (performMaintenance) &ndash; hours logged and credit earned, most recent first.</p>
          <c:choose>
            <c:when test="${empty myCompletedMaintenance}"><p class="text-muted">Nothing completed yet.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>#</th><th>Equipment</th><th>Component</th><th>Type</th><th>Completed</th><th>Hours</th><th>Credit</th></tr></thead>
                <tbody>
                  <c:forEach var="t" items="${myCompletedMaintenance}">
                    <tr>
                      <td>${t.maintenanceId}</td>
                      <td>${t.equipmentName} (${t.assetTag})</td>
                      <td>${t.componentName}</td>
                      <td>${t.maintenanceType}</td>
                      <td>${t.completedAt}</td>
                      <td>${t.maintenanceHours}</td>
                      <td>$${t.creditEarned}</td>
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
  <script src="${pageContext.request.contextPath}/assets/js/quarter-hour-field.js"></script>
  <script>initQuarterHourField("scheduledStartField");</script>
</body>
</html>
