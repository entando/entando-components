<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
    <li class="list-group-item">
        <a href="<s:url namespace="/do/TagcloudAdmin" action="configSystemParams" />" >
            <span class="list-group-item-value"><s:text name="jpcmstagcloud.name"/></span>
        </a>
    </li>
</wp:ifauthorized>