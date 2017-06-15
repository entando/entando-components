<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
    <li role="presentation">
        <a role="menuitem"  href="<s:url namespace="/do/jpavatar/Config" action="management" />">
                <s:text name="jpavatar.title.avatar" />
        </a>
    </li>
</wp:ifauthorized>