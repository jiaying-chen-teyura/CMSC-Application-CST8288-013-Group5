<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("pageTitle", "Billing"); %>
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
          <p>Credit and debit history, and end-of-month settlement.</p>
        </div>

        <!-- TODO: replace with ledger table + settle-debit action once the Billing DAO/DTO exist. -->
        <div class="empty-state">
          <div class="icon">&#36;</div>
          <h3>Billing history coming soon</h3>
          <p>Your credit/debit ledger and settlement status will appear here once the Business and Data layers are connected.</p>
        </div>
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
