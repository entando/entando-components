<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
<li role="presentation"><a role="menuitem" href="<s:url action="intro" namespace="/do/jpcomponentinstaller/Installer" />" ><s:text name="jpcomponentinstaller.admin.intro" /></a></li>
</wp:ifauthorized>