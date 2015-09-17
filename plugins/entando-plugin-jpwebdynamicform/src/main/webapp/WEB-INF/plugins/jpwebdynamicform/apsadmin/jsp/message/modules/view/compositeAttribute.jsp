<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set name="masterCompositeAttributeTracer" value="#attributeTracer" />
<s:set name="masterCompositeAttribute" value="#attribute" />
<s:iterator value="#attribute.attributes" id="attribute">
<s:set name="attributeTracer" value="#masterCompositeAttributeTracer.getCompositeTracer(#masterCompositeAttribute)"></s:set>
<s:set name="parentAttribute" value="#masterCompositeAttribute"></s:set>
	<s:property value="#attribute.name"/>:&#32;
	<s:if test="#attribute.type == 'Boolean' || #attribute.type == 'CheckBox'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/booleanAttribute.jsp" />
	</s:if>
	<s:elseif test="#attribute.type == 'ThreeState'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/threeStateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Date'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/dateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Number'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/numberAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'EnumeratorMap'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/enumeratorMapAttribute.jsp" />
	</s:elseif>
	<s:else>
		<!-- ############# Text attributes - Monotext, Text, Longtext, Enumerator, ecc. ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/monotextAttribute.jsp" />
	</s:else>
	<br />
</s:iterator>
<s:set name="attributeTracer" value="#masterCompositeAttributeTracer" />
<s:set name="attribute" value="#masterCompositeAttribute" />
<s:set name="parentAttribute"  value=""></s:set>