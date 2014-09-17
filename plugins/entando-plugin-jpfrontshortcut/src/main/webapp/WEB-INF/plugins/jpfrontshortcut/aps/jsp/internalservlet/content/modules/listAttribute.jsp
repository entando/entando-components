<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#attribute.getAttributeList(#lang.code).size() != 0">
<ul>
</s:if>
<s:set name="masterListAttributeTracer" value="#attributeTracer" />
<s:set name="masterListAttribute" value="#attribute" />
<s:iterator value="#attribute.getAttributeList(#lang.code)" id="attribute" status="elementStatus">
<s:set name="attributeTracer" value="#masterListAttributeTracer.getListElementTracer(#lang, #elementStatus.index)"></s:set>

<s:set name="elementIndex" value="#elementStatus.index" />
<li>
	<label for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#elementStatus.index + 1" /></label>
	<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/listAttributes/allList_operationModule.jsp" /> 
	<s:if test="#attribute.type == 'Monotext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
	</s:if>
	<s:elseif test="#attribute.type == 'Enumerator'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Date'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Number'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Boolean'">
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/booleanAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'ThreeState'">
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/threeStateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'CheckBox'">
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/checkBoxAttribute.jsp" />
	</s:elseif>
</li>
</s:iterator>

<s:set name="attributeTracer" value="#masterListAttributeTracer" />
<s:set name="attribute" value="#masterListAttribute" />
<s:set name="elementIndex" value="" />
<s:if test="#attribute.getAttributeList(#lang.code).size() != 0">
</ul>
</s:if>

<p class="button-align"><s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/listAttributes/allList_addElementButton.jsp" /></p>