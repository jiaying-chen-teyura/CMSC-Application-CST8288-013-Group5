<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("pageTitle", "Equipment"); %>
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
          <p>Book equipment, check in/out, and view equipment status.</p>
        </div>

        <!-- TODO: replace with equipment table + booking form once EquipmentServlet and the Equipment DAO/DTO exist. -->
        <div class="empty-state">
          <div class="icon">&#9881;</div>
          <h3>Equipment list coming soon</h3>
          <p>This section will show available equipment, booking controls, and live status once the Business and Data layers are connected.</p>
        </div>
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
