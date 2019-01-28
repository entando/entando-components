<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="dashboard" uri="/dashboard-core"%>

<dashboard:dashboardPieChart var="dashboardPieChart" />
<article>
<c:choose>
	<c:when test="${not empty dashboardPieChart}">
	<h1><wp:i18n key="dashboard_DASHBOARDPIECHART_ID" />: <c:out value="${dashboardPieChart.id}" /></h1>
	<ul>
		<li>
			<wp:i18n key="dashboard_DASHBOARDPIECHART_WIDGETID" />: <c:out value="${dashboardPieChart.widgetId}" /><br />
		</li>
	</ul>
	</c:when>
	<c:otherwise>
	<div class="alert alert-error">
		<p><wp:i18n key="dashboard_DASHBOARDPIECHART_NOT_FOUND" /></p>
	</div>
	</c:otherwise>
</c:choose>
</article>