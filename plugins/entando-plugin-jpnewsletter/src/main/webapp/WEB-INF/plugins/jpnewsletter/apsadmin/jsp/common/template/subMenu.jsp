<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="jpnewsletter_management" >
	<li class="openmenu"><a href="#" rel="fagiano_jpnewsletter" id="menu_jpnewsletter" class="subMenuToggler"><s:text name="jpnewsletter.admin.menu" /></a>
	<div id="fagiano_jpnewsletter" class="menuToggler"><div class="menuToggler-1"><div class="menuToggler-2">		
		<ul>
			<li><a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter" />" ><s:text name="jpnewsletter.admin.list"/></a></li>
			<li><a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter/Queue" />" ><s:text name="jpnewsletter.admin.queue"/></a></li>
			<li><a href="<s:url action="list" namespace="/do/jpnewsletter/Subscriber" />" ><s:text name="jpnewsletter.admin.subscribersList" /></a></li>
			<wp:ifauthorized permission="jpnewsletter_config" >
				<li><a href="<s:url action="edit" namespace="/do/jpnewsletter/Newsletter/Config" />" ><s:text name="jpnewsletter.admin.config" /></a></li>
			</wp:ifauthorized>
		</ul>
	</div></div></div>
	</li>
</wp:ifauthorized>