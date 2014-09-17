<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page">
		<span class="panel-body display-block">
				<s:text name="jpversioning.admin.menu" />&#32;/&#32;
				<a href="<s:url action="list" namespace="do/jpversioning/Content/Versioning" />"
				title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpversioning.content" />">
				<s:text name="title.jpversioning.content" /></a>&#32;/&#32;
				<s:text name="title.jpversioning.delete.confirm" />
		</span>
</h1>

<div id="main">
	<s:set var="contentVersion" value="%{getContentVersion(versionId)}" />
	<s:form action="delete">
		<p class="sr-only">
			<s:hidden name="versionId" />
			<s:hidden name="contentId" />
			<s:hidden name="backId" />
		</p>
		<div class="alert alert-warning">
			<s:text name="message.jpversioning.confirmDelete" />&#32;<code><s:property value="%{#contentVersion.version}"/>&#32;<s:property value="#contentVersion.contentId" /></code>?
			<div class="text-center margin-large-top">
				<wpsf:submit type="button" cssClass="btn btn-warning btn-lg" >
					<span class="icon fa fa-times-circle"></span>&#32;
					<s:text name="label.remove" />
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>
