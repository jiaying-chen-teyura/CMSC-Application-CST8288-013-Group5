<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Equipment" %>
<% request.setAttribute("pageTitle", "Book Equipment"); %>
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
          <p>Book equipment for your own projects. Data below is hard-coded for now (BookEquipmentServlet + EquipmentDao) until the real database is connected.</p>
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
              <% if ("Available".equals(e.getStatus())) { %>
              <form action="${pageContext.request.contextPath}/BookEquipmentServlet" method="post">
                <input type="hidden" name="assetTag" value="<%= e.getAssetTag() %>">
                <button type="submit" class="btn btn-primary">Book</button>
              </form>
              <% } %>
            </td>
          </tr>
          <% } %>
        </table>
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
