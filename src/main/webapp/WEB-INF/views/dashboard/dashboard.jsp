<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% request.setAttribute("pageTitle", "Dashboard"); request.setAttribute("activeNav", "dashboard"); %>
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
        <div class="content-header">
          <p>Overview of your account and the co-op's current status.</p>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- FR-06 Reporting & Analytics: live numbers from LedgerBusinessLogic / v_user_monthly_account_report -->
        <div class="stat-grid">
          <div class="stat-card">
            <div class="label">Credits This Month</div>
            <div class="value">$<fmt:formatNumber value="${ledgerSummary.totalCredits}" maxFractionDigits="2" minFractionDigits="2"/></div>
            <div class="hint">Donations, training, maintenance, work orders</div>
          </div>
          <div class="stat-card">
            <div class="label">Debits This Month</div>
            <div class="value">$<fmt:formatNumber value="${ledgerSummary.totalDebits}" maxFractionDigits="2" minFractionDigits="2"/></div>
            <div class="hint">Equipment access + materials used</div>
          </div>
          <div class="stat-card">
            <div class="label">Amount to Settle</div>
            <div class="value">$<fmt:formatNumber value="${ledgerSummary.amountToSettle}" maxFractionDigits="2" minFractionDigits="2"/></div>
            <div class="hint">Due within 10 days of month end</div>
          </div>
          <div class="stat-card">
            <div class="label">My Active Sessions</div>
            <c:set var="activeCount" value="0"/>
            <c:forEach var="s" items="${mySessions}"><c:if test="${s.sessionStatus == 'ACTIVE'}"><c:set var="activeCount" value="${activeCount + 1}"/></c:if></c:forEach>
            <div class="value">${activeCount}</div>
            <div class="hint">Equipment currently checked out to you</div>
          </div>
        </div>

        <c:if test="${sessionScope.currentUser.shopTech || sessionScope.currentUser.admin}">
          <div class="card">
            <h2 class="section-title">Open Maintenance Alerts (FR-05)</h2>
            <c:choose>
              <c:when test="${empty openAlerts}"><p class="text-muted">No open maintenance alerts.</p></c:when>
              <c:otherwise>
                <table class="data-table">
                  <thead><tr><th>Equipment</th><th>Component</th><th>Priority</th><th>Status</th></tr></thead>
                  <tbody>
                    <c:forEach var="t" items="${openAlerts}">
                      <tr>
                        <td>${t.equipmentName} (${t.assetTag})</td>
                        <td>${t.componentName}</td>
                        <td><span class="badge badge-${fn:toLowerCase(t.priority)}">${t.priority}</span></td>
                        <td><span class="badge badge-${fn:toLowerCase(t.status)}">${t.status}</span></td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </c:otherwise>
            </c:choose>
          </div>

          <div class="card">
            <h2 class="section-title">Open Work Orders</h2>
            <c:choose>
              <c:when test="${empty openWorkOrders}"><p class="text-muted">No open work orders.</p></c:when>
              <c:otherwise>
                <table class="data-table">
                  <thead><tr><th>#</th><th>Requester</th><th>Priority</th><th>Status</th></tr></thead>
                  <tbody>
                    <c:forEach var="w" items="${openWorkOrders}">
                      <tr>
                        <td>${w.workOrderId}</td><td>${w.requesterLabel}</td>
                        <td><span class="badge badge-${fn:toLowerCase(w.priority)}">${w.priority}</span></td>
                        <td><span class="badge badge-${fn:toLowerCase(w.status)}">${w.status}</span></td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </c:otherwise>
            </c:choose>
          </div>
        </c:if>

        <c:if test="${empty openAlerts && empty openWorkOrders}">
          <div class="empty-state">
            <div class="icon">&#9679;</div>
            <h3>You're all caught up</h3>
            <p>Use the sidebar to book equipment, donate materials, submit a work order, or check your ledger.</p>
          </div>
        </c:if>
      </main>
    </div>
  </div>
</body>
</html>
