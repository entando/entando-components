<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:if test="symbolicLink != null">
<p>
	<wp:i18n key="jpfastcontentedit_PREVIOUS_LINK" />&#32;
	<s:if test="symbolicLink.destType == 1">
		<wp:i18n key="jpfastcontentedit_LINK_TO_URL" />: <s:property value="symbolicLink.urlDest"/>.
	</s:if>
	<s:if test="symbolicLink.destType == 2 || symbolicLink.destType == 4">
		<wp:i18n key="jpfastcontentedit_LINK_TO_PAGE" />: <s:property value="%{getPage(symbolicLink.pageDest).titles[currentLang.code]}"/>.
	</s:if>
	<s:if test="symbolicLink.destType == 3 || symbolicLink.destType == 4">
		<wp:i18n key="jpfastcontentedit_LINK_TO_CONTENT" />: <s:property value="symbolicLink.contentDest"/> &ndash; <s:property value="%{getContentVo(symbolicLink.contentDest).descr}"/>.
	</s:if>
</p>
</s:if>