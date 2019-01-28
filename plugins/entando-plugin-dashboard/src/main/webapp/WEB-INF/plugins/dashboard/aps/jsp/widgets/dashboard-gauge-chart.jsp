<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="dashboard" uri="/dashboard-core"%>

<dashboard:dashboardGaugeChart var="dashboardGaugeChart" />
<article>
<c:choose>
	<c:when test="${not empty dashboardGaugeChart}">
	<h1><wp:i18n key="dashboard_DASHBOARDGAUGECHART_ID" />: <c:out value="${dashboardGaugeChart.id}" /></h1>
	<ul>
		<li>
			<wp:i18n key="dashboard_DASHBOARDGAUGECHART_WIDGETID" />: <c:out value="${dashboardGaugeChart.widgetId}" /><br />
		</li>
	</ul>
	</c:when>
	<c:otherwise>
	<div class="alert alert-error">
		<p><wp:i18n key="dashboard_DASHBOARDGAUGECHART_NOT_FOUND" /></p>
	</div>
	</c:otherwise>
</c:choose>
</article>