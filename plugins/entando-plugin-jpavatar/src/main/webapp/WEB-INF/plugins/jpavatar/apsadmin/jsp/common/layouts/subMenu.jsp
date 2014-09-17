<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<li class="margin-large-bottom"><span class="h5"><s:text name="jpavatar.title.avatar" /></span>
        <ul class="nav nav-pills nav-stacked">
            <li><a href="<s:url action="edit" namespace="/do/jpavatar/Avatar" />" ><s:text name="jpavatar.admin.menu.avatar" /></a></li>
                <wp:ifauthorized permission="superuser">
                <li><a href="<s:url namespace="/do/jpavatar/Config" action="edit" />" ><s:text name="jpavatar.admin.menu.config" /></a></li> 
                </wp:ifauthorized>
        </ul>
</li>