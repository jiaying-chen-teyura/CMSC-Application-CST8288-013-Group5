<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.WorkOrder" %>
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
          <p>Submit, accept, and track fabrication work orders. Data below is hard-coded for now (WorkOrderServlet + WorkOrderDao) until the real database is connected.</p>
        </div>

        <% if (request.getAttribute("message") != null) { %>
          <p class="info-banner"><%= request.getAttribute("message") %></p>
        <% } %>

        <table class="data-table">
          <tr>
            <th>#</th>
            <th>Description</th>
            <th>Requested by</th>
            <th>Status</th>
            <th></th>
          </tr>
          <%
            List<WorkOrder> workOrders = (List<WorkOrder>) request.getAttribute("workOrders");
            for (WorkOrder w : workOrders) {
          %>
          <tr>
            <td><%= w.getId() %></td>
            <td><%= w.getDescription() %></td>
            <td><%= w.getRequestedBy() %></td>
            <td><%= w.getStatus() %></td>
            <td>
              <% if ("Pending".equals(w.getStatus())) { %>
              <form action="${pageContext.request.contextPath}/WorkOrderServlet" method="post">
                <input type="hidden" name="id" value="<%= w.getId() %>">
                <button type="submit" class="btn btn-primary">Accept</button>
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
