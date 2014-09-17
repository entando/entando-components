<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="webmail" uri="/webmail-core" %>

<s:set name="currentFolder" value="currentFolder" />
<s:set name="parentFolder" value="#currentFolder.parent" />
<c:set var="startFolderPath">/ExtStr2/do/jpwebmail/Portal/WebMail/intro.action</c:set>

<h3><wp:i18n key="jpwebmail_FOLDERS" /></h3>
<p>
	<wp:i18n key="jpwebmail_CURRENT_FOLDER" />: <s:property value="#currentFolder.name" />
</p>

<p>
	<wp:i18n key="jpwebmail_REFRESH_LAST" />:<br />
	<s:bean name="java.util.Date" id="now"></s:bean>
	<s:date name="now" format="EEEE, dd MMMM yyyy HH:mm" /><br />
	(<a href="<wp:action path="${startFolderPath}" />" title="<wp:i18n key="jpwebmail_RETURN_START_FOLDER" />" ><wp:i18n key="jpwebmail_REFRESH_NOW" /></a>)
</p>

<ul>
<s:if test="#parentFolder.fullName != ''">
	<li>
		<a href="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/changeFolder" ><wp:parameter name="currentFolderName"><s:property value="#parentFolder.fullName" /></wp:parameter></wp:action>" title="<s:property value="#parentFolder.name" />&amp;currentFolderName=<s:property value="#parentFolder.fullName" />"><abbr title="<s:property value="#parentFolder.name" />">..</abbr></a>
	</li>
</s:if>
<s:iterator value="currentChildrenFolders" id="folder">
	<li>
		<s:if test="#folder.name.equals(#currentFolder.name)">
		<c:set var="customPageTitleVar" ><wp:currentPage /> - <s:property value="#folder.name" /><s:if test="#folder.getUnreadMessageCount()"> (<s:property value="#folder.getUnreadMessageCount()" />)</s:if></c:set>
		<webmail:customPageTitle title="${customPageTitleVar}"  />
		</s:if>
		<a href="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/changeFolder" ><wp:parameter name="currentFolderName"><s:property value="#folder.fullName" /></wp:parameter></wp:action>"><s:property value="#folder.name" /><s:if test="#folder.getUnreadMessageCount()">&#32;<strong>(<abbr title="<s:property value="#folder.getUnreadMessageCount()" />&#32;<wp:i18n key="jpwebmail_MSG_NOT_READ" />"><s:property value="#folder.getUnreadMessageCount()" /></abbr>)</strong></s:if></a>
	</li>
</s:iterator>
</ul>