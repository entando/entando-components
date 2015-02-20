<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
	<li role="presentation"><a role="menuitem" href="<s:url action="controlPanel" namespace="/do/Mailgun" />" ><s:text name="jpmailgun.name" /></a></li>
</wp:ifauthorized>
