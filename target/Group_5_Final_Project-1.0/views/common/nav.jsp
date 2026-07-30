<%--
  Sidebar navigation — included by every page under views/.
  Links are context-relative (start with ${pageContext.request.contextPath})
  so they resolve correctly regardless of which views/<section>/ folder
  the including page lives in.

  "active" state is set client-side by resources/js/main.js based on
  the current file name, so this file stays static for now (no
  Servlet/session logic yet).
--%>
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
      <a class="nav-link" href="${pageContext.request.contextPath}/views/equipment/equipment.jsp"><span class="indicator"></span>Equipment</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/views/consumable/consumables.jsp"><span class="indicator"></span>Consumables</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/views/training/training.jsp"><span class="indicator"></span>Training</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/views/maintenance/maintenance.jsp"><span class="indicator"></span>Maintenance</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/views/workorder/workorders.jsp"><span class="indicator"></span>Work Orders</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="${pageContext.request.contextPath}/views/billing/billing.jsp"><span class="indicator"></span>Billing</a>
    </li>
  </ul>

  <div class="sidebar-foot">
    CMSC &middot; CST8288
  </div>
</aside>
