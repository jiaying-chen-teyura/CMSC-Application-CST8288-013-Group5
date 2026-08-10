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
      <div class="headline">Get it <span class="accent">fabricated</span>.<br>No membership needed.</div>
      <div class="footnote">Campus Maker Space Co-op</div>
    </div>

    <div class="auth-form-side">
      <div class="auth-card">
        <h1>Request a fabrication job</h1>
        <p class="subtitle">For external clients &mdash; a Shop-Tech will review and quote your job.</p>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <form action="${pageContext.request.contextPath}/controller" method="POST">
          <input type="hidden" name="action" value="submitWorkOrder">
          <input type="hidden" name="isExternal" value="true">

          <div class="form-row-split">
            <div class="field">
              <label for="firstName">First name</label>
              <input type="text" id="firstName" name="firstName" required>
            </div>
            <div class="field">
              <label for="lastName">Last name</label>
              <input type="text" id="lastName" name="lastName" required>
            </div>
          </div>
          <div class="field">
            <label for="organization">Organization (optional)</label>
            <input type="text" id="organization" name="organization">
          </div>
          <div class="form-row-split">
            <div class="field">
              <label for="contactEmail">Email</label>
              <input type="email" id="contactEmail" name="contactEmail" required>
            </div>
            <div class="field">
              <label for="contactPhone">Phone</label>
              <input type="text" id="contactPhone" name="contactPhone" required>
            </div>
          </div>
          <div class="field">
            <label for="jobDetails">Describe the job</label>
            <textarea id="jobDetails" name="jobDetails" rows="4" required></textarea>
          </div>
          <div class="form-row-split">
            <div class="field">
              <label for="labourRate">Work order rate ($ credits for Shop-Tech)</label>
              <input type="number" id="labourRate" name="labourRate" step="0.01" min="0.01" placeholder="e.g. 25.00" required>
            </div>
            <div class="field field-checkbox">
              <label class="checkbox-label"><input type="checkbox" name="isUrgent" value="true"> This is urgent (rush priority)</label>
            </div>
          </div>

          <button type="submit" class="btn btn-primary" style="width:100%;">Submit request</button>
        </form>

        <p class="auth-switch"><a href="${pageContext.request.contextPath}/login.jsp">Back to member login</a></p>
      </div>
    </div>
  </div>
</body>
</html>
