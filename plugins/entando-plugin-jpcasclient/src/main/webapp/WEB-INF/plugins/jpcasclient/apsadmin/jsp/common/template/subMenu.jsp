<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li class="openmenu"><a href="#" rel="fagiano_jpcasclient" id="fagiano_menu_jpcasclient" class="subMenuToggler" ><s:text name="jpcasclient.admin.menu" /></a>
		<div id="fagiano_jpcasclient" class="menuToggler">
			<div class="menuToggler-1">
				<div class="menuToggler-2">
					<ul>
						<li><a href="<s:url action="edit" namespace="/do/jpcasclient/config/" />" ><s:text name="jpcasclient.admin.menu.config" /></a></li>
					</ul>
				</div>
			</div>
		</div>
	</li>
</wp:ifauthorized>