<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
<li class="margin-large-bottom"><span class="h5"><s:text name="jpavatar.title.avatar" /></span>
    <ul class="nav nav-pills nav-stacked">
        <li><a href="<s:url namespace="/do/jpavatar/Config" action="edit" />" ><s:text name="jpavatar.admin.menu.config" /></a></li> 
    </ul>
</li>
</wp:ifauthorized>