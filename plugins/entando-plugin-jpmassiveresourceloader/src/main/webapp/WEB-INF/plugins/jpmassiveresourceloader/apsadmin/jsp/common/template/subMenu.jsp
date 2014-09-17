<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<wp:ifauthorized permission="manageResources">
<li class="openmenu"><a href="#" rel="fagiano_jpmassiveresourceloader" id="menu_jpmassiveresourceloader" class="subMenuToggler" ><s:text name="jpmassiveresourceloader.admin.menu" /></a>
	<div class="menuToggler" id="fagiano_jpmassiveresourceloader"><div class="menuToggler-1"><div class="menuToggler-2">
		<ul>
			<li><a href="<s:url action="new" namespace="/do/jpmassiveresourceloader/Resource/Massive" ><s:param name="resourceTypeCode" >Attach</s:param></s:url>" ><s:text name="jpmassiveresourceloader.admin.menu.attach" /></a></li>
			<li><a href="<s:url action="new" namespace="/do/jpmassiveresourceloader/Resource/Massive" ><s:param name="resourceTypeCode" >Image</s:param></s:url>" ><s:text name="jpmassiveresourceloader.admin.menu.image" /></a></li>
		</ul>
	</div></div></div>
</li>
</wp:ifauthorized>