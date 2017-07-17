<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="jpcontentfeedback_moderate">
    <li class="list-group-item">
        <a href="<s:url action="list" namespace="/do/jpcontentfeedback/Comments" />">
            <span class="list-group-item-value">
                <s:text name="jpcontentfeedback.admin.menu"/>
            </span>
        </a>
    </li>
</wp:ifauthorized>
