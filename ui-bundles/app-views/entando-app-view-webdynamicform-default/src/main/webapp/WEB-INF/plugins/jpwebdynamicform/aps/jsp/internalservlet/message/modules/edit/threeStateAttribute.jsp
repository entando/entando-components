<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:if test="#lang.default">
		<label class="radio inline" for="<s:property value="%{#attribute_id + '-none'}" />">
			<wpsf:radio 
				useTabindexAutoIncrement="true" 
				name="%{#attributeTracer.getFormFieldName(#attribute)}" 
				id="%{#attribute_id + '-none'}"
				value="" 
				checked="%{#attribute.booleanValue == null}" />
				<wp:i18n key="${i18n_attribute_name}_NONE" />
		</label>
		<label class="radio inline" for="<s:property value="%{#attribute_id + '-true'}" />">
			<wpsf:radio 
				useTabindexAutoIncrement="true" 
				name="%{#attributeTracer.getFormFieldName(#attribute)}" 
				id="%{#attribute_id + '-true'}"
				value="true" 
				checked="%{#attribute.booleanValue != null && #attribute.booleanValue == true}" />
				<wp:i18n key="${i18n_attribute_name}_YES" />
		</label>
		<label class="radio inline" for="<s:property value="%{#attribute_id + '-false'}" />">
			<wpsf:radio 
				useTabindexAutoIncrement="true" 
				name="%{#attributeTracer.getFormFieldName(#attribute)}" 
				id="%{#attribute_id + '-false'}"
				value="false" 
				checked="%{#attribute.booleanValue != null && #attribute.booleanValue == false}" />
				<wp:i18n key="${i18n_attribute_name}_NO" />
		</label>
</s:if>