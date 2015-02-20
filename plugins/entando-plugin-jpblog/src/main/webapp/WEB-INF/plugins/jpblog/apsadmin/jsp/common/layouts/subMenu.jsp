<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
 	<li class="margin-large-bottom"><span class="h5"><s:text name="jpblog.admin.menu" /></span>
        <ul class="nav nav-pills nav-stacked">
            <li><a href="<s:url action="edit" namespace="/do/jpblog/Config" />" ><s:text name="jpblog.config" /></a></li>
        </ul>
    </li>
</wp:ifauthorized>