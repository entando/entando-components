<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="#attribute.failedNumberString == null">
	<s:property value="%{#attribute.value}" />
</s:if>
<s:else>
	<s:property value="%{#attribute.failedNumberString}" />
</s:else>