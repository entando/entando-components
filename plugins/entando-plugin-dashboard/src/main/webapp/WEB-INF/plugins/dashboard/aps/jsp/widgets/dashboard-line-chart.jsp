<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="dashboard" uri="/dashboard-core"%>

<dashboard:dashboardLineChart var="dashboardLineChart" />
<article>
<c:choose>
	<c:when test="${not empty dashboardLineChart}">
	<h1><wp:i18n key="dashboard_DASHBOARDLINECHART_ID" />: <c:out value="${dashboardLineChart.id}" /></h1>
	<ul>
		<li>
			<wp:i18n key="dashboard_DASHBOARDLINECHART_WIDGETID" />: <c:out value="${dashboardLineChart.widgetId}" /><br />
		</li>
	</ul>
	</c:when>
	<c:otherwise>
	<div class="alert alert-error">
		<p><wp:i18n key="dashboard_DASHBOARDLINECHART_NOT_FOUND" /></p>
	</div>
	</c:otherwise>
</c:choose>
</article>