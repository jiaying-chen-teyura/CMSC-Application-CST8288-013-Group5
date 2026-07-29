<%--
  ============================================================
  REFERENCE FILE — not included by any page yet.
  ============================================================

  This shows the shared-shell pattern we designed: a Servlet
  will eventually set a "bodyPage" attribute and forward here,
  and this file assembles the full page around that content.

  Example of how a future Servlet would use this file:

    request.setAttribute("bodyPage", "/views/equipment/equipment-body.jsp");
    request.setAttribute("pageTitle", "Equipment");
    request.getRequestDispatcher("/views/common/layout.jsp").forward(request, response);

  For now, each views/<section>/<section>.jsp file is a complete,
  self-contained page (it includes head.jsp/nav.jsp/header.jsp
  directly) so it can be opened and previewed without a Servlet.
  Once Servlets exist, those files can be simplified down to just
  their inner content and routed through this layout instead.
  ============================================================
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/views/common/head.jsp" %>
</head>
<body>
  <div class="app-shell">
    <%@ include file="/views/common/nav.jsp" %>

    <div class="app-main">
      <%@ include file="/views/common/header.jsp" %>

      <main class="content">
        <jsp:include page="${bodyPage}" />
      </main>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
