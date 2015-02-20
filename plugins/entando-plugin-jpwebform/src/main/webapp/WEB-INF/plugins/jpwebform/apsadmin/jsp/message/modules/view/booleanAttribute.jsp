<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#attribute.booleanValue != null && #attribute.booleanValue"><s:text name="label.yes" /></s:if>
<s:else><s:text name="label.no" /></s:else>
