<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>

<script
	src="<wp:resourceURL />plugins/jpkiebpm/administration/js/kieserver.service.js"></script>
<script
	src="<wp:resourceURL />plugins/jpkiebpm/administration/js/jbpm-process-diagram-config.js"></script>




<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><a href="<s:url action="viewTree" namespace="/do/Page" />"
		title="<s:text name="note.goToSomewhere" />:
           <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" />
	</a></li>
	<li class="page-title-container"><s:text name="title.configPage" />
	</li>
</ol>
<h1 class="page-title-container">
	<s:text name="title.configPage" />
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>
<br>

<div class="mb-20">

	<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />

	<s:action namespace="/do/Page" name="printPageDetails"
		executeResult="true" ignoreContextParams="true">
		<s:param name="selectedNode" value="pageCode"></s:param>
	</s:action>

	<s:form action="save"
		namespace="/do/bpm/Page/SpecialWidget/BpmProcessDiagramViewer"
		class="form-horizontal"  ng-app="bpm-process-diagram-config" ng-controller="ProcessConfigEntandoController as vm">
		<p class="noscreen">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="widgetTypeCode" />
		</p>
		<div class="panel panel-default" >
			<div class="panel-heading">
				<s:include
					value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
			</div>
			<div class="panel-body">
				<process-config options="vm.config"></process-config>
			</div>
			<wpsf:hidden name="frontEndConfig" value="{{vm.config|json}}" />
			<h3>Configuration Preview (DEV TOOLS)</h3>
			<pre style="font-size: 0.7em">{{vm.config|json}}</pre>
			<div class="form-horizontal">
				<div class="form-group">
					<div class="col-xs-12">
						<wpsf:submit type="button" cssClass="btn btn-primary pull-right"
							action="save">
							<s:text name="%{getText('label.save')}" />
						</wpsf:submit>
					</div>
				</div>
			</div>
	</s:form>
	<script type="text/javascript">
		kieCommons('<wp:info key="systemParam" paramName="applicationBaseURL" />');
		bootBpmComponent('<wp:resourceURL />');
	</script>
</div>
