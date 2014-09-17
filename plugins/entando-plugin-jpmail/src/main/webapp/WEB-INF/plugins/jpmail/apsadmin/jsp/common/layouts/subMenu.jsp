<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
    <li class="margin-large-bottom"><span class="h5"><s:text name="jpmail.admin.menu" /></span>
    <ul class="nav nav-pills nav-stacked">
        <li><a href="<s:url action="editSmtp" namespace="/do/jpmail/MailConfig" />" ><s:text name="jpmail.admin.menu.smtp" /></a></li>
	<li><a href="<s:url action="viewSenders" namespace="/do/jpmail/MailConfig" />" ><s:text name="jpmail.admin.menu.senders" /></a></li>
    </ul>
    </li>
</wp:ifauthorized>