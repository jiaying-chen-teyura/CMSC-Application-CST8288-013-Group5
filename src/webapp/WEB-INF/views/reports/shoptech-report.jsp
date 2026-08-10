<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% request.setAttribute("pageTitle", "My Shop-Tech Report"); request.setAttribute("activeNav", "shoptechReport"); %>
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
        <div class="content-header"><p>Credits earned by activity: maintenance logged and work orders completed (FR-06).</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="stat-grid">
          <div class="stat-card"><div class="label">Credits (${summary.reportMonth})</div><div class="value">$${summary.totalCredits}</div></div>
          <div class="stat-card"><div class="label">Debits</div><div class="value">$${summary.totalDebits}</div></div>
          <div class="stat-card"><div class="label">Amount to Settle</div><div class="value">$${summary.amountToSettle}</div></div>
        </div>

        <div class="card">
          <h2 class="section-title">My Maintenance Tasks</h2>
          <c:choose>
            <c:when test="${empty myMaintenanceTasks}"><p class="text-muted">None assigned.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>#</th><th>Equipment</th><th>Status</th><th>Hours</th><th>Credit</th></tr></thead>
                <tbody>
                  <c:forEach var="t" items="${myMaintenanceTasks}">
                    <tr>
                      <td>${t.maintenanceId}</td><td>${t.equipmentName} (${t.assetTag})</td>
                      <td><span class="badge badge-${fn:toLowerCase(t.status)}">${t.status}</span></td>
                      <td>${t.maintenanceHours}</td><td>$${t.creditEarned}</td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </c:otherwise>
          </c:choose>
        </div>

        <div class="card">
          <h2 class="section-title">My Work Orders</h2>
          <c:choose>
            <c:when test="${empty myWorkOrders}"><p class="text-muted">None assigned.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>#</th><th>Requester</th><th>Status</th><th>Credit</th></tr></thead>
                <tbody>
                  <c:forEach var="w" items="${myWorkOrders}">
                    <tr>
                      <td>${w.workOrderId}</td><td>${w.requesterLabel}</td>
                      <td><span class="badge badge-${fn:toLowerCase(w.status)}">${w.status}</span></td>
                      <td>$${w.creditEarned}</td>
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
