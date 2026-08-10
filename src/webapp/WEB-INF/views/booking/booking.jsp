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
        <div class="content-header"><p>Book a slot, then check equipment in/out and report materials consumed .</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="card">
          <h2 class="section-title">Book a Slot</h2>
          <p class="hint">Pick a date, then an hour, then a minute — minutes are always :00, :15, :30, or :45, so it's easy to estimate a real job. End time is always after start time.</p>
          <form class="form-grid" id="bookingForm" action="${pageContext.request.contextPath}/controller" method="POST">
            <input type="hidden" name="action" value="bookEquipment">
            <div class="field"><label>Equipment</label>
              <select name="assetTag" required>
                <c:forEach var="e" items="${equipmentList}">
                  <option value="${e.assetTag}">${e.equipmentName} (${e.assetTag}) &mdash; $${e.accessCreditRate}/hr</option>
                </c:forEach>
              </select>
            </div>
            <div class="field"><label>Start Time</label>
              <div class="qh-picker" id="startTimeField" data-name="startTime"></div>
            </div>
            <div class="field"><label>End Time</label>
              <div class="qh-picker" id="endTimeField" data-name="endTime"></div>
            </div>
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
          <p class="hint">Check in or out right from the row below — no need to hunt through the session report for it.</p>
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
                        <c:choose>
                          <c:when test="${b.bookingStatus == 'BOOKED'}">
                            <form style="display:inline" action="${pageContext.request.contextPath}/controller" method="POST">
                              <input type="hidden" name="action" value="checkInEquipment">
                              <input type="hidden" name="assetTag" value="${b.assetTag}">
                              <input type="hidden" name="bookingId" value="${b.bookingId}">
                              <button type="submit" class="btn btn-primary btn-small">Check In</button>
                            </form>
                            <form style="display:inline" action="${pageContext.request.contextPath}/controller" method="POST">
                              <input type="hidden" name="action" value="cancelBooking">
                              <input type="hidden" name="bookingId" value="${b.bookingId}">
                              <button type="submit" class="btn btn-secondary btn-small">Cancel</button>
                            </form>
                          </c:when>
                          <c:when test="${not empty b.activeUsageSessionId}">
                            <button type="button" class="btn btn-secondary btn-small"
                              onclick="document.getElementById('coModal-${b.activeUsageSessionId}').style.display='block';">Check Out</button>
                          </c:when>
                        </c:choose>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </c:otherwise>
          </c:choose>
        </div>

        <div class="card">
          <h2 class="section-title">Live Equipment / Session Report </h2>
          <c:choose>
            <c:when test="${empty activeSessions}"><p class="text-muted">No equipment is currently checked out.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>Session</th><th>Equipment</th><th>User</th><th>Checked in</th><th>Elapsed</th><th>Rate</th><th>Materials Consumed</th><th></th></tr></thead>
                <tbody>
                  <c:forEach var="s" items="${activeSessions}">
                    <tr>
                      <td>#${s.usageSessionId}</td>
                      <td>${s.equipmentName} (${s.assetTag})</td>
                      <td>${s.userName}</td>
                      <td>${s.checkInTime}</td>
                      <td>${s.elapsedMinutes / 60}h ${s.elapsedMinutes % 60}m</td>
                      <td>$${s.hourlyRate}/hr</td>
                      <td>
                        <c:choose>
                          <c:when test="${empty s.materialsUsed}"><span class="text-muted">None reported yet</span></c:when>
                          <c:otherwise>
                            <c:forEach var="m" items="${s.materialsUsed}" varStatus="st">${m.materialName}: ${m.quantityUsed}${m.unit}<c:if test="${!st.last}">, </c:if></c:forEach>
                          </c:otherwise>
                        </c:choose>
                      </td>
                      <td>
                        <c:if test="${s.userId == sessionScope.currentUser.userId}">
                          <button type="button" class="btn btn-secondary btn-small"
                            onclick="document.getElementById('coModal-${s.usageSessionId}').style.display='block';">Check Out</button>
                        </c:if>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </c:otherwise>
          </c:choose>
        </div>

        <!-- One simple inline checkout panel per active session belonging to the current user -->
        <c:forEach var="s" items="${activeSessions}">
          <c:if test="${s.userId == sessionScope.currentUser.userId}">
            <div class="card" id="coModal-${s.usageSessionId}" style="display:none;">
              <h2 class="section-title">Check Out &mdash; ${s.equipmentName} (Session #${s.usageSessionId})</h2>
              <form action="${pageContext.request.contextPath}/controller" method="POST">
                <input type="hidden" name="action" value="checkOutEquipment">
                <input type="hidden" name="usageSessionId" value="${s.usageSessionId}">
                <p class="text-muted">Optionally report consumables used for your project (FR-04):</p>
                <c:choose>
                  <c:when test="${empty s.availableConsumables}">
                    <p class="text-muted">This equipment has no consumable types registered against it.</p>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="c" items="${s.availableConsumables}" varStatus="st">
                      <div class="form-row-split">
                        <div class="field">
                          <label>${c.materialName} (${c.unit}, $${c.unitDebitRate}/unit) &mdash; stock: ${c.currentStock}</label>
                          <input type="hidden" name="consumableId" value="${c.consumableId}">
                        </div>
                        <div class="field">
                          <input type="number" step="0.01" min="0" name="quantity" placeholder="Quantity used (leave 0 if none)" value="0">
                        </div>
                      </div>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
                <button type="submit" class="btn btn-primary">Confirm Check Out</button>
              </form>
            </div>
          </c:if>
        </c:forEach>
      </main>
    </div>
  </div>
  <script src="${pageContext.request.contextPath}/assets/js/quarter-hour-field.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/time-range.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/booking.js"></script>
</body>
</html>
