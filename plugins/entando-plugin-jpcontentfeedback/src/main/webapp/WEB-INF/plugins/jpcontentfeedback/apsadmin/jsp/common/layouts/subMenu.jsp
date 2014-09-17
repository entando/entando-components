<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="jpcontentfeedback_moderate">
    <li class="margin-large-bottom"><span class="h5"><s:text name="jpcontentfeedback.admin.menu" /></span>
        <ul class="nav nav-pills nav-stacked">
            <li><a href="<s:url action="list" namespace="/do/jpcontentfeedback/Comments" />" ><s:text name="jpcontentfeedback.admin.menu.contentFeedback" /></a></li>
                <wp:ifauthorized permission="superuser">
                <li><a href="<s:url action="edit" namespace="/do/jpcontentfeedback/Config" />" ><s:text name="jpcontentfeedback.admin.menu.contentFeedback.edit" /></a></li>
                </wp:ifauthorized>
        </ul>
    </li>
</wp:ifauthorized>