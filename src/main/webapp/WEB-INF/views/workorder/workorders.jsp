<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% request.setAttribute("pageTitle", "Work Orders"); request.setAttribute("activeNav", "workorders"); %>
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
        <div class="content-header"><p>Submit, accept, and complete fabrication work orders.</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <c:if test="${!sessionScope.currentUser.shopTech && !sessionScope.currentUser.admin}">
          <div class="card">
            <h2 class="section-title">Submit a Work Order</h2>
            <p class="hint">Have a Shop-Tech fabricate something on your behalf.</p>
            <form class="form-grid" action="${pageContext.request.contextPath}/controller" method="POST">
              <input type="hidden" name="action" value="submitWorkOrder">
              <div class="field" style="grid-column: 1 / -1;"><label>Description</label>
                <textarea name="description" rows="3" required></textarea>
              </div>
              <div class="field"><label>Priority</label>
                <select name="priority"><option value="STANDARD">Standard</option><option value="RUSH">Rush</option></select>
              </div>
              <div class="field"><label>Est. Equipment Cost</label><input type="number" step="0.01" min="0" name="estimatedEquipmentCost" value="0"></div>
              <div class="field"><label>Est. Material Cost</label><input type="number" step="0.01" min="0" name="estimatedMaterialCost" value="0"></div>
              <div class="field"><label>Est. Labour Cost</label><input type="number" step="0.01" min="0" name="estimatedLabourCost" value="0"></div>
              <div class="field"><button type="submit" class="btn btn-primary">Submit Work Order</button></div>
            </form>
          </div>
        </c:if>

        <div class="card">
          <h2 class="section-title">${(sessionScope.currentUser.shopTech || sessionScope.currentUser.admin) ? 'Open Queue (all members + external clients)' : 'My Work Orders'}</h2>
          <c:set var="list" value="${(sessionScope.currentUser.shopTech || sessionScope.currentUser.admin) ? openWorkOrders : myWorkOrders}"/>
          <c:choose>
            <c:when test="${empty list}"><p class="text-muted">Nothing here yet.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>#</th><th>Requester</th><th>Description</th><th>Priority</th><th>Status</th><th>Credit</th><th></th></tr></thead>
                <tbody>
                  <c:forEach var="w" items="${list}">
                    <tr>
                      <td>${w.workOrderId}</td>
                      <td>${w.requesterLabel}</td>
                      <td>${w.description}</td>
                      <td><span class="badge badge-${fn:toLowerCase(w.priority)}">${w.priority}</span></td>
                      <td><span class="badge badge-${fn:toLowerCase(w.status)}">${w.status}</span></td>
                      <td>$${w.creditEarned}</td>
                      <td>
                        <c:if test="${(sessionScope.currentUser.shopTech || sessionScope.currentUser.admin) && (w.status == 'SUBMITTED' || w.status == 'QUOTED')}">
                          <form style="display:inline" action="${pageContext.request.contextPath}/controller" method="POST">
                            <input type="hidden" name="action" value="acceptWorkOrder">
                            <input type="hidden" name="workOrderId" value="${w.workOrderId}">
                            <button type="submit" class="btn btn-secondary btn-small">Accept</button>
                          </form>
                        </c:if>
                        <c:if test="${(sessionScope.currentUser.shopTech || sessionScope.currentUser.admin) && w.status == 'ACCEPTED'}">
                          <form style="display:inline" action="${pageContext.request.contextPath}/controller" method="POST">
                            <input type="hidden" name="action" value="completeWorkOrder">
                            <input type="hidden" name="workOrderId" value="${w.workOrderId}">
                            <button type="submit" class="btn btn-primary btn-small">Complete</button>
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

        <c:if test="${sessionScope.currentUser.shopTech || sessionScope.currentUser.admin}">
          <p class="hint">External clients submit jobs via the public form: <a href="${pageContext.request.contextPath}/external-request.jsp">/external-request.jsp</a> (Adapter pattern: ExternalJobRequestAdapter).</p>
        </c:if>
      </main>
    </div>
  </div>
</body>
</html>
