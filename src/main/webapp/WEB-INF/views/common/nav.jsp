<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
  Sidebar navigation — included by every page under WEB-INF/views/.
  Links now go through the Front Controller (?action=...) instead of
  static JSP paths, and are filtered by the logged-in user's role so
  each actor only sees the use cases that apply to them (per the Use
  Case Diagram: User / Trainer / Shop-Tech / External Client).
  "active" is set server-side by comparing to the "activeNav" request
  attribute each page sets before including this file.
--%>
<aside class="sidebar">
  <div class="sidebar-brand">
    <span class="mark"><span class="dot"></span>CMSC</span>
    <span class="subtitle">Maker Space Co-op</span>
  </div>

  <ul class="nav-list">
    <li class="nav-item">
      <a class="nav-link ${activeNav == 'dashboard' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/controller?action=dashboard"><span class="indicator"></span>Dashboard</a>
    </li>
    <li class="nav-item">
      <a class="nav-link ${activeNav == 'equipmentAvailability' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/controller?action=viewEquipmentAvailability"><span class="indicator"></span>Book Equipment</a>
    </li>
    <li class="nav-item">
      <a class="nav-link ${activeNav == 'sessions' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/controller?action=viewActiveSessions"><span class="indicator"></span>Check In / Out</a>
    </li>
    <li class="nav-item">
      <a class="nav-link ${activeNav == 'consumables' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/controller?action=viewInventory"><span class="indicator"></span>Consumables</a>
    </li>
    <li class="nav-item">
      <a class="nav-link ${activeNav == 'workorders' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/controller?action=viewWorkOrders"><span class="indicator"></span>Work Orders</a>
    </li>
    <li class="nav-item">
      <a class="nav-link ${activeNav == 'ledger' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/controller?action=viewLedger"><span class="indicator"></span>My Ledger</a>
    </li>

    <c:if test="${sessionScope.currentUser.trainer}">
      <li class="nav-item">
        <a class="nav-link ${activeNav == 'training' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/controller?action=viewTrainerReport"><span class="indicator"></span>Training</a>
      </li>
    </c:if>

    <c:if test="${sessionScope.currentUser.shopTech || sessionScope.currentUser.admin}">
      <li class="nav-item">
        <a class="nav-link ${activeNav == 'equipmentManage' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/controller?action=viewEquipment"><span class="indicator"></span>Manage Equipment</a>
      </li>
      <li class="nav-item">
        <a class="nav-link ${activeNav == 'maintenance' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/controller?action=viewMaintenanceAlerts"><span class="indicator"></span>Maintenance</a>
      </li>
      <li class="nav-item">
        <a class="nav-link ${activeNav == 'shoptechReport' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/controller?action=viewShopTechReport"><span class="indicator"></span>My Shop-Tech Report</a>
      </li>
      <li class="nav-item">
        <a class="nav-link ${activeNav == 'statusReport' ? 'active' : ''}"
           href="${pageContext.request.contextPath}/controller?action=viewEquipmentInventoryStatusReport"><span class="indicator"></span>Co-op Status Report</a>
      </li>
    </c:if>
  </ul>

  <div class="sidebar-foot">
    CMSC &middot; CST8288
  </div>
</aside>
