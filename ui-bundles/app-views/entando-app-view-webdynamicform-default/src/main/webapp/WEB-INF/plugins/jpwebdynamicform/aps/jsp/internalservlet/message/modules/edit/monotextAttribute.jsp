<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:if test="#lang.default">
	<wpsf:textfield 
		useTabindexAutoIncrement="true" 
		id="%{#attribute_id}" 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		value="%{#attribute.getTextForLang(#lang.code)}"
		maxlength="254" />
</s:if>