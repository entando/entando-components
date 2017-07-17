<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
    <li class="list-group-item">
        <a href="<s:url action="viewItem" namespace="/do/jpcontentscheduler/config" />">
            <span class="list-group-item-value"><s:text name="jpcontentscheduler.title.management" /></span>
        </a>
    </li>
</wp:ifauthorized>