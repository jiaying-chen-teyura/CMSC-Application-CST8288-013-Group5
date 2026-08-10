<!-- Author: Jiaying Chen -->
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% request.setAttribute("pageTitle", "Training"); request.setAttribute("activeNav", "training"); %>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/WEB-INF/views/common/head.jsp" %>
</head>
<body>
  <div class="app-shell">
    <%@ include file="/WEB-INF/views/common/nav.jsp" %>
    <div class="app-main">
      <%@ include file="/WEB-INF/views/common/header.jsp" %>
      <main class="content">
        <div class="content-header"><p>Schedule and conduct intro/safety training sessions.</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="card">
          <h2 class="section-title">Schedule a Training Session</h2>
          <p class="hint">Pick a date, then an hour, then a minute — minutes are always :00, :15, :30, or :45.</p>
          <form class="form-grid" id="trainingForm" action="${pageContext.request.contextPath}/controller" method="POST">
            <input type="hidden" name="action" value="scheduleTraining">
            <div class="field"><label>Title</label><input type="text" name="title" required></div>
            <div class="field"><label>Equipment Category</label>
              <select name="category">
                <option value="THREE_D_PRINTER">3D Printer</option>
                <option value="LASER_CUTTER">Laser Cutter</option>
                <option value="CNC">CNC</option>
              </select>
            </div>
            <div class="field"><label>Start</label>
              <div class="qh-picker" id="scheduledStartField" data-name="scheduledStart"></div>
            </div>
            <div class="field"><label>End</label>
              <div class="qh-picker" id="scheduledEndField" data-name="scheduledEnd"></div>
            </div>
            <div class="field"><label>Location</label><input type="text" name="location"></div>
            <div class="field"><label>Capacity</label><input type="number" min="1" name="capacity" value="8" required></div>
            <div class="field"><button type="submit" class="btn btn-primary">Schedule</button></div>
          </form>
        </div>

        <div class="card">
          <h2 class="section-title">My Sessions </h2>
          <c:choose>
            <c:when test="${empty mySessionsTaught}"><p class="text-muted">No sessions yet.</p></c:when>
            <c:otherwise>
              <c:forEach var="t" items="${mySessionsTaught}">
                <div style="border-bottom:1px solid var(--color-border); padding: var(--space-3) 0;">
                  <strong>${t.title}</strong> &mdash; ${t.category} &mdash;
                  <span class="badge badge-${fn:toLowerCase(t.status)}">${t.status}</span>
                  &mdash; ${t.scheduledStart} to ${t.scheduledEnd} &mdash; Credit earned: $${t.trainerCredit}

                  <c:if test="${t.status == 'SCHEDULED'}">
                    <form action="${pageContext.request.contextPath}/controller" method="POST" class="mt-4">
                      <input type="hidden" name="action" value="conductTraining">
                      <input type="hidden" name="trainingSessionId" value="${t.trainingSessionId}">
                      <p class="hint mb-0">Mark complete and select attendees who passed:</p>
                      <div class="form-grid">
                        <c:forEach var="m" items="${allMembers}">
                          <label class="field" style="font-weight:400;">
                            <input type="checkbox" name="attendeeUserIds" value="${m.userId}"> ${m.name}
                          </label>
                        </c:forEach>
                      </div>
                      <button type="submit" class="btn btn-primary btn-small mt-4">Complete Session</button>
                    </form>
                  </c:if>
                </div>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </div>
      </main>
    </div>
  </div>
  <script src="${pageContext.request.contextPath}/assets/js/quarter-hour-field.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/time-range.js"></script>
  <script>initTimeRange("scheduledStartField", "scheduledEndField", "trainingForm", 15);</script>
</body>
</html>
