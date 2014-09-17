<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="#lang.default">
	<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/threeStateAttributeInputField.jsp" />
</s:if>
<s:else>
	<s:if test="#attributeTracer.listElement">
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/threeStateAttributeInputField.jsp" />
	</s:if>
	<s:elseif test="#attributeTracer.compositeElement">
		<s:text name="note.editContent.doThisInTheDefaultLanguage.must" />.
	</s:elseif>	
	<s:else>
		<s:text name="note.editContent.doThisInTheDefaultLanguage.must" />.
	</s:else>
</s:else>