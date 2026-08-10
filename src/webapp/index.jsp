<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Landing page: send logged-in users to the dashboard, everyone else to login. --%>
<%
  if (session.getAttribute("currentUser") != null) {
    response.sendRedirect(request.getContextPath() + "/controller?action=dashboard");
  } else {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
  }
%>
