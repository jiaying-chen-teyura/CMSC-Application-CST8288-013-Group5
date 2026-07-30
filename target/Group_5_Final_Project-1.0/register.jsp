<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/views/common/head.jsp" %>
</head>
<body>
  <div class="auth-shell">
    <div class="auth-brand">
      <span class="mark"><span class="dot"></span>CMSC</span>

      <div class="headline">
        Join the co-op.<br>
        Start <span class="accent">making</span>.
      </div>

      <div class="footnote">Campus Maker Space Co-op &middot; CST8288</div>
    </div>

    <div class="auth-form-side">
      <div class="auth-card">
        <h1>Create your account</h1>
        <p class="subtitle">Register as a User, Trainer, or Shop-Tech.</p>

        <!-- No action/method wired yet — AccountServlet will handle
             this once the Business layer exists. -->
        
        <!-- testing git -->
        <form>
          <div class="field">
            <label for="name">Full name</label>
            <input type="text" id="name" name="name" placeholder="Jane Student" required>
          </div>

          <div class="field">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" placeholder="you@algonquinlive.com" required>
          </div>

          <div class="form-row-split">
            <div class="field">
              <label for="password">Password</label>
              <input type="password" id="password" name="password" placeholder="&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;" required>
            </div>
            <div class="field">
              <label for="confirmPassword">Confirm password</label>
              <input type="password" id="confirmPassword" name="confirmPassword" placeholder="&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;" required>
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
