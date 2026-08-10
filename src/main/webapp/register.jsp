<!-- Author: Jiaying Chen -->
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
        Join the co-op.<br>
        Start <span class="accent">making</span>.
      </div>

      <div class="footnote">Campus Maker Space Co-op</div>
    </div>

    <div class="auth-form-side">
      <div class="auth-card">
        <h1>Create your account</h1>
        <p class="subtitle">Register as a User, Trainer, or Shop-Tech.</p>

        <c:if test="${not empty errorMessage}">
          <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/controller" method="POST"
              onsubmit="return document.getElementById('password').value === document.getElementById('confirmPassword').value
                        || (alert('Passwords do not match'), false);">
          <input type="hidden" name="action" value="register">
          <div class="form-row-split">
            <div class="field">
              <label for="firstName">First name</label>
              <input type="text" id="firstName" name="firstName" placeholder="Jane" required>
            </div>
            <div class="field">
              <label for="lastName">Last name</label>
              <input type="text" id="lastName" name="lastName" placeholder="Student" required>
            </div>
          </div>

          <div class="field">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" placeholder="you@algonquinlive.com" required>
          </div>

          <div class="form-row-split">
            <div class="field">
              <label for="password">Password</label>
              <input type="password" id="password" name="password" placeholder="&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;" required minlength="6">
            </div>
            <div class="field">
              <label for="confirmPassword">Confirm password</label>
              <input type="password" id="confirmPassword" name="confirmPassword" placeholder="&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;" required minlength="6">
            </div>
          </div>

          <div class="field">
            <label for="userType">Account type</label>
            <select id="userType" name="userType">
              <option value="USER">User</option>
              <option value="TRAINER">Trainer</option>
              <option value="SHOP_TECH">Shop-Tech</option>
            </select>
            <div class="hint">Trainer and Shop-Tech are special types of User, per the project spec.</div>
          </div>

          <button type="submit" class="btn btn-primary" style="width:100%;">Create account</button>
        </form>

        <p class="auth-switch">Already have an account? <a href="login.jsp">Log in</a></p>
      </div>
    </div>
  </div>
</body>
</html>
