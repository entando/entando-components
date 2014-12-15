<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/jpcomponentinstaller/Installer" action="intro" />"><s:text name="jpcomponentinstaller.title.installerManagement" /></a>
		&#32;/&#32;
		<s:text name="jpcomponentinstaller.title.installer.confirmUninstall" />
	</span>
</h1>

<s:form action="uninstall" namespace="/do/jpcomponentinstaller/Installer">
	<p class="sr-only">
		<wpsf:hidden name="componentCode" />
	</p>
	<s:set var="componentVar" value="%{getComponent(componentCode)}" />
	<div>
		<p>
			<s:text name="jpcomponentinstaller.note.uninstaller.confirmUninstall" />&#32;
			'<s:property value="#componentVar.description" />'?
		</p>
		<div class="input-group">
			<div class="text-center margin-large-top">
				<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
					<span class="icon fa fa-times-circle"></span>&#32;
					<s:text name="label.uninstall" />
				</wpsf:submit>
				<a class="btn btn-link" href="<s:url namespace="/do/jpcomponentinstaller/Installer" action="intro" />">
				<s:text name="note.goToSomewhere" />:&#32;<s:text name="jpcomponentinstaller.title.installerManagement" /></a>
			</div>
		</div>
	</div>
</s:form>
