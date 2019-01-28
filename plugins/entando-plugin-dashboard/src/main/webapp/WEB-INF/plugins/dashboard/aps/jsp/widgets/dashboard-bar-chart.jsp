<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="dashboard" uri="/dashboard-core"%>

<dashboard:dashboardBarChart var="dashboardBarChart" />
<article>
<c:choose>
	<c:when test="${not empty dashboardBarChart}">
	<h1><wp:i18n key="dashboard_DASHBOARDBARCHART_ID" />: <c:out value="${dashboardBarChart.id}" /></h1>
	<ul>
		<li>
			<wp:i18n key="dashboard_DASHBOARDBARCHART_WIDGETID" />: <c:out value="${dashboardBarChart.widgetId}" /><br />
		</li>
	</ul>
	</c:when>
	<c:otherwise>
	<div class="alert alert-error">
		<p><wp:i18n key="dashboard_DASHBOARDBARCHART_NOT_FOUND" /></p>
	</div>
	</c:otherwise>
</c:choose>
</article>