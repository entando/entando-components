<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
    <li class="list-group-item">
        <a role="menuitem"  href="<s:url namespace="/do/jpavatar/Config" action="management" />">
            <span class="list-group-item-value">
                <s:text name="jpavatar.title.avatar" />
            </span>
                
        </a>
    </li>
</wp:ifauthorized>