<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="jpiot.title.iotConfigManagement" /></a>&#32;/&#32;
		<s:if test="getStrutsAction() == 4">
			<s:text name="jpiot.iotConfig.label.delete" />
		</s:if>
	</span>
</h1>

<div id="main" role="main">

<s:form action="delete">
	<p class="sr-only">
		<wpsf:hidden name="strutsAction" />
		<wpsf:hidden name="id" />
	</p>
	<div class="alert alert-warning">
		<p>
			<s:text name="note.deleteIotConfig.areYouSure" />&#32;
			<code><s:property value="id"/></code>?
		</p>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
				<span class="icon fa fa-times-circle"></span>&#32;
				<s:text name="jpiot.iotConfig.label.delete" />
			</wpsf:submit>
			<a href="<s:url action="list"/>" class="btn btn-link"><s:text name="note.goToSomewhere" />: <s:text name="jpiot.title.iotConfigManagement" /></a>
		</div>
	</div>
</s:form>

</div>
