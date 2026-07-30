<%--
  Top bar - included by every page under views/.
  Title text is set per-page below (each page passes its own
  title in before including this file) via the pageTitle
  request attribute; falls back to "Dashboard" if not set.

  User chip now reads the logged-in user's name/role from the
  session (set by LoginServlet) instead of being hard-coded.
--%>
<%
  String headerUserName = (String) session.getAttribute("userName");
  String headerUserType = (String) session.getAttribute("userType");
  if (headerUserName == null) {
      headerUserName = "Guest";
      headerUserType = "Not logged in";
  }
  String[] headerNameParts = headerUserName.split(" ");
  String headerInitials = headerNameParts.length > 1
      ? ("" + headerNameParts[0].charAt(0) + headerNameParts[1].charAt(0))
      : headerUserName.substring(0, Math.min(2, headerUserName.length()));
  headerInitials = headerInitials.toUpperCase();
%>
<header class="topbar">
  <h1 class="topbar-title">${empty pageTitle ? "Dashboard" : pageTitle}</h1>
  <div class="topbar-user">
    <div class="user-chip">
      <span class="avatar"><%= headerInitials %></span>
      <span><%= headerUserName %> &middot; <%= headerUserType %></span>
    </div>
    <a class="link-muted" href="${pageContext.request.contextPath}/login.jsp">Log out</a>
  </div>
</header>
