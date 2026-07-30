<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("pageTitle", "Consumables"); %>
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
          <p>Donate materials, and view stock levels and consumption.</p>
        </div>

        <!-- TODO: replace with consumable inventory table + donate form once the Consumable DAO/DTO exist. -->
        <div class="empty-state">
          <div class="icon">&#9679;</div>
          <h3>Consumable inventory coming soon</h3>
          <p>Stock levels, restock alerts, and the donation form will appear here once the Business and Data layers are connected.</p>
        </div>
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
