<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.TrainingSession" %>
<% request.setAttribute("pageTitle", "Training"); %>
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
          <p>Schedule and review training sessions. Data below is hard-coded for now (TrainingServlet + TrainingDao) until the real database is connected.</p>
        </div>

        <% if (request.getAttribute("message") != null) { %>
          <p class="info-banner"><%= request.getAttribute("message") %></p>
        <% } %>

        <table class="data-table">
          <tr>
            <th>Topic</th>
            <th>Trainer</th>
            <th>Date</th>
          </tr>
          <%
            List<TrainingSession> sessions = (List<TrainingSession>) request.getAttribute("sessions");
            for (TrainingSession s : sessions) {
          %>
          <tr>
            <td><%= s.getTopic() %></td>
            <td><%= s.getTrainerName() %></td>
            <td><%= s.getSessionDate() %></td>
          </tr>
          <% } %>
        </table>

        <h3>Schedule a training session</h3>
        <form action="${pageContext.request.contextPath}/TrainingServlet" method="post">
          <div class="field">
            <label for="topic">Topic</label>
            <input type="text" id="topic" name="topic" required>
          </div>
          <div class="field">
            <label for="trainerName">Trainer</label>
            <input type="text" id="trainerName" name="trainerName" required>
          </div>
          <div class="field">
            <label for="sessionDate">Date</label>
            <input type="date" id="sessionDate" name="sessionDate" required>
          </div>
          <button type="submit" class="btn btn-primary">Schedule session</button>
        </form>
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
