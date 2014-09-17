<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="#attribute.type == 'Monotext'">
	<%-- ############# ATTRIBUTO TESTO MONOLINGUA ############# --%>
	<p> 
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monotextAttribute.jsp" />
	</p>
</s:if>

<s:elseif test="#attribute.type == 'Text'">
	<%-- ############# ATTRIBUTO TESTO SEMPLICE MULTILINGUA ############# --%>
	<p> 
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monotextAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Longtext'">
	<%-- ############# ATTRIBUTO TESTOLUNGO ############# --%>
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monotextAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Hypertext'">
	<%-- ############# ATTRIBUTO TESTOLUNGO ############# --%>
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_hypertextAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Boolean'">
	<%-- ############# ATTRIBUTO Boolean ############# --%>
	<p>
			<span><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></span>:<br />
	</p>
	<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_booleanAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'ThreeState'">
	<%-- ############# ATTRIBUTO ThreeState ############# --%>
	<p>
		<span><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></span>:<br />
	</p>
	<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_threeStateAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'Number'">
	<%-- ############# ATTRIBUTO Number ############# --%>
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_numberAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Date'">
	<%-- ############# ATTRIBUTO Date ############# --%>
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_dateAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Enumerator'">
	<%-- ############# ATTRIBUTO TESTO Enumerator ############# --%>
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label><br />
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_enumeratorAttribute.jsp" />
	</p>
</s:elseif>

<s:elseif test="#attribute.type == 'CheckBox'">
	<%-- ############# ATTRIBUTO CheckBox ############# --%>
	<p>
		<label for="<s:property value="attribute_id" />"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></label>:
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_checkBoxAttribute.jsp" />
	</p>
</s:elseif>	

<s:elseif test="#attribute.type == 'Monolist'">
	<%-- ############# ATTRIBUTO Monolist ############# --%>
	<p>
		<span><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></span>:
	</p>
	<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monolistAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'List'">
	<%-- ############# ATTRIBUTO List ############# --%>
	<p>
		<span><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></span>:<br />			
	</p>
	<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monolistAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'Composite'">
	<%-- ############# ATTRIBUTO Composite ############# --%>
	<p>
		<span><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_attributeInfo.jsp" /></span>:<br />
	</p>
	
	<div>
		<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_compositeAttribute.jsp" />
	</div>

</s:elseif>