<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
	<li>
		<%--
		<li><a href="<s:url action="intro" namespace="/do/jpwebmail/WebMail" />" ><s:text name="jpwebmail.admin.submenu.webmail" /></a></li>
		--%>
		<a href="<s:url namespace="/do/jpwebmail/Config" action="edit" />">
			<s:text name="jpwebmail.admin.menu" />
		</a>
	</li>
</wp:ifauthorized>
