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
        <div class="content-header"><p>Accept and complete fabrication work orders for members and external clients.</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="card">
          <h2 class="section-title">Open Queue (all members + external clients)</h2>
          <c:choose>
            <c:when test="${empty openWorkOrders}"><p class="text-muted">Nothing here yet.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>#</th><th>Requester</th><th>Description</th><th>Priority</th><th>Status</th><th>Credit</th><th></th></tr></thead>
                <tbody>
                  <c:forEach var="w" items="${openWorkOrders}">
                    <tr>
                      <td>${w.workOrderId}</td>
                      <td>${w.requesterLabel}</td>
                      <td>${w.description}</td>
                      <td><span class="badge badge-${fn:toLowerCase(w.priority)}">${w.priority}</span></td>
                      <td><span class="badge badge-${fn:toLowerCase(w.status)}">${w.status}</span></td>
                      <td>$${w.creditEarned}</td>
                      <td>
                        <c:if test="${w.status == 'SUBMITTED' || w.status == 'QUOTED'}">
                          <form style="display:inline" action="${pageContext.request.contextPath}/controller" method="POST">
                            <input type="hidden" name="action" value="acceptWorkOrder">
                            <input type="hidden" name="workOrderId" value="${w.workOrderId}">
                            <button type="submit" class="btn btn-secondary btn-small">Accept</button>
                          </form>
                        </c:if>
                        <c:if test="${w.status == 'ACCEPTED'}">
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

        <div class="card">
          <h2 class="section-title">My Work Orders (accepted by me)</h2>
          <c:choose>
            <c:when test="${empty myWorkOrders}"><p class="text-muted">Nothing here yet.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>#</th><th>Requester</th><th>Description</th><th>Priority</th><th>Status</th><th>Credit</th><th></th></tr></thead>
                <tbody>
                  <c:forEach var="w" items="${myWorkOrders}">
                    <tr>
                      <td>${w.workOrderId}</td>
                      <td>${w.requesterLabel}</td>
                      <td>${w.description}</td>
                      <td><span class="badge badge-${fn:toLowerCase(w.priority)}">${w.priority}</span></td>
                      <td><span class="badge badge-${fn:toLowerCase(w.status)}">${w.status}</span></td>
                      <td>$${w.creditEarned}</td>
                      <td>
                        <c:if test="${w.status == 'ACCEPTED'}">
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

        <p class="hint">Members request work through their Shop-Tech directly; external clients submit jobs via the public form: <a href="${pageContext.request.contextPath}/external-request.jsp">/external-request.jsp</a> (Adapter pattern: ExternalJobRequestAdapter).</p>
      </main>
    </div>
  </div>
</body>
</html>
