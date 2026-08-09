<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/WEB-INF/views/common/head.jsp" %>
</head>
<body>
  <div class="auth-shell">
    <div class="auth-brand">
      <span class="mark"><span class="dot"></span>CMSC</span>

      <div class="headline">
        Contribute your time.<br>
        Earn your <span class="accent">access</span>.
      </div>

      <div class="footnote">Campus Maker Space Co-op</div>
    </div>

    <div class="auth-form-side">
      <div class="auth-card">
        <h1>Welcome back</h1>
        <p class="subtitle">Log in to book equipment, track credits, and more.</p>

        <c:if test="${not empty errorMessage}">
          <div class="alert alert-error">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty param.error}">
          <div class="alert alert-error">${param.error}</div>
        </c:if>
        <c:if test="${param.registered == 'true'}">
          <div class="alert alert-info">Account created &mdash; log in below.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/controller" method="POST">
          <input type="hidden" name="action" value="login">
          <div class="field">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" placeholder="you@example.com" required>
          </div>

          <div class="field">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" placeholder="&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;" required>
          </div>

          <button type="submit" class="btn btn-primary" style="width:100%;">Log in</button>
        </form>

        <p class="auth-switch">Don't have an account? <a href="register.jsp">Register</a></p>
        <p class="auth-switch"><a href="${pageContext.request.contextPath}/external-request.jsp">Requesting a job as an external client?</a></p>
      </div>
    </div>
  </div>
</body>
</html>
