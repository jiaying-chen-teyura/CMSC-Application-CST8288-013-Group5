<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
  Top bar — included by every page under WEB-INF/views/.
  Shows the actual logged-in user (from the session, set by
  LoginCommand/RegisterCommand) instead of the old hardcoded name.
--%>
<header class="topbar">
  <h1 class="topbar-title">${empty pageTitle ? "Dashboard" : pageTitle}</h1>
  <div class="topbar-user">
    <c:if test="${not empty sessionScope.currentUser}">
      <div class="user-chip">
        <span class="avatar">${fn:substring(sessionScope.currentUser.name, 0, 1)}</span>
        <span>${sessionScope.currentUser.name} &middot; ${sessionScope.currentUser.userType}</span>
      </div>
      <a class="link-muted" href="${pageContext.request.contextPath}/controller?action=logout">Log out</a>
    </c:if>
  </div>
</header>
