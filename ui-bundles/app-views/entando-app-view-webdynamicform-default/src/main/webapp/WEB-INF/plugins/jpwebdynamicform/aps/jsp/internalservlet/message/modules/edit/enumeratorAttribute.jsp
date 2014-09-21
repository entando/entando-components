<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:if test="#lang.default">
	<wp:i18n key="jpwebdynamicform_SELECT" var="labelSelect" />
	<wpsf:select 
		useTabindexAutoIncrement="true" 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" 
		id="%{#attribute_id}"
		headerValue="%{#attr.labelSelect}" 
		headerKey="" 
		list="#attribute.items" 
		value="%{#attribute.getText()}" />
</s:if>