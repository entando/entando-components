<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<wp:ifauthorized permission="superuser">
    <li class="margin-large-bottom"><span class="h5"><s:text name="jpcrowdsourcing.admin.menu" /></span>
        <ul class="nav nav-pills nav-stacked">
        <li><a href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />" tabindex="<wpsa:counter />"><s:text name="jpcrowdsourcing.ideaInstance.list"/></a></li>
        <li><a href="<s:url action="list" namespace="/do/collaboration/Idea" />" tabindex="<wpsa:counter />"><s:text name="jpcrowdsourcing.idea.list"/></a></li>
        <li><a href="<s:url action="list" namespace="/do/collaboration/Idea/Comment" />" tabindex="<wpsa:counter />"><s:text name="jpcrowdsourcing.comment.list"/></a></li>
        <li><a href="<s:url action="entryConfig" namespace="/do/collaboration/Config" />" tabindex="<wpsa:counter />"><s:text name="jpcrowdsourcing.config"/></a></li>
    </ul>
</li>
</wp:ifauthorized>