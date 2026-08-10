<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Shared error/info banner. Commands set request-scope errorMessage/infoMessage. --%>
<c:if test="${not empty errorMessage}">
  <div class="alert alert-error">${errorMessage}</div>
</c:if>
<c:if test="${not empty infoMessage}">
  <div class="alert alert-info">${infoMessage}</div>
</c:if>
