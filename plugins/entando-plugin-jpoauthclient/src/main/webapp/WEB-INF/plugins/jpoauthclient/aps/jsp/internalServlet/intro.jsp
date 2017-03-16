<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="hasFieldErrors()">
<h3><wp:i18n key="jpoauthclient_FIELD_ERROS" /></h3>
<ul>
	<s:iterator value="fieldErrors">
		<s:iterator value="value">
			<li><s:property escapeHtml="false" /></li>
		</s:iterator>
	</s:iterator>
</ul>
</s:if>
<s:if test="hasActionErrors()">
<div class="message message_error">
<h3><wp:i18n key="jpoauthclient_ACTION_ERROS" /></h3>
<ul>
	<s:iterator value="actionErrors">
		<li><s:property escapeHtml="false" /></li>
	</s:iterator>
</ul>
</div>
</s:if>

<form action="<wp:action path="/ExtStr2/do/jpoauthclient/Front/call.action" />" method="post" >
	
	<wpsf:hidden name="checkCall" value="'true'" />
	<fieldset>
		<legend><wp:i18n key="jpoauthclient_RESOURCE_URI" /></legend>
		<wp:i18n key="jpoauthclient_LANG_CODE" />
		<wpsf:textfield name="langCode" />
		<br />
		<wp:i18n key="jpoauthclient_NAMESPACE" />
		<wpsf:textfield name="namespace" />
		<br />
		<wp:i18n key="jpoauthclient_RESOURCE_NAME" />
		<wpsf:textfield name="resourceName" />
		<br />
		<wp:i18n key="jpoauthclient_QUERY_STRING" />
		<wpsf:textfield name="queryString" />
	</fieldset>
	<br />
	<wp:i18n key="jpoauthclient_RESPONSE_CONTENT_TYPE" />
	<wpsf:select list="allowedMediaTypes" name="responseContentType" />
	<br /><br />
	<wp:i18n key="jpoauthclient_REST_METHOD" />
	<wpsf:select list="allowedMethods" name="method" />
	<br /><br />
	<wp:i18n key="jpoauthclient_REQUEST_CONTENT_TYPE" />
	<wpsf:select list="allowedMediaTypes" name="requestContentType" />
	<br /><br />
	<wp:i18n key="jpoauthclient_REQUEST_BODY" /><br />
	<wpsf:textarea name="requestBody" cols="100" rows="10" />
	<br /><br />
	<wpsf:checkbox name="plainTextResult" /><wp:i18n key="jpoauthclient_PLAIN_TEXT_RESULT" />
	<br /><br />
	
	<s:set var="submitLabel"><wp:i18n key="jpoauthclient_SUBMIT" /></s:set>
	
	<p><wpsf:submit useTabindexAutoIncrement="true" value="%{#submitLabel}" cssClass="button" /></p> 

</form>
	