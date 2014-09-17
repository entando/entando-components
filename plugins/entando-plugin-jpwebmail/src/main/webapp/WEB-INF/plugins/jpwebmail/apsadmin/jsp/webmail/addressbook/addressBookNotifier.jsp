<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="hasActionMessages() && !hasActionErrors()">
	<p><s:text name="Title.addressBookManager.message" /></p>
<ul>
	<s:iterator value="actionMessages">
		<li><s:property/></li>
	</s:iterator>
</ul>
<s:if test="!hasActionErrors()">
<s:form action="forwardIntroNewMail">
	<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('button.addressBookManager.back')}" cssClass="button" />
</s:form>
</s:if>
</s:if>