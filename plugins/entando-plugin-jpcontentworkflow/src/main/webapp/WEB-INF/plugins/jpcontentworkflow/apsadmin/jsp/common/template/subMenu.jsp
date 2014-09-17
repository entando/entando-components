<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
<li class="margin-large-bottom">
	<span class="h5">
		<s:text name="jpcontentworkflow.menu.workflowAdmin" />
	</span>
		<ul class="nav nav-pills nav-stacked">
			<li><a href="<s:url action="list" namespace="/do/jpcontentworkflow/Workflow" />" ><s:text name="jpcontentworkflow.menu.workflow" /></a></li>
			<li><a href="<s:url action="config" namespace="/do/jpcontentworkflow/Notifier" />" ><s:text name="jpcontentworkflow.menu.notifier" /></a></li>
		</ul>
</li>
</wp:ifauthorized>
