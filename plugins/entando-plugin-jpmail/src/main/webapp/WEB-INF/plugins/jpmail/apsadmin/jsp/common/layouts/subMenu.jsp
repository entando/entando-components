<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
    <li class="list-group-item">
        <a href="<s:url action="editSmtp" namespace="/do/jpmail/MailConfig" />">
            <span class="list-group-item-value"><s:text name="jpmail.admin.menu" /></span>
        </a>
    </li>
</wp:ifauthorized>

