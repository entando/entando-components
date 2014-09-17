<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%--
YES = Si
NO = No
 --%>

<ul>
	<li><wpsf:radio useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="true_%{attribute_id}" value="true" checked="%{#attribute.value == true}" cssClass="radio" /><label for="true_<s:property value="%{attribute_id}" />" class="normal"><wp:i18n key="YES" /></label></li>
	<li><wpsf:radio useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="false_%{attribute_id}" value="false" checked="%{#attribute.value == false}" cssClass="radio"  /><label for="false_<s:property value="%{attribute_id}" />" class="normal"><wp:i18n key="NO" /></label></li>
</ul>