<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
    <li class="margin-large-bottom"><span class="h5"><s:text name="jpuserprofile.menu.profileAdmin" /></span>
        <ul class="nav nav-pills nav-stacked">
            <li><a href="<s:url namespace="/do/Entity" action="initViewEntityTypes"><s:param name="entityManagerName">UserProfileManager</s:param></s:url>" ><s:text name="jpuserprofile.menu.profileTypeAdmin" /></a></li>
        </ul>
    </li>
</wp:ifauthorized>