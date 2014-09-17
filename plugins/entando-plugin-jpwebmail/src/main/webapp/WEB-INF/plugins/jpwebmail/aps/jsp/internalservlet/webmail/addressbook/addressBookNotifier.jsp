<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="hasActionMessages() && !hasActionErrors()">
	<p><wp:i18n key="ERROR" /></p>
<ul>
	<s:iterator value="actionMessages">
		<li><s:property escape="false" /></li>
	</s:iterator>
</ul>
<s:if test="!hasActionErrors()">
	<form action="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/forwardIntroNewMail.action" />" method="post" >
	<s:set value="backLabel"><wp:i18n key="jpwebmail_BACK" /></s:set>
	<wpsf:submit useTabindexAutoIncrement="true" value="%#backLabel}" cssClass="button" />
</form>
</s:if>
</s:if>