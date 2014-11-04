<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="hasActionErrors()">
<div class="message message_error">
<h2><s:text name="message.title.ActionErrors" /></h2>	
	<ul>
	<s:iterator value="actionErrors">
		<li><s:property escape="false" /></li>
	</s:iterator>
	</ul>
</div>
</s:if>