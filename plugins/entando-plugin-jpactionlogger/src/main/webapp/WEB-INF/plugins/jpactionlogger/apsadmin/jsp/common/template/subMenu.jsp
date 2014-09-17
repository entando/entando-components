<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
	<li><a href="<s:url action="list" namespace="/do/jpactionlogger/ActionLogger" />" ><s:text name="menu.jpactionlogger.actionLogger.list"/></a></li>
</wp:ifauthorized>