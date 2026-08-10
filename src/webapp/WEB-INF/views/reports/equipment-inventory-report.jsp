<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% request.setAttribute("pageTitle", "Co-op Status Report"); request.setAttribute("activeNav", "statusReport"); %>
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
        <div class="content-header"><p>Co-op-wide equipment status and consumable stock levels (FR-06).</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="card">
          <h2 class="section-title">Equipment Status</h2>
          <table class="data-table">
            <thead><tr><th>Asset Tag</th><th>Name</th><th>Category</th><th>Status</th><th>Usage Hrs</th></tr></thead>
            <tbody>
              <c:forEach var="e" items="${equipmentList}">
                <tr>
                  <td>${e.assetTag}</td><td>${e.equipmentName}</td><td>${e.category}</td>
                  <td><span class="badge badge-${fn:toLowerCase(e.status)}">${e.status}</span></td>
                  <td>${e.totalUsageHours}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>

        <div class="card">
          <h2 class="section-title">Consumable Stock Levels</h2>
          <table class="data-table">
            <thead><tr><th>Material</th><th>Stock</th><th>Restock Level</th><th>Status</th></tr></thead>
            <tbody>
              <c:forEach var="c" items="${inventoryReport}">
                <tr>
                  <td>${c.materialName}</td><td>${c.currentStock} ${c.unit}</td><td>${c.restockLevel}</td>
                  <td><span class="badge badge-${fn:toLowerCase(c.stockStatus)}">${c.stockStatus}</span></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>

        <div class="card">
          <h2 class="section-title">Open Maintenance Alerts</h2>
          <c:choose>
            <c:when test="${empty openAlerts}"><p class="text-muted">None.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>Equipment</th><th>Component</th><th>Priority</th><th>Status</th></tr></thead>
                <tbody>
                  <c:forEach var="t" items="${openAlerts}">
                    <tr>
                      <td>${t.equipmentName} (${t.assetTag})</td><td>${t.componentName}</td>
                      <td><span class="badge badge-${fn:toLowerCase(t.priority)}">${t.priority}</span></td>
                      <td><span class="badge badge-${fn:toLowerCase(t.status)}">${t.status}</span></td>
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
