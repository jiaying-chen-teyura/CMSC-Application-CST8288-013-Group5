<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("pageTitle", "Work Orders"); %>
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
          <p>Submit, accept, and track fabrication work orders.</p>
        </div>

        <!-- TODO: replace with work order table + accept/check-in/out actions once the WorkOrder DAO/DTO exist. -->
        <div class="empty-state">
          <div class="icon">&#128203;</div>
          <h3>Work orders coming soon</h3>
          <p>Submitted jobs, their status, and history will appear here once the Business and Data layers are connected.</p>
        </div>
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
