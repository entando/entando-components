<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpcomponentinstaller.title.installerManagement" />
	</span>
</h1>

<s:if test="hasActionMessages()">
	<div class="alert alert-success alert-dismissable fade in">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
		<ul class="margin-base-top">
			<s:iterator value="actionMessages">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>
<s:if test="hasActionErrors()">
	<div class="alert alert-danger alert-dismissable">
		<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<p><s:text name="message.title.ActionErrors" /></p>
	</div>
</s:if>
<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable">
		<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<p><s:text name="message.title.FieldErrors" /></p>
	</div>
</s:if>

<%--
<p class="sr-only"><a href="#editFrame"><s:text name="note.goToEditFrame" /></a></p>
--%>
<div id="main" role="main">
	
	<s:set var="currentReportVar" value="currentReport" />
	
	<fieldset class="margin-large-top">
	<legend data-toggle="collapse" data-target="#installed-components"><s:text name="jpcomponentinstaller.title.installedComponents" />&#32;<span class="icon fa fa-chevron-down"></span></legend>
	
	<div class="collapse" id="installed-components">
		
		CREATION: <s:date name="#currentReportVar.creation" format="dd/MMMM/yyyy HH:mm" />
		<br />
		LAST UPDATE: <s:date name="#currentReportVar.lastUpdate" format="dd/MMMM/yyyy HH:mm" />
		<br />
		STATUS: <s:property value="#currentReportVar.status" />
		<br />
		
		<table class="table table-bordered table-hover table-condensed table-striped">
			<tr>
				<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
				<th>Component</th>
				<th class="text-center">Date</th>
			</tr>
			<s:iterator var="componentReportVar" value="#currentReportVar.reports">
			<s:set var="componentVar" value="%{getComponent(#componentReportVar.componentCode)}" />
			<tr>
				<td class="text-center text-nowrap">
					<s:if test="%{null != componentVar.uninstallerInfo}" >
						<%-- remove --%>
						<s:url action="installIntro" var="uninstallActionVar">
							<s:param name="componentCode" value="#componentVar.code"/>
						</s:url>
						<div class="btn-group btn-group-xs">
							<a class="btn btn-warning"
								href="<s:property value="#uninstallActionVar" escapeHtml="false" />"
								title="<s:text name="label.uninstall" />: <s:property value="#componentVar.description" />" >
								<span class="sr-only"><s:text name="label.alt.clear" /></span>
								<span class="icon fa fa-times-circle-o"></span>&#32;
							</a>
						</div>
					</s:if>
				</td>
				<td>
					<s:property value="#componentVar.description" />&#32;<code><s:property value="#componentReportVar.componentCode" /></code>
					<br />
					<s:property value="#componentVar.artifactId" />&#32;<s:property value="#componentVar.artifactGroupId" />&#32;
					<s:property value="#componentVar.artifactVersion" />&#32;
				</td>
				<td class="text-center">
					<code><s:date name="#componentReportVar.date" format="dd/MM/yyyy HH:mm:ss" /></code>
				</td>
			</tr>
			</s:iterator>
		</table>
	</div>
	</fieldset>
	
	<fieldset>
		<legend><s:text name="jpcomponentinstaller.title.newComponent" /></span></legend>
		<div class="panel panel-default">
			<div class="panel-body">
				<p class="sr-only"><s:text name="jpcomponentinstaller.note.chooseAComponent" /></p>
				<s:form action="chooseVersion">
					<div class="form-group margin-base-vertical">
						<label for="availableComponent" class="sr-only"><s:text name="jpcomponentinstaller.title.chooseAComponent" /></label>
						<div class="input-group">
							<span class="input-group-addon" title="<s:text name="jpcomponentinstaller.title.chooseAComponent" />">
								<span class="icon fa fa-puzzle-piece"></span>
							</span>
							<select name="availableArtifactId" id="artifactCode" class="form-control">
								<wpsa:set var="tmpAvailableComponentVar">tmpAvailableComponentValue</wpsa:set>
								<s:iterator var="availableComponentVar" value="availableComponents">
									<s:if test="#availableComponentVar.optgroup != #tmpAvailableComponentVar">
									</optgroup>
									<optgroup label="<s:property value="#availableComponentVar.optgroup" />">
									</s:if>
									<option value="<s:property value="#availableComponentVar.key" />"><s:property value="#availableComponentVar.value" /></option>
									<wpsa:set var="tmpAvailableComponentVar"><s:property value="#availableComponentVar.optgroup" /></wpsa:set>
								</s:iterator>
								</optgroup>
							</select>
							<span class="input-group-btn">
								<wpsf:submit type="button" cssClass="btn btn-success">
									<span class="icon fa fa-check"></span>&#32;
									<s:text name="label.install" />
								</wpsf:submit>
							</span>
						</div>
					</div>
				</s:form>
			</div>
		</div>
	</fieldset>
	
</div>
