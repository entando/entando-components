<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="#attribute.failedDateString == null">
	<s:property value="%{#attribute.getFormattedDate('dd/MM/yyyy')}" />
</s:if>
<s:else>
	<s:property value="%{#attribute.failedDateString}" />
</s:else>
