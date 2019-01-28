<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="dashboard" uri="/dashboard-core"%>

<dashboard:dashboardDonutChart var="dashboardDonutChart" />
<article>
<c:choose>
	<c:when test="${not empty dashboardDonutChart}">
	<h1><wp:i18n key="dashboard_DASHBOARDDONUTCHART_ID" />: <c:out value="${dashboardDonutChart.id}" /></h1>
	<ul>
		<li>
			<wp:i18n key="dashboard_DASHBOARDDONUTCHART_WIDGETID" />: <c:out value="${dashboardDonutChart.widgetId}" /><br />
		</li>
	</ul>
	</c:when>
	<c:otherwise>
	<div class="alert alert-error">
		<p><wp:i18n key="dashboard_DASHBOARDDONUTCHART_NOT_FOUND" /></p>
	</div>
	</c:otherwise>
</c:choose>
</article>
