<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
	<li class="list-group-item">
		<a href="<s:url action="edit" namespace="/do/jpmyportalplus/Config" />">
            <span class="list-group-item-value">
                <s:text name="jpmyportalplus.menu.config" />
            </span>
		</a>
	</li>
</wp:ifauthorized>
