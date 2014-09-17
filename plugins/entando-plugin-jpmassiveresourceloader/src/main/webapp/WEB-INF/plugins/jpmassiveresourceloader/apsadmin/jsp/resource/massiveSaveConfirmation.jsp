<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1><a href="<s:url action="list" namespace="/do/Resource"><s:param name="resourceTypeCode"><s:property value="resourceTypeCode" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.resourceManagement" />"><s:text name="title.resourceManagement" /></a></h1>
<div id="main">
	<h2 class="margin-more-bottom"><s:text name="jpmassiveresourceloader.title.resourceManagement.massiveResourceNew" />&nbsp;<s:text name="jpmassiveresourceloader.resourceType.%{resourceTypeCode}" /></h2>
	<s:if test="hasActionMessages()">
		<div class="message message_confirm">
			<h3><s:text name="messages.confirm" /></h3>
			<ul>
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasActionErrors()">
		<div class="message message_error">
			<h3><s:text name="message.title.ActionErrors" /></h3>
			<ul>
				<s:iterator value="ActionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
</div>