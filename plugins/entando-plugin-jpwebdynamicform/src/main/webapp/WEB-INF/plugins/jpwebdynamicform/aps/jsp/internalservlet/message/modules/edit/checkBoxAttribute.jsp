<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set name="checkedValue" value="%{#attribute.booleanValue != null && #attribute.booleanValue ==true}" />
<s:if test="#lang.default">
	<wpsf:checkbox 
		useTabindexAutoIncrement="true" 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		id="%{#attribute_id}" 
		value="#checkedValue"/>&#32;
</s:if>