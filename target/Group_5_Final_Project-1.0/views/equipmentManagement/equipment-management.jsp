<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Equipment" %>
<% request.setAttribute("pageTitle", "Equipment Management"); %>
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
          <p>Shop-Tech: register or remove equipment. Data below is hard-coded for now (EquipmentManagementServlet + EquipmentDao) until the real database is connected.</p>
        </div>

        <% if (request.getAttribute("message") != null) { %>
          <p class="info-banner"><%= request.getAttribute("message") %></p>
        <% } %>

        <table class="data-table">
          <tr>
            <th>Asset Tag</th>
            <th>Name</th>
            <th>Category</th>
            <th>Status</th>
            <th></th>
          </tr>
          <%
            List<Equipment> equipment = (List<Equipment>) request.getAttribute("equipment");
            for (Equipment e : equipment) {
          %>
          <tr>
            <td><%= e.getAssetTag() %></td>
            <td><%= e.getName() %></td>
            <td><%= e.getCategory() %></td>
            <td><%= e.getStatus() %></td>
            <td>
              <form action="${pageContext.request.contextPath}/EquipmentManagementServlet" method="post">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="assetTag" value="<%= e.getAssetTag() %>">
                <button type="submit" class="btn btn-secondary">Remove</button>
              </form>
            </td>
          </tr>
          <% } %>
        </table>

        <h3>Register new equipment</h3>
        <form action="${pageContext.request.contextPath}/EquipmentManagementServlet" method="post">
          <input type="hidden" name="action" value="register">
          <div class="field">
            <label for="assetTag">Asset tag</label>
            <input type="text" id="assetTag" name="assetTag" required>
          </div>
          <div class="field">
            <label for="name">Name</label>
            <input type="text" id="name" name="name" required>
          </div>
          <div class="field">
            <label for="category">Category</label>
            <input type="text" id="category" name="category" required>
          </div>
          <button type="submit" class="btn btn-primary">Register equipment</button>
        </form>
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
