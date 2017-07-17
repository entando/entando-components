<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<wp:ifauthorized permission="superuser">
    <li class="list-group-item">
        <a href="<s:url action="edit" namespace="/do/jpblog/Config" />" id="menu_jpblog" >
            <span class="list-group-item-value"><s:text name="jpblog.admin.menu" /></span>
        </a>
    </li>
</wp:ifauthorized>
