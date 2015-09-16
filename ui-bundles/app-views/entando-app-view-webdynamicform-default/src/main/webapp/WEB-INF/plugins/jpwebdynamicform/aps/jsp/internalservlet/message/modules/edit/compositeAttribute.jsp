<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--Version for plugin jpwebdynamicform --%>
<s:set var="i18n_parent_attribute_name" value="#attribute.name" />
<s:set var="masterCompositeAttributeTracer" value="#attributeTracer" />
<s:set var="masterCompositeAttribute" value="#attribute" />
<s:iterator value="#attribute.attributes" var="attribute">
	<s:set var="attributeTracer" value="#masterCompositeAttributeTracer.getCompositeTracer(#masterCompositeAttribute)"></s:set>
	<s:set var="parentAttribute" value="#masterCompositeAttribute" />
	<%/*FIXME: i18n_attribute_name should be like below: */%>
		<%--
			<s:set var="i18n_attribute_name" value="%{'jpwebdynamicform_'+ #typeCodeKey +'_'+ #i18n_parent_attribute_name +'_'+ #attribute.name}" scope="request" />
		--%>
	<s:set var="i18n_attribute_name" value="%{'jpwebdynamicform_'+ #typeCodeKey +'_'+ #attribute.name}" scope="request" />
	<s:set var="attribute_id" value="%{'jpwebdynamicform_'+ #typeCodeKey +'_'+ #attributeTracer.getFormFieldName(#attribute)}" />
	<s:set var="fieldErrorClass" value="%{#fieldErrors.containsKey(#attributeTracer.getFormFieldName(#attribute)) ? ' error ' : ' '  }" />

	<%-- Attributes --%>
	<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" /> <s:property value="#fieldErrorClass" />">
		<label class="control-label" for="<s:property value="#attribute_id" />">
			<wp:i18n key="${i18n_attribute_name}" />
			<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/inc/include_attributeInfo.jsp" />
		</label>
		<div class="controls">
			<s:if test="#attribute.type == 'Boolean'">
				<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/booleanAttribute.jsp" />
			</s:if>
			<s:elseif test="#attribute.type == 'CheckBox'">
				<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/checkBoxAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Date'">
				<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/dateAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Enumerator'">
				<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/enumeratorAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'EnumeratorMap'">
				<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/enumeratorMapAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Longtext'">
				<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/longtextAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Number'">
				<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/numberAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Monotext' || #attribute.type == 'Text'">
				<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/monotextAttribute.jsp" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'ThreeState'">
				<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/threeStateAttribute.jsp" />
			</s:elseif>
			<s:else>
				<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/monotextAttribute.jsp" />
			</s:else>

			<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/inc/front_attributeInfo-help-block.jsp" />
		</div>
	</div>

</s:iterator>
<s:set var="attributeTracer" value="#masterCompositeAttributeTracer" />
<s:set var="attribute" value="#masterCompositeAttribute" />
<s:set var="attributeNameI18nKey" value="#attribute.name" />
<s:set var="parentAttribute" value="" />