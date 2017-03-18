<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page">
		<span class="panel-body display-block">
				<s:text name="jpversioning.admin.menu" />&#32;/&#32;
				<a href="<s:url action="list" namespace="do/jpversioning/Content/Versioning" />"
				title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpversioning.content" />">
				<s:text name="title.jpversioning.content" /></a>&#32;/&#32;
				<s:text name="jpversioning.label.detail" />
		</span>
</h1>
<div id="main">
<p class="sr-only"><s:text name="note.workingOn" />:&#32;<em class="important"><s:property value="content.descr"/></em> (<s:property value="content.typeDescr"/>)</p>
<h3 class="sr-only"><s:text name="title.quickMenu" /></h3>
<ul class="sr-only">
	<li><a href="#jpcontentinspection_metadata"><abbr title="<s:text name="metadata.full" />"><s:text name="metadata" /></abbr></a></li>
	<li><a href="#jpcontentinspection_referral_contents"><s:text name="title.referencingContents" /></a></li>
	<li><a href="#jpcontentinspection_referring_conts"><s:text name="title.referencedContents" /></a></li>
	<li><a href="#jpcontentinspection_pages"><s:text name="title.referencedPages" /></a></li>
	<li><a href="#jpcontentinspection_contents"><s:text name="title.content" /></a></li>
	<li><a href="#jpcontentinspection_referencing_pages"><s:text name="title.referencingPages" /></a></li>
</ul>
<%-- metadata --%>
	<p class="margin-base-vertical text-right">
		<button type="button" data-toggle="collapse" data-target="#jpcontentinspection_info" class="btn btn-link">
			<s:text name="label.info" />
			<span class="icon-chevron-down"></span>
		</button>
	</p>
	<div class="collapse" id="jpcontentinspection_info">
		<table class="table table-bordered">
			<tr>
				<th class="text-right"><s:text name="label.description" /></th>
				<td><s:property value="contentVersion.descr" /></td>
			</tr>
			<tr>
				<th class="text-right"><s:text name="jpversioning.label.version.date" /></th>
				<td><code><s:date name="contentVersion.versionDate" format="dd/MM/yyyy HH:mm" /></code></td>
			</tr>
			<tr>
				<th class="text-right"><s:text name="jpversioning.label.author" /></th>
				<td><code><s:property value="%{contentVersion.username}" /></code></td>
			</tr>
			<tr>
				<th class="text-right"><s:text name="jpversioning.label.version" /></th>
				<td><s:property value="contentVersion.version" /></td>
			</tr>
			<tr>
				<th class="text-right"><s:text name="jpversioning.label.status" /></th>
				<td><s:property value="%{getText(contentVersion.status)}" /></td>
			</tr>
			<tr>
				<s:set var="contentGroup" value="%{getGroup(content.getMainGroup())}" />
				<th class="text-right"><s:text name="label.ownerGroup" /></th>
				<td><s:property value="%{#contentGroup.descr}" /></td>
			</tr>
			<tr>
				<th class="text-right"><s:text name="label.viewgroups" /></th>
				<td>
				<s:if test="%{content.groups.size() <= 0}">
					<span class="text-muted"><s:text name="noExtraGroup" /></span>
				</s:if>
				<s:if test="%{content.groups.size() > 0}">
						<s:iterator value="content.groups" var="groupName">
							<s:property value="%{getGroupsMap()[#groupName].getDescr()}"/>&#32;
						</s:iterator>
				</s:if>
			</tr>

			<tr>
				<th class="text-right"><s:text name="label.categories" /></th>
				<td>
					<s:if test="%{content.categories.size() <=  0}">
						<span class="text-muted"><s:text name="category.no.category" /></span>
					</s:if>

					<s:if test="%{content.categories.size() > 0}">
							<s:iterator value="content.categories" var="contentCategory">
								<s:property value="#contentCategory.defaultFullTitle"/>&#32;
							</s:iterator>
					</s:if>
				</td>
			</tr>
		</table>
	</div>
	<s:include value="/WEB-INF/plugins/jpversioning/apsadmin/jsp/versioning/inc/contentView.jsp" />
</div>
