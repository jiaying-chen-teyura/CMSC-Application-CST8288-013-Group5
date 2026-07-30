<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.LedgerEntry" %>
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
          <p>Credit and debit history. Data below is hard-coded for now (BillingServlet + BillingDao) until the real database is connected.</p>
        </div>

        <% if (request.getAttribute("message") != null) { %>
          <p class="info-banner"><%= request.getAttribute("message") %></p>
        <% } %>

        <table class="data-table">
          <tr>
            <th>Date</th>
            <th>Type</th>
            <th>Amount</th>
            <th>Description</th>
          </tr>
          <%
            List<LedgerEntry> entries = (List<LedgerEntry>) request.getAttribute("entries");
            for (LedgerEntry e : entries) {
          %>
          <tr>
            <td><%= e.getEntryDate() %></td>
            <td><%= e.getType() %></td>
            <td>$<%= String.format("%.2f", e.getAmount()) %></td>
            <td><%= e.getDescription() %></td>
          </tr>
          <% } %>
          <tr>
            <td colspan="2"><b>Balance</b></td>
            <td colspan="2"><b>$<%= String.format("%.2f", (Double) request.getAttribute("balance")) %></b></td>
          </tr>
        </table>

        <form action="${pageContext.request.contextPath}/BillingServlet" method="post">
          <button type="submit" class="btn btn-primary">Settle debits</button>
        </form>
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
