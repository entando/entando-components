<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#attribute.value == null">-</s:if>
<s:elseif test="#attribute.value"><s:text name="label.yes" /></s:elseif>
<s:else><s:text name="label.no" /></s:else>
