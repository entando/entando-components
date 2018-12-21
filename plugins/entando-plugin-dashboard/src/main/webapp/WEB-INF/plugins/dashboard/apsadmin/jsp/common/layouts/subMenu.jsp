<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li><a href="<s:url namespace="/do/dashboard/DashboardConfig" action="list" />" ><s:text name="dashboard.title.dashboardConfigManagement" /></a></li>
</wp:ifauthorized>
