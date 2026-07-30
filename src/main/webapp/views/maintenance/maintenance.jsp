<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("pageTitle", "Maintenance"); %>
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
          <p>Predictive maintenance alerts and scheduled service tasks.</p>
        </div>

        <!-- TODO: replace with maintenance alert list + schedule form once the Maintenance DAO/DTO exist. -->
        <div class="empty-state">
          <div class="icon">&#9888;</div>
          <h3>Maintenance alerts coming soon</h3>
          <p>Wear tracking, diagnostics, and scheduled maintenance will appear here once the Business and Data layers are connected.</p>
        </div>
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
