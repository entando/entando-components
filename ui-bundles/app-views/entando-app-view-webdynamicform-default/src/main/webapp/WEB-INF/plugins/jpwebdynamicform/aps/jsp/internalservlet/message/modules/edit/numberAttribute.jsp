<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:if test="#lang.default">
	<s:if test="#attribute.failedNumberString == null"><s:set var="numberAttributeValue" value="#attribute.value" /></s:if>
	<s:else><s:set var="numberAttributeValue" value="#attribute.failedNumberString" /></s:else>
	<wpsf:textfield 
		useTabindexAutoIncrement="true" 
		id="%{#attribute_id}" 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		value="%{#numberAttributeValue}"
		maxlength="254" />
</s:if>