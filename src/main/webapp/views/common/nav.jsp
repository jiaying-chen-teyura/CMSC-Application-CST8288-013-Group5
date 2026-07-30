<%--
  Sidebar navigation - included by every page under views/.
  Links are context-relative (start with ${pageContext.request.contextPath})
  so they resolve correctly regardless of which views/<section>/ folder
  the including page lives in.

  Section visibility now depends on the logged-in user's role
  (session attribute "userType", set by LoginServlet), matching the
  use-case diagram: every role gets the base User sections; Shop-Tech
  additionally gets Equipment Management/Maintenance/Work Orders;
  Trainer additionally gets Training.
--%>
<%
  String navUserType = (String) session.getAttribute("userType");
  boolean navIsShopTech = "SHOP_TECH".equals(navUserType);
  boolean navIsTrainer = "TRAINER".equals(navUserType);
%>
<aside class="sidebar">
  <div class="sidebar-brand">
    <span class="mark"><span class="dot"></span>CMSC</span>
    <span class="subtitle">Maker Space Co-op</span>
  </div>

  <ul class="nav-list">
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/views/dashboard/dashboard.jsp"><span class="indicator"></span>Dashboard</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/BookEquipmentServlet"><span class="indicator"></span>Book Equipment</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/ConsumableServlet"><span class="indicator"></span>Consumables</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/BillingServlet"><span class="indicator"></span>Billing</a>
    </li>

    <% if (navIsTrainer) { %>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/TrainingServlet"><span class="indicator"></span>Training</a>
    </li>
    <% } %>

    <% if (navIsShopTech) { %>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/EquipmentManagementServlet"><span class="indicator"></span>Equipment Management</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/MaintenanceServlet"><span class="indicator"></span>Maintenance</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/WorkOrderServlet"><span class="indicator"></span>Work Orders</a>
    </li>
    <% } %>
  </ul>

  <div class="sidebar-foot">
    CMSC &middot; CST8288
  </div>
</aside>
