<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="#attribute.attributes.size() != 0">
<ul>
</s:if>
<s:set name="masterListAttributeTracer" value="#attributeTracer" />
<s:set name="masterListAttribute" value="#attribute" />
<s:iterator value="#attribute.attributes" id="attribute" status="elementStatus">
<s:set name="attributeTracer" value="#masterListAttributeTracer.getMonoListElementTracer(#elementStatus.index)"></s:set>

<s:set name="elementIndex" value="#elementStatus.index" />
<li>
	<label for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#elementStatus.index + 1" /></label> 
	
	<s:if test="#attribute.type == 'Monotext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
	</s:if>
	<s:elseif test="#attribute.type == 'Text'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Longtext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Date'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Hypertext'">
		<%-- <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" /> --%>
	</s:elseif>
	<s:elseif test="#attribute.type == 'Image'">
		<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/imageAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Attach'">
		<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/attachAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Link'">
		<%--<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/linkAttribute.jsp" /> --%>
		<s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/linkAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Composite'">
		<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/compositeAttribute.jsp" />
	</s:elseif>
	&#32;
	<s:if test="#attribute.type != 'Composite'">	
		<s:if test="#lang.default">
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_operationModule.jsp" />
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
<p><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/listAttributes/allList_addElementButton.jsp" /></p>
</s:if>