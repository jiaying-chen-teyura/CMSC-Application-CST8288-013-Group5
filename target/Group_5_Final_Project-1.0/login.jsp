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
        Contribute your time.<br>
        Earn your <span class="accent">access</span>.
      </div>

      <div class="footnote">Campus Maker Space Co-op &middot; CST8288</div>
    </div>

    <div class="auth-form-side">
      <div class="auth-card">
        <h1>Welcome back</h1>
        <p class="subtitle">Log in to book equipment, track credits, and more.</p>

        <% if (request.getAttribute("error") != null) { %>
          <p style="color:red;"><%= request.getAttribute("error") %></p>
        <% } else if ("true".equals(request.getParameter("registered"))) { %>
          <p style="color:green;">Account created - log in below.</p>
        <% } %>

        <form action="LoginServlet" method="POST">
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
      </div>
    </div>
  </div>
</body>
</html>
