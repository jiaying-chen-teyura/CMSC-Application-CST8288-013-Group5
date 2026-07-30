<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.MaintenanceAlert" %>
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
          <p>Predictive maintenance alerts. Data below is hard-coded for now (MaintenanceServlet + MaintenanceDao) until the real database is connected.</p>
        </div>

        <% if (request.getAttribute("message") != null) { %>
          <p class="info-banner"><%= request.getAttribute("message") %></p>
        <% } %>

        <table class="data-table">
          <tr>
            <th>Equipment</th>
            <th>Issue</th>
            <th>Status</th>
            <th></th>
          </tr>
          <%
            List<MaintenanceAlert> alerts = (List<MaintenanceAlert>) request.getAttribute("alerts");
            for (MaintenanceAlert a : alerts) {
          %>
          <tr>
            <td><%= a.getEquipmentName() %></td>
            <td><%= a.getIssue() %></td>
            <td><%= a.getStatus() %></td>
            <td>
              <% if ("Open".equals(a.getStatus())) { %>
              <form action="${pageContext.request.contextPath}/MaintenanceServlet" method="post">
                <input type="hidden" name="equipmentName" value="<%= a.getEquipmentName() %>">
                <button type="submit" class="btn btn-primary">Schedule</button>
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
