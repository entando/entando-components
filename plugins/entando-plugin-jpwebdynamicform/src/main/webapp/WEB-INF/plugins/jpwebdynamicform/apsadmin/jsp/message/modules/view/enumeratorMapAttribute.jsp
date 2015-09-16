<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="%{null == #attribute.getMapKey()}" >&ndash;</s:if>
<s:else>
	<p><label><s:text name="label.key" /></label>:&#32;<s:property value="%{#attribute.getMapKey()}" /></p>
	<p><label><s:text name="label.value" /></label>:&#32;<s:property value="%{#attribute.getMapValue()}" /></p>
</s:else>