<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#attribute.attributes.size() != 0">
<ul>
</s:if>
<s:set var="masterListAttributeTracer" value="#attributeTracer" />
<s:set var="masterListAttribute" value="#attribute" />
<s:iterator value="#attribute.attributes" var="attribute" status="elementStatus">
<s:set var="attributeTracer" value="#masterListAttributeTracer.getMonoListElementTracer(#elementStatus.index)"></s:set>

<s:set var="elementIndex" value="#elementStatus.index" />

	<s:if test="#attribute.type == 'Composite'">
<li class="contentAttributeBox">	
		<p>
			<span class="important"><s:property value="#elementStatus.index + 1" />:</span>
		</p>
	</s:if>
	<s:else>
<li>	
		<label for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#elementStatus.index + 1" /></label>
	</s:else>
	<s:if test="#attribute.type == 'Monotext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/monotextAttribute.jsp" />
	</s:if>
	<s:elseif test="#attribute.type == 'Text'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Longtext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/longtextAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Date'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/dateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Number'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/numberAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Hypertext'">
		<s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/message/modules/view/monotextAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Enumerator'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'EnumeratorMap'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorMapAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Composite'">
		<s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/message/modules/view/compositeAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Boolean'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/booleanAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'ThreeState'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/threeStateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'CheckBox'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/checkBoxAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'File'">
		<s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/message/modules/view/fileAttribute.jsp" />
	</s:elseif>
</li>
</s:iterator>

<s:set var="attributeTracer" value="#masterListAttributeTracer" />
<s:set var="attribute" value="#masterListAttribute" />
<s:set var="elementIndex" value="" />
<s:if test="#attribute.attributes.size() != 0">
</ul>
</s:if>
<s:else><s:text name="label.attribute.listEmpty" /></s:else>