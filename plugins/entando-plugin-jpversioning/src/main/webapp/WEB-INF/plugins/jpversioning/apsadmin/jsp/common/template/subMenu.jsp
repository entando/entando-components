<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li class="margin-large-bottom"><span class="h5"><s:text name="jpversioning.admin.menu" /></span>
		<ul class="nav nav-pills nav-stacked">
				<li><a href="<s:url action="list" namespace="/do/jpversioning/Content/Versioning" />" id="menu_versioning" ><s:text name="jpversioning.menu.contentList" /></a></li>
				<li><a href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" >Image</s:param></s:url>" ><s:text name="jpversioning.menu.images" /></a></li>
				<li><a href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" >Attach</s:param></s:url>" ><s:text name="jpversioning.menu.attaches" /></a></li>
		</ul>
	</li>
</wp:ifauthorized>