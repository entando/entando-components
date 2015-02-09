<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="jpnewsletter_management" >
<li class="margin-large-bottom"><span class="h5"><s:text name="jpnewsletter.admin.menu" /></span>
	<ul class="nav nav-pills nav-stacked">
		<li><a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter" />"><s:text name="jpnewsletter.admin.list"/></a></li>
		<li><a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter/Queue" />"><s:text name="jpnewsletter.admin.queue"/></a></li>
		<li><a href="<s:url action="list" namespace="/do/jpnewsletter/Subscriber" />" ><s:text name="jpnewsletter.admin.subscribersList" /></a></li>
		<wp:ifauthorized permission="jpnewsletter_config" >
		<li><a href="<s:url action="edit" namespace="/do/jpnewsletter/Newsletter/Config" />" ><s:text name="jpnewsletter.admin.config" /></a></li>
		</wp:ifauthorized>
	</ul>
</li>
</wp:ifauthorized>