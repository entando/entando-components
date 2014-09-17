<%@ taglib prefix="s" uri="/struts-tags" %>
{
	"result": "<s:property value="targetFramePos"/>",
	"shiftingElements": 
	<s:if test="null != shiftingElements && shiftingElements.size() > 0">
		[
		<s:iterator value="shiftingElements" var="frame" status="status">
			[<s:property value="#frame.key" />, <s:property value="#frame.value" />]
			<s:if test="!#status.last">,</s:if>
		</s:iterator>
		]
	</s:if>
	<s:else>null</s:else>
}
<%--
Esempio completo di risposta
{
	"result": "9",
	"shiftingElements": 
		[
			[8, 9], //vecchia posizione, nuova posizione
			[9, 10],
			[10, 11]
		]
}
--%>