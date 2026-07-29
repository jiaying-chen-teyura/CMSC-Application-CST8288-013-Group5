<%--
  Top bar — included by every page under views/.
  Title text is set per-page below (each page passes its own
  title in before including this file) via the pageTitle
  request attribute; falls back to "Dashboard" if not set.
--%>
<header class="topbar">
  <h1 class="topbar-title">${empty pageTitle ? "Dashboard" : pageTitle}</h1>
  <div class="topbar-user">
    <div class="user-chip">
      <span class="avatar">JS</span>
      <span>Jane Student &middot; User</span>
    </div>
    <a class="link-muted" href="${pageContext.request.contextPath}/login.jsp">Log out</a>
  </div>
</header>
