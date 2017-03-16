<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page">
		<span class="panel-body display-block">
				<s:text name="jpversioning.admin.menu" />&#32;/&#32;
				<a href="<s:url action="list" namespace="do/jpversioning/Content/Versioning" />"
				title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpversioning.content" />">
				<s:text name="title.jpversioning.content" /></a>&#32;/&#32;
				<s:text name="title.jpversioning.recover" />
		</span>
</h1>
<div id="main">

	<table class="table table-bordered">
		<tr>
			<th class="text-right">
				<s:text name="jpversioning.label.id" />
			</th>
			<td><s:property value="contentVersion.contentId" /></td>
		</tr>
		<tr>
			<th class="text-right">
				<s:text name="jpversioning.label.description" />
			</th>
			<td><s:property value="contentVersion.descr" /></td>
		</tr>
		<tr>
			<th class="text-right">
				<s:text name="jpversioning.label.lastVersion" />
			</th>
			<td><s:date name="contentVersion.versionDate" format="dd/MM/yyyy HH:mm" /></td>
		</tr>
		<tr>
			<th class="text-right">
				<s:text name="jpversioning.label.version" />
			</th>
			<td><s:property value="contentVersion.version" /></td>
		</tr>
	</table>

<%--		<h3><s:text name="title.jpversioning.referenced.resources" /></h3>
		<h4><s:text name="title.jpversioning.resources.images" /></h4>--%>
		<s:if test="%{trashedResources == null || trashedResources.size() == 0 }">
			<%--<p><s:text name="message.versioning.noTrashedResources" /></p>--%>
		</s:if>
		<s:else>
			<table class="generic">
				<caption><span><s:text name="title.versioning.resource.image.trashed" /></span></caption>
				<tr>
					<th><s:text name="versioning.resource.id"/></th>
					<th><s:text name="label.type"/></th>
					<th><s:text name="label.description"/></th>
					<th><s:text name="label.ownerGroup"/></th>
				</tr>
				<s:iterator var="id" value="trashedResources">
					<s:set name="resourceItem" value="getTrashedResource(#id)" />
					<tr>
						<td><s:property value="#resourceItem.id" /></td>
						<td><s:property value="#resourceItem.type" /></td>
						<td><s:property value="#resourceItem.descr" /></td>
						<td><s:property value="#resourceItem.mainGroup" /></td>
					</tr>
				</s:iterator>
			</table>
		</s:else>

		<%--<h4><s:text name="title.jpversioning.resources.attaches" /></h4>--%>
		<s:if test="%{trashRemovedResources == null || trashRemovedResources.size() == 0 }">
			<%--<p><s:text name="message.versioning.noTrashRemovedResources" /></p>--%>
		</s:if>
		<s:else>
			<table class="generic">
				<caption><span><s:text name="title.versioning.resource.attach.trashed" /></span></caption>
				<tr>
					<th><s:text name="versioning.resource.id"/></th>
					<th><s:text name="label.messages"/></th>
				</tr>
				<s:iterator var="id" value="trashRemovedResources">
					<tr>
						<td><s:property value="#id" /></td>
						<td><s:text name="message.versioning.trashRemovedResource" /></td>
					</tr>
				</s:iterator>
			</table>
		</s:else>
	<div class="subsection-light">
		<s:form action="recover" >
			<p class="sr-only">
				<wpsf:hidden name="versionId" />
				<wpsf:hidden name="contentId" />
				<wpsf:hidden name="contentOnSessionMarker" />
			</p>
			<div class="alert alert-info">
				<s:text name="jpversioning.confirmRestore.info" />
			</div>
			<div class="text-center margin-large-top">
				<wpsf:submit type="button" cssClass="btn btn-warning btn-lg" >
					<span class="icon fa fa-check"></span>&#32;
					<s:text name="label.confirm" />
				</wpsf:submit>
			</div>
		</s:form>
	</div>
</div>
