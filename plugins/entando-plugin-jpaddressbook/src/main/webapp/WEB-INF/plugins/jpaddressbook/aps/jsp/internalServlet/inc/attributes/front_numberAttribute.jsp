<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="#attribute.failedNumberString == null">
	<s:set name="numberAttributeValue" value="#attribute.value"></s:set>
</s:if>
<s:else>
	<s:set name="numberAttributeValue" value="#attribute.failedNumberString"></s:set>
</s:else>
<wpsf:textfield useTabindexAutoIncrement="true" id="%{attribute_id}" 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#numberAttributeValue}"
		maxlength="254" cssClass="text" /> 