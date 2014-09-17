<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li><a href="<s:url action="intro" namespace="/do/jpcontentrefs/Category" />"><s:text name="jpcontentrefs.admin.menu" /></a></li>
</wp:ifauthorized>