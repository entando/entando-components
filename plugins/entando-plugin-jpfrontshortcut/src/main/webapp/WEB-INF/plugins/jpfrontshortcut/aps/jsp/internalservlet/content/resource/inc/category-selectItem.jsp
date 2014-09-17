<%@ taglib prefix="s" uri="/struts-tags" %>

<option <s:if test="categoryCode.equals(#currentRoot.code)">selected="selected"</s:if> value="<s:property value="#currentRoot.code" />"><s:property value="#currentRoot.getShortFullTitle(currentLang.code)" /></option>
<s:if test="#currentRoot.children.length > 0">
	<s:iterator value="#currentRoot.children" var="nodeVar">
		<s:set name="currentRoot" value="#nodeVar" />
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/resource/inc/category-selectItem.jsp" />
	</s:iterator>
</s:if>