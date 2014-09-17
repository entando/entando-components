<%@ taglib prefix="s" uri="/struts-tags" %>

<s:set name="masterCompositeAttributeTracer" value="#attributeTracer" />
<s:set name="masterCompositeAttribute" value="#attribute" />
<s:iterator value="#attribute.attributes" id="attribute">
<s:set name="attributeTracer" value="#masterCompositeAttributeTracer.getCompositeTracer(#masterCompositeAttribute)"></s:set>
<s:set name="parentAttribute" value="#masterCompositeAttribute"></s:set>

	<s:property value="#attribute.name"/>:&#32;

	<s:if test="#attribute.type == 'Boolean' || #attribute.type == 'CheckBox'">
		<!-- ############# ATTRIBUTO Boolean ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/booleanAttribute.jsp" />
	</s:if>

	<s:elseif test="#attribute.type == 'ThreeState'">
		<!-- ############# ATTRIBUTO ThreeState ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/threeStateAttribute.jsp" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Date'">
		<!-- ############# ATTRIBUTO ThreeState ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/dateAttribute.jsp" />
	</s:elseif>

	<s:elseif test="#attribute.type == 'Number'">
		<!-- ############# ATTRIBUTO ThreeState ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/numberAttribute.jsp" />
	</s:elseif>

	<s:else>
		<!-- ############# ATTRIBUTO Monotext (Text, Longtext, Enumerator, ecc.) ############# -->
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/apsadmin/jsp/message/modules/view/monotextAttribute.jsp" />
	</s:else>

	<br />

</s:iterator>
<s:set name="attributeTracer" value="#masterCompositeAttributeTracer" />
<s:set name="attribute" value="#masterCompositeAttribute" />
<s:set name="parentAttribute"  value=""></s:set>