<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="jpnewsletter_management" >
	<li class="list-group-item">
		<a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter" />">
			<span class="list-group-item-value"><s:text name="jpnewsletter.admin.menu" /></span>
		</a>
	</li>
</wp:ifauthorized>