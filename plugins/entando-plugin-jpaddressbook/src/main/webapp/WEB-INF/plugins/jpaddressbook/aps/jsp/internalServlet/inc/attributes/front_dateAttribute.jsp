<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="#attribute.failedDateString == null">
	<s:set name="dateAttributeValue" value="#attribute.getFormattedDate('dd/MM/yyyy')"></s:set>
</s:if>
<s:else>
	<s:set name="dateAttributeValue" value="#attribute.failedDateString"></s:set>
</s:else>
	<wpsf:textfield useTabindexAutoIncrement="true" id="%{attribute_id}" 
			name="%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#dateAttributeValue}"
		 	maxlength="254" cssClass="text"></wpsf:textfield>&#32;<span class="inlineNote">dd/MM/yyyy</span>