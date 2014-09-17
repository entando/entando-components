<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<s:set name="currentFolder" value="currentFolder" />
<s:set name="parentFolder" value="#currentFolder.parent" />

<h3><s:text name="title.folders"/></h3>
<p>
	<s:text name="label.currentFolder"/>: <s:property value="#currentFolder.name" />
</p>

<p>
	<s:text name="label.lastRefresh"/>:<br />
	<s:bean name="java.util.Date" id="now"></s:bean>
	<s:date name="now" format="EEEE, dd MMMM yyyy HH:mm" /><br />
	(<a href="<s:url action="intro" />" ><s:text name="label.refreshNow"/></a>)
</p>

<ul>
<s:if test="#parentFolder.fullName != ''">
	<li>
		<a href="<s:url action="changeFolder"><s:param name="currentFolderName" value="#parentFolder.fullName" /></s:url>" title="<s:property value="#parentFolder.name" />"><abbr title="<s:property value="#parentFolder.name" />">..</abbr></a>
	</li>
</s:if>
<s:iterator value="currentChildrenFolders" id="folder">
	<li>
		<a href="<s:url action="changeFolder"><s:param name="currentFolderName" value="#folder.fullName" /></s:url>"><s:property value="#folder.name" /><s:if test="#folder.getUnreadMessageCount()">&#32;<strong>(<abbr title="<s:property value="#folder.getUnreadMessageCount()" />&#32;<wp:i18n key="WEBMAIL_MSGS_NOTREAD" />"><s:property value="#folder.getUnreadMessageCount()" /></abbr>)</strong></s:if></a>
	</li>
</s:iterator>
</ul>