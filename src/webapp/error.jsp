<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
  Real error page for 404/500s (see web.xml). Previously both error codes
  pointed at login.jsp, which - because it never touches the session -
  left the user still logged in but staring at the login form, looking
  exactly like an unexplained logout. This page shows what actually
  happened instead, and sends the member back to a safe, logged-in page.
--%>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/WEB-INF/views/common/head.jsp" %>
</head>
<body>
  <div class="auth-shell">
    <div class="auth-brand">
      <span class="mark"><span class="dot"></span>CMSC</span>
      <div class="headline">Something went<br>wrong.</div>
      <div class="footnote">Campus Maker Space Co-op</div>
    </div>

    <div class="auth-form-side">
      <div class="auth-card">
        <h1>Unexpected error</h1>
        <p class="subtitle">
          <c:choose>
            <c:when test="${pageContext.errorData.statusCode == 404}">
              The page you were looking for doesn't exist.
            </c:when>
            <c:otherwise>
              Something went wrong on our end while handling your request.
            </c:otherwise>
          </c:choose>
        </p>

        <c:if test="${not empty pageContext.exception}">
          <div class="alert alert-error">${pageContext.exception.message}</div>
        </c:if>

        <p class="auth-switch">
          <a href="${pageContext.request.contextPath}/controller?action=dashboard">Back to Dashboard</a>
        </p>
        <p class="auth-switch">
          <a href="${pageContext.request.contextPath}/login.jsp">Go to Login</a>
        </p>
      </div>
    </div>
  </div>
</body>
</html>
