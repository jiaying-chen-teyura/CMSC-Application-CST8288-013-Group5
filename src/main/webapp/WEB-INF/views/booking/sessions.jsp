<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% request.setAttribute("pageTitle", "Check In / Check Out"); request.setAttribute("activeNav", "sessions"); %>
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
        <div class="content-header"><p>Check equipment in/out and report materials consumed (FR-03/FR-04).</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="card">
          <h2 class="section-title">Check In</h2>
          <form class="inline-form" action="${pageContext.request.contextPath}/controller" method="POST">
            <input type="hidden" name="action" value="checkInEquipment">
            <div class="field"><label>Equipment</label>
              <select name="assetTag" required>
                <c:forEach var="e" items="${equipmentList}">
                  <c:if test="${e.status == 'AVAILABLE'}">
                    <option value="${e.assetTag}">${e.equipmentName} (${e.assetTag})</option>
                  </c:if>
                </c:forEach>
              </select>
            </div>
            <div class="field"><label>My Booking (optional)</label>
              <select name="bookingId">
                <option value="">Walk-in (no booking)</option>
                <c:forEach var="b" items="${myBookings}">
                  <c:if test="${b.bookingStatus == 'BOOKED'}">
                    <option value="${b.bookingId}">#${b.bookingId} &mdash; ${b.assetTag}</option>
                  </c:if>
                </c:forEach>
              </select>
            </div>
            <div class="field"><button type="submit" class="btn btn-primary">Check In</button></div>
          </form>
        </div>

        <div class="card">
          <h2 class="section-title">Live Equipment / Session Report (FR-03)</h2>
          <c:choose>
            <c:when test="${empty activeSessions}"><p class="text-muted">No equipment is currently checked out.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>Session</th><th>Equipment</th><th>User</th><th>Checked in</th><th>Rate</th><th></th></tr></thead>
                <tbody>
                  <c:forEach var="s" items="${activeSessions}">
                    <tr>
                      <td>#${s.usageSessionId}</td>
                      <td>${s.equipmentName} (${s.assetTag})</td>
                      <td>${s.userName}</td>
                      <td>${s.checkInTime}</td>
                      <td>$${s.hourlyRate}/hr</td>
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
                <c:forEach var="c" items="${consumables}" varStatus="st">
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
                <button type="submit" class="btn btn-primary">Confirm Check Out</button>
              </form>
            </div>
          </c:if>
        </c:forEach>
      </main>
    </div>
  </div>
</body>
</html>
