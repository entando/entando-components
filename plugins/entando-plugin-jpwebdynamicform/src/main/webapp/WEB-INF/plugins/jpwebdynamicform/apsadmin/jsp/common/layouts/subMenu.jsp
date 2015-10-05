<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:ifauthorized permission="superuser" var="isSuperuser"/>
<c:choose>
	<c:when test="${isSuperuser}">
            <li class="margin-large-bottom"><span class="h5"><s:text name="jpwebdynamicform.menu.admin" /></span>
			<ul class="nav nav-pills nav-stacked">
				<li><a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Operator" />" ><s:text name="jpwebdynamicform.menu.messages"/></a></li>
				<li><a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Config" />" ><s:text name="jpwebdynamicform.menu.config" /></a></li>
				<li><a href="<s:url namespace="/do/Entity" action="initViewEntityTypes"><s:param name="entityManagerName">jpwebdynamicformMessageManager</s:param></s:url>" ><s:text name="jpwebdynamicform.menu.messageTypeAdmin" /></a></li>
			</ul>
	</li>
	</c:when>
	<c:otherwise>
		<wp:ifauthorized permission="jpwebdynamicform_manageForms">
			<li class="margin-large-bottom"><a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Operator" />" ><s:text name="jpwebdynamicform.menu.messages"/></a></li>
		</wp:ifauthorized>
	</c:otherwise>
</c:choose>
