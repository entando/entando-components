<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
	<li>
		
		<a href="<s:url namespace="/do/jpsharedocs/Config" action="edit" />">
			<s:text name="jpsharedocs.admin.menu" />
		</a>
	</li>
</wp:ifauthorized>
