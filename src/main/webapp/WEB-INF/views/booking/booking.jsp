<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% request.setAttribute("pageTitle", "Book Equipment"); request.setAttribute("activeNav", "equipmentAvailability"); %>
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
        <div class="content-header"><p>View equipment availability and book a slot (FR-02).</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="card">
          <h2 class="section-title">Book a Slot</h2>
          <form class="form-grid" action="${pageContext.request.contextPath}/controller" method="POST">
            <input type="hidden" name="action" value="bookEquipment">
            <div class="field"><label>Equipment</label>
              <select name="assetTag" required>
                <c:forEach var="e" items="${equipmentList}">
                  <option value="${e.assetTag}">${e.equipmentName} (${e.assetTag}) &mdash; $${e.accessCreditRate}/hr</option>
                </c:forEach>
              </select>
            </div>
            <div class="field"><label>Start Time</label><input type="datetime-local" name="startTime" required></div>
            <div class="field"><label>End Time</label><input type="datetime-local" name="endTime" required></div>
            <div class="field"><button type="submit" class="btn btn-primary">Book Equipment</button></div>
          </form>
        </div>

        <div class="card">
          <h2 class="section-title">Equipment Availability</h2>
          <c:choose>
            <c:when test="${empty equipmentList}"><p class="text-muted">No active equipment.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>Asset Tag</th><th>Name</th><th>Category</th><th>Status</th><th>Rate</th><th>Location</th></tr></thead>
                <tbody>
                  <c:forEach var="e" items="${equipmentList}">
                    <tr>
                      <td>${e.assetTag}</td><td>${e.equipmentName}</td><td>${e.category}</td>
                      <td><span class="badge badge-${fn:toLowerCase(e.status)}">${e.status}</span></td>
                      <td>$${e.accessCreditRate}/hr</td><td>${e.location}</td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </c:otherwise>
          </c:choose>
        </div>

        <div class="card">
          <h2 class="section-title">My Bookings</h2>
          <c:choose>
            <c:when test="${empty myBookings}"><p class="text-muted">You have no bookings yet.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>Equipment</th><th>Start</th><th>End</th><th>Status</th><th></th></tr></thead>
                <tbody>
                  <c:forEach var="b" items="${myBookings}">
                    <tr>
                      <td>${b.assetTag}</td><td>${b.startTime}</td><td>${b.endTime}</td>
                      <td><span class="badge badge-${fn:toLowerCase(b.bookingStatus)}">${b.bookingStatus}</span></td>
                      <td>
                        <c:if test="${b.bookingStatus == 'BOOKED'}">
                          <form style="display:inline" action="${pageContext.request.contextPath}/controller" method="POST">
                            <input type="hidden" name="action" value="cancelBooking">
                            <input type="hidden" name="bookingId" value="${b.bookingId}">
                            <button type="submit" class="btn btn-secondary btn-small">Cancel</button>
                          </form>
                        </c:if>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </c:otherwise>
          </c:choose>
        </div>
      </main>
    </div>
  </div>
</body>
</html>
