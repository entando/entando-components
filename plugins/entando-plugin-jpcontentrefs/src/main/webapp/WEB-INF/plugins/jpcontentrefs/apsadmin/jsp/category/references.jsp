<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<h1><s:text name="jpcontentrefs.title.contentTypesManagement" /></h1>
<div id="main">

<h2><s:text name="jpcontentrefs.title.deleting" /></h2>

<p><s:text name="jpcontentrefs.note.youAreDeleting" />&#32;<em class="important"><s:property value="%{getContentType(contentTypeCode).getDescr()}" /></em>
&#32;<s:text name="jpcontentrefs.note.referencedBy" />&#32;<em class="important"><s:property value="%{getCategory(categoryCode).title}" /></em></p>

<table class="generic" summary="<s:text name="jpcontentrefs.note.contents.summary" />">
<caption><span><s:text name="jpcontentrefs.title.contentAssociated" /></span></caption>
	<tr>
		<th><s:text name="jpcontentrefs.label.code" /></th>
		<th><s:text name="jpcontentrefs.label.description" /></th>
		<th><s:text name="jpcontentrefs.label.group" /></th>
		<th><s:text name="jpcontentrefs.label.lastModify" /></th>
	</tr>
	<s:iterator id="content" value="references">
		<tr>
			<td><span class="monospace"><s:property value="#content.id" /></span></td>
			<td><s:property value="#content.descr" /></td>
			<td><s:property value="#content.mainGroupCode" /></td>
			<td class="icon"><span class="monospace"><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></span></td>
		</tr>
	</s:iterator>
</table>
</div>