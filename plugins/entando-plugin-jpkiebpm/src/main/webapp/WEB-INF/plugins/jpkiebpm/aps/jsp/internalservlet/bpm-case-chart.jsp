<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
    String cId = java.util.UUID.randomUUID().toString();
%>

<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>
<s:if test="#request['bpmcss']==null">
    <link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jbpm-widget-ext.css" rel="stylesheet">
    <s:set var="bpmcss" value="true" scope="request"/>
</s:if>

<s:if test="#request['angular']==null">
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>
    <s:set var="angular" value="true" scope="request"/>
</s:if>

<s:if test="#request['bpmCharts']==null">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-charts.js"></script>
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/angular-chart.min.js"></script>
    
    <s:set var="bpmCharts" value="true" scope="request"/>
</s:if>

 <s:if test="#request['ngjsoneditor']==null">
     <link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jsoneditor.min.css" rel="stylesheet">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jsoneditor.min.js"></script>
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/ng-jsoneditor.min.js"></script>
    angular-chart.min.js
    <s:set var="ngjsoneditor" value="true" scope="request"/>
</s:if>
       
    
    
    
    
<div class="ibox" id="<%=cId%>" ng-controller="CaseMilestoneChartController as vm">
  <div class="ibox-title"></div>
  <div class="ibox-content">
    <canvas id="doughnut" class="chart-base" chart-type="vm.mod.chartType"
    chart-data="vm.mod.chart.data" chart-labels="vm.mod.chart.labels" chart-options="vm.mod.chart.options" chart-colors="vm.mod.chart.colors">
  </canvas> 
      <br>
     <div class="panel panel-danger">
      <div class="panel-heading">
        <h3 class="panel-title">Config</h3>
      </div>
      <div class="panel-body" style="min-height:400px">
        <div ng-jsoneditor ng-model="vm.mod" options="{}"></div>
      </div>

    </div>
  </div>
</div>    
    
    
    
<script type="text/javascript">

    (function () {
    <s:if test="milestones != null">
        bootBpmChartsComponents('<%=cId%>',<s:property value="milestones" escapeHtml="false" escapeJavaScript="false"/>);

        angular.element(document).ready(function () {
            angular.bootstrap(document.getElementById('<%=cId%>'), ['<%=cId%>']);
        });

    </s:if>
    })();
</script>
    
    
 