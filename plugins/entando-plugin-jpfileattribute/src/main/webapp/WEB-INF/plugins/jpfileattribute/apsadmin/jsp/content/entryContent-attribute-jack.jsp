<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="#attribute.type == 'File'">
<p>
	<span class="important"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:</span>
	<s:include value="/WEB-INF/plugins/jpfileattribute/apsadmin/jsp/entity/modules/fileAttribute.jsp" />
</p>
</s:if>