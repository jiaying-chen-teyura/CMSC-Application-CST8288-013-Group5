<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Consumable" %>
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
          <p>Donate materials, and view stock levels. Data below is hard-coded for now (ConsumableServlet + ConsumableDao) until the real database is connected.</p>
        </div>

        <% if (request.getAttribute("message") != null) { %>
          <p class="info-banner"><%= request.getAttribute("message") %></p>
        <% } %>

        <table class="data-table">
          <tr>
            <th>Material</th>
            <th>Stock level</th>
            <th>Unit</th>
          </tr>
          <%
            List<Consumable> consumables = (List<Consumable>) request.getAttribute("consumables");
            for (Consumable c : consumables) {
          %>
          <tr>
            <td><%= c.getName() %></td>
            <td><%= c.getStockLevel() %></td>
            <td><%= c.getUnit() %></td>
          </tr>
          <% } %>
        </table>

        <h3>Donate consumables</h3>
        <form action="${pageContext.request.contextPath}/ConsumableServlet" method="post">
          <div class="field">
            <label for="name">Material</label>
            <select id="name" name="name">
              <%
                for (Consumable c : consumables) {
              %>
              <option value="<%= c.getName() %>"><%= c.getName() %></option>
              <% } %>
            </select>
          </div>
          <div class="field">
            <label for="amount">Amount</label>
            <input type="number" id="amount" name="amount" min="1" required>
          </div>
          <button type="submit" class="btn btn-primary">Donate</button>
        </form>
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
