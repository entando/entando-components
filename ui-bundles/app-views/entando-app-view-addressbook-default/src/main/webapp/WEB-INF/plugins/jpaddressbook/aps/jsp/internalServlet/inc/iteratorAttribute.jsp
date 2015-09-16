<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="#attribute.type == 'Monotext'">
	<p> 
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monotextAttribute.jsp" />
	</p>
</s:if>

<s:elseif test="#attribute.type == 'Text'">
	<p> 
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monotextAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Longtext'">
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monotextAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Hypertext'">
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_hypertextAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Boolean'">
	<p>
			<span><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></span>:<br />
	</p>
	<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_booleanAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'ThreeState'">
	<p>
		<span><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></span>:<br />
	</p>
	<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_threeStateAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'Number'">
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_numberAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Date'">
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_dateAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Enumerator'">
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_enumeratorAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'EnumeratorMap'">
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_enumeratorMapAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'CheckBox'">
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label>:
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_checkBoxAttribute.jsp" />
	</p>
</s:elseif>	

<s:elseif test="#attribute.type == 'Monolist'">
	<p>
		<span><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></span>:
	</p>
	<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monolistAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'List'">
	<p>
		<span><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></span>:<br />			
	</p>
	<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monolistAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'Composite'">
	<p>
		<span><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></span>:<br />
	</p>
	
	<div>
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_compositeAttribute.jsp" />
	</div>

</s:elseif>