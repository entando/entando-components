<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="symbolicLink != null">
<p>
	<s:text name="note.previousLinkSettings" />&#32;
	<s:if test="symbolicLink.destType == 1">
		<s:text name="note.URLLinkTo"></s:text>: <s:property value="symbolicLink.urlDest"/>.
	</s:if>
	<s:if test="symbolicLink.destType == 2 || symbolicLink.destType == 4">
		<s:text name="note.pageLinkTo"></s:text>: <s:property value="%{getPage(symbolicLink.pageDest).titles[currentLang.code]}"/>
	</s:if>
	<s:if test="symbolicLink.destType == 3 || symbolicLink.destType == 4">
		<s:text name="note.contentLinkTo"></s:text>: <s:property value="symbolicLink.contentDest"/> &ndash; <s:property value="%{getContentVo(symbolicLink.contentDest).descr}"/>.
	</s:if>
</p>
</s:if>