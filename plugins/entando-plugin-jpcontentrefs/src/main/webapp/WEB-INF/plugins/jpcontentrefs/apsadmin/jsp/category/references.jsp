<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
			<s:text name="jpcontentrefs.title.deleting" />
	</span>
</h1>
<div class="alert alert-warning">
	<s:text name="jpcontentrefs.note.youAreDeleting" />
	&#32;
	<em class="important">
		<s:property value="%{getContentType(contentTypeCode).getDescr()}" />
	</em>
	&#32;
	<s:text name="jpcontentrefs.note.referencedBy" />&#32;
	<em class="important">
		<s:property value="%{getCategory(categoryCode).title}" />
	</em>
</div>

<div class="table-responsive">
	<table class="table" summary="<s:text name="jpcontentrefs.note.contents.summary" />">
		<caption class="sr-only"><span><s:text name="jpcontentrefs.title.contentAssociated" /></span></caption>
		<tr>
			<th><s:text name="jpcontentrefs.label.code" /></th>
			<th><s:text name="jpcontentrefs.label.description" /></th>
			<th><s:text name="jpcontentrefs.label.group" /></th>
			<th><s:text name="jpcontentrefs.label.lastModify" /></th>
		</tr>
		<s:iterator value="references" var="content" >
			<tr>
				<td><code><s:property value="#content.id" /></code></td>
				<td><s:property value="#content.descr" /></td>
				<td><s:property value="#content.mainGroupCode" /></td>
				<td class="icon"><span class="monospace"><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></span></td>
			</tr>
		</s:iterator>
	</table>
</div>
