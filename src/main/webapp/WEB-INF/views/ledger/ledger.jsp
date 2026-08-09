<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% request.setAttribute("pageTitle", "My Ledger"); request.setAttribute("activeNav", "ledger"); %>
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
        <div class="content-header"><p>Your monthly credit/debit report (FR-06). Debits must be settled within 10 days of month end.</p></div>
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="stat-grid">
          <div class="stat-card"><div class="label">Credits (${summary.reportMonth})</div><div class="value">$${summary.totalCredits}</div></div>
          <div class="stat-card"><div class="label">Debits</div><div class="value">$${summary.totalDebits}</div></div>
          <div class="stat-card"><div class="label">Payments</div><div class="value">$${summary.totalPayments}</div></div>
          <div class="stat-card"><div class="label">Amount to Settle</div><div class="value">$${summary.amountToSettle}</div></div>
        </div>

        <div class="card">
          <h2 class="section-title">Settle Debits / Recharge Account</h2>
          <form class="inline-form" action="${pageContext.request.contextPath}/controller" method="POST">
            <input type="hidden" name="action" value="settleAccount">
            <div class="field"><label>Amount</label><input type="number" step="0.01" min="0.01" name="amount" required></div>
            <div class="field"><button type="submit" class="btn btn-primary">Settle</button></div>
          </form>
        </div>

        <div class="card">
          <h2 class="section-title">Transaction History</h2>
          <c:choose>
            <c:when test="${empty transactions}"><p class="text-muted">No transactions yet.</p></c:when>
            <c:otherwise>
              <table class="data-table">
                <thead><tr><th>Date</th><th>Type</th><th>Activity</th><th>Amount</th><th>Description</th><th>Settled</th></tr></thead>
                <tbody>
                  <c:forEach var="t" items="${transactions}">
                    <tr>
                      <td>${t.transactionDate}</td>
                      <td><span class="badge badge-${fn:toLowerCase(t.transactionType)}">${t.transactionType}</span></td>
                      <td>${t.activityType}</td>
                      <td>$${t.amount}</td>
                      <td>${t.description}</td>
                      <td>${t.settled ? 'Yes' : 'No'}</td>
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
