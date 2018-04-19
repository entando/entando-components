<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String cId = java.util.UUID.randomUUID().toString();
%>
<c:if test="${empty requestScope['bpmcss']}">
	<link rel="stylesheet"
		href="<wp:resourceURL />plugins/jpkiebpm/static/css/jbpm-widget-ext.css"
		rel="stylesheet">
	<c:set var="bpmcss" value="true" scope="request" />
</c:if>

<c:if test="${empty requestScope['angular']}">
	<script
		src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>
	<c:set var="angular" value="true" scope="request" />
</c:if>
<c:if test="${empty requestScope['angular-sce']}">
	<script
		src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular-sanitize.min.js"></script>
	<c:set var="angular-sce" value="true" scope="request" />
</c:if>

<c:if test="${empty requestScope['bpmKieserverServices']}">
	<script src="<wp:resourceURL />plugins/jpkiebpm/administration/js/kieserver.service.js"></script>
	<c:set var="bpmKieserverServices" value="true" scope="request" />
</c:if>

<c:if test="${empty requestScope['bpmProcessDiagram']}">
	<script
		src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-process-diagram-component.js"></script>
	<c:set var="bpmProcessDiagram" value="true" scope="request" />
</c:if>

<div id="<%=cId%>" ng-controller="ProcessEntandoController as vm">

<process-diagram options="vm.config"></process-diagram>

</div>


<script type="text/javascript">
        (function () {
        	<c:if test="${empty requestScope['bpmKieserverServicesBoot']}">
        	kieCommons('<wp:info key="systemParam" paramName="applicationBaseURL" />');
        	<c:set var="bpmKieserverServicesBoot" value="true" scope="request" />
        	</c:if>

			var savedConfig = <wp:currentWidget param="config" configParam="frontEndConfig" escapeXml="false"/>;


        	
            bootProcessDiagramComponent('<%=cId%>','<wp:resourceURL />',savedConfig);
            angular.element(document).ready(function () {
                angular.bootstrap(document.getElementById('<%=cId%>'), ['<%=cId%>']);
				});
	})();
</script>


