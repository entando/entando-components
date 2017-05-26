<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>

<wp:ifauthorized permission="superuser">
	<li class="list-group-item">
		<a href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />" tabindex="<wpsa:counter />">
            <span class="list-group-item-value">
                <s:text name="jpcrowdsourcing.admin.menu" />
            </span>
		</a>
	</li>
</wp:ifauthorized>
