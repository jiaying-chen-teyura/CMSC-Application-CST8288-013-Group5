<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%--
  Landing page. For now, just send everyone to login.jsp.
  Once AccountServlet exists and can check for a logged-in
  session, this should route logged-in users straight to
  views/dashboard/dashboard.jsp instead.
--%>
<% response.sendRedirect(request.getContextPath() + "/login.jsp"); %>
