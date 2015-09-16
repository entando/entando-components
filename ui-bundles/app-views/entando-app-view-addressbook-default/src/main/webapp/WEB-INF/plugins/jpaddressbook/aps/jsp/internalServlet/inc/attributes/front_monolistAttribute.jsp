<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<%--
	<p class="noscreen"><s:text name="note.monolist.intro" /></p>
--%>

<s:if test="#attribute.attributes.size() != 0">
	<ul>
</s:if>

<s:set name="masterListAttributeTracer" value="#attributeTracer" />
<s:set name="masterListAttribute" value="#attribute" />	

<s:iterator value="#attribute.attributes" id="attribute" status="elementStatus">
	<s:set name="attributeTracer" value="#masterListAttributeTracer.getMonoListElementTracer(#elementStatus.index)"></s:set>
	<s:set name="elementIndex" value="#elementStatus.index" />
	<s:set var="i18n_attribute_name">jpaddressbook_ATTR<s:property value="#attribute.name" /></s:set>
	<s:set var="attribute_id">jpaddressbook_<s:property value="#attribute.name" />_<s:property value="#elementStatus.index + 1" /></s:set>

	<s:if test="#attribute.type == 'Composite'">
		<li>
		<p><span class="important"><s:property
			value="#elementStatus.index + 1" />&#32;<s:text
			name="label.compositeAttribute.element" />:</span> <s:if
			test="#lang.default">
			<s:include
				value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_operationModule.jsp" />
		</s:if></p>
	</s:if>
	<s:else>
		<li>
			<label for="<s:property value="%{attribute_id}" />"><s:property value="#elementStatus.index + 1" /></label>
	</s:else>
		<s:if test="#attribute.type == 'Monotext'">
			<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monotextAttribute.jsp" />
		</s:if>
		<s:elseif test="#attribute.type == 'Text'">
			<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monotextAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Longtext'"> 
			<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_monotextAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Date'">
		 	<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_dateAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Enumerator'">
			<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_enumeratorAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'EnumeratorMap'">
			<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_enumeratorMapAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Hypertext'">
			<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/front_hypertextAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Composite'">
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/compositeAttribute.jsp" />
		</s:elseif>
		&#32;
		<s:if test="#attribute.type != 'Composite'">	
			<s:if test="#lang.default">
				<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/listAttributes/front_allList_operationModule.jsp" />
			</s:if>
		</s:if> 
	</li>
</s:iterator>

<s:set name="attributeTracer" value="#masterListAttributeTracer" />
<s:set name="attribute" value="#masterListAttribute" />
<s:set name="elementIndex" value="" />
<s:if test="#attribute.attributes.size() != 0">
</ul>
</s:if>

<s:if test="#lang.default">
<p><s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/attributes/listAttributes/front_allList_addElementButton.jsp" /></p>
</s:if>