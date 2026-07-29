<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("pageTitle", "Dashboard"); %>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/views/common/head.jsp" %>
</head>
<body>
  <div class="app-shell">
    <%@ include file="/views/common/nav.jsp" %>

    <div class="app-main">
      <%@ include file="/views/common/header.jsp" %>

      <main class="content">
        <div class="content-header">
          <p>Overview of your account and the co-op's current status.</p>
        </div>

        <!-- FR-06 Reporting & Analytics: placeholder stat cards.
             Values are static for now — wire these up once the
             Business layer / DAO can supply real numbers. -->
        <div class="stat-grid">
          <div class="stat-card">
            <div class="label">Credit Balance</div>
            <div class="value placeholder">&mdash;</div>
            <div class="hint">Awaiting data source</div>
          </div>
          <div class="stat-card">
            <div class="label">Debit This Month</div>
            <div class="value placeholder">&mdash;</div>
            <div class="hint">Awaiting data source</div>
          </div>
          <div class="stat-card">
            <div class="label">Active Bookings</div>
            <div class="value placeholder">&mdash;</div>
            <div class="hint">Awaiting data source</div>
          </div>
          <div class="stat-card">
            <div class="label">Low-Stock Alerts</div>
            <div class="value placeholder">&mdash;</div>
            <div class="hint">Awaiting data source</div>
          </div>
        </div>

        <div class="empty-state">
          <div class="icon">&#9679;</div>
          <h3>Activity feed placeholder</h3>
          <p>Recent bookings, contributions, and alerts will show up here once the Business and Data layers are connected.</p>
        </div>
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
