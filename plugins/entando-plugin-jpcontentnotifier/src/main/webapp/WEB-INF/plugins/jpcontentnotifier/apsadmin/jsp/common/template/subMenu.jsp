<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
<li><a href="<s:url action="config" namespace="/do/jpcontentnotifier/NotifierConfig" />" id="jpcontentnotifier_menu" ><s:text name="jpcontentnotifier.admin.menu" /></a></li>
</wp:ifauthorized>