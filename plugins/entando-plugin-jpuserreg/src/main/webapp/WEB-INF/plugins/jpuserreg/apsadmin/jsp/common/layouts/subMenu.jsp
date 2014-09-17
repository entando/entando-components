<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
    <li class="margin-large-bottom"><span class="h5"><s:text name="jpuserreg.menu.userregAdmin" /></span>
        <ul class="nav nav-pills nav-stacked">
            <li><a href="<s:url namespace="/do/jpuserreg/Config" action="edit" />" ><s:text name="jpuserreg.menu.config" /></a></li>
        </ul>
</li>
</wp:ifauthorized>