<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ page contentType="charset=UTF-8" %>
<s:set var="contentVersionsInfo" value="%{getContentVersion(versionId)}" />
<s:set var="id" value="contentId" />

<h1 class="panel panel-default title-page">
		<span class="panel-body display-block">
				<s:text name="jpversioning.admin.menu" />&#32;/&#32;
				<a href="<s:url action="list" namespace="do/jpversioning/Content/Versioning" />"
				title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpversioning.content" />">
				<s:text name="title.jpversioning.content" /></a>&#32;/&#32;
				<s:text name="title.jpversioning.versionsManagement" /> of <code><s:property value="#id" /></code>
		</span>
</h1>
<div id="main">
	<s:form action="history" >
		<wpsa:subset source="contentVersions" count="10" objectName="groupContent" advanced="true" offset="5">
			<s:set var="group" value="#groupContent" />

			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>

			<p class="sr-only">
				<s:hidden name="versionId" />
				<s:hidden name="backId" />
				<s:hidden name="fromEdit" />
			</p>

		<div class="table-responsive">
			<table class="table table-bordered">
				<caption class="sr-only"><s:text name="title.jpversioning.versionList" /></caption>
				<tr>
					<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
					<th class="text-center"><abbr title="<s:text name="jpversioning.version.full" />"><s:text name="jpversioning.version.short" /></abbr></th>
					<th><s:text name="jpversioning.label.description" /></th>
					<th class="text-center"><s:text name="jpversioning.label.lastVersion" /></th>
					<th><s:text name="jpversioning.label.username" /></th>
					<th class="text-center">
						<abbr title="<s:text name="name.onLine" />">P</abbr>
					</th>
				</tr>

				<s:iterator var="id" status="statusIndex">
					<s:set var="contentVersion" value="%{getContentVersion(#id)}" />
					<tr>
						<td class="text-center text-nowrap">
							<div class="btn-group btn-group-xs">
								<%-- edit --%>
								<a class="btn btn-default" href="<s:url action="preview" ><s:param name="versionId" value="#contentVersion.id" /><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="fromEdit" value="fromEdit" /><s:param name="backId" value="versionId" /></s:url>" title="<s:text name="jpversioning.label.detailOf" />:&#32;<s:property value="#contentVersion.version" />">
								<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#model.description" /></span>
									<span class="icon fa fa-info"></span>
								</a>
								<a class="btn btn-default" href="<s:url action="entryRecover" ><s:param name="versionId" value="#contentVersion.id" /><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="fromEdit" value="fromEdit" /><s:param name="backId" value="versionId" /></s:url>" title="<s:text name="jpversioning.label.restore" />:&#32;<s:property value="#contentVersion.version" />" >
									<span class="sr-only"><s:text name="jpversioning.label.restore" />&#32;<s:property value="#model.description" /></span>
									<span class="icon fa fa-reply"></span>
								</a>
							</div>
							<%-- remove --%>
							<div class="btn-group btn-group-xs">
								<a class="btn btn-warning" href="<s:url action="trash" ><s:param name="versionId" value="#contentVersion.id" /><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="fromEdit" value="fromEdit" /><s:param name="backId" value="versionId" /></s:url>" title="<s:text name="label.remove" />:&#32;<s:property value="#contentVersion.version" />">
								<span class="icon fa fa-times-circle-o"></span>&#32;
								<span class="sr-only"><s:text name="label.alt.clear" /></span>
								</a>
							</div>

						</td>
						<td class="text-center text-nowrap">
							<code><s:property value="#contentVersion.version" /></code>
						</td>
						<td>
								<s:property value="#contentVersion.descr" />
						</td>
						<td class="text-center text-nowrap">
							<code><s:date name="#contentVersion.versionDate" format="dd/MM/yyyy HH:mm" /></code>
						</td>
						<td>
							<s:property value="#contentVersion.username" />
						</td>
						<s:if test="(#contentVersion.status == 'PUBLIC')">
							<s:set var="iconName">check</s:set>
							<s:set var="textVariant">success</s:set>
							<s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
						</s:if>
						<s:if test="(#contentVersion.status != 'PUBLIC')">
							<s:set var="iconName">pause</s:set>
							<s:set var="textVariant">warning</s:set>
							<s:set var="isOnlineStatus" value="%{getText('label.no')}" />
						</s:if>
						<td class="text-center">
							<%--<s:property value="#contentVersion.status" />--%>
							<span class="icon fa fa-<s:property value="iconName" /> text-<s:property value="textVariant" />" title="<s:property value="isOnlineStatus" />"></span>
							<span class="sr-only"><s:property value="isOnlineStatus" /></span>
						</td>
					</tr>
				</s:iterator>
			</table>

			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
		</wpsa:subset>
	</s:form>
</div>
