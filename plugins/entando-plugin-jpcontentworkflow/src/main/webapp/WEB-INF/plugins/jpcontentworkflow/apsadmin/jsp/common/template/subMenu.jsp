<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<wp:ifauthorized permission="superuser">
	<li class="list-group-item"><a
		href="<s:url action="list" namespace="/do/jpcontentworkflow/Workflow" />">
			<span class="list-group-item-value"><s:text
					name="jpcontentworkflow.menu.workflow" /></span>
	</a></li>
</wp:ifauthorized>
