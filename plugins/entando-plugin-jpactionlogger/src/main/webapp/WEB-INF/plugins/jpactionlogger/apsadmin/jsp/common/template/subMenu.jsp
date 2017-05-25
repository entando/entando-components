<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<wp:ifauthorized permission="superuser">
	<li class="list-group-item"><a
		href="<s:url action="list" namespace="/do/jpactionlogger/ActionLogger" />"
		tabindex="<wpsa:counter />"> <span class="list-group-item-value">
				<s:text name="menu.jpactionlogger.actionLogger.list" />
		</span>
	</a></li>
</wp:ifauthorized>