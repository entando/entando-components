<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
    String cId = java.util.UUID.randomUUID().toString();
%>
<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>
<s:if  test="#request['bpmcss']==null">
    <link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jbpm-widget-ext.css" rel="stylesheet">
    <s:set var="bpmcss" value="true" scope="request"/>
</s:if>

<s:if test="#request['angular']==null">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/angular.min.js"></script>
    <s:set var="angular" value="true" scope="request"/>
</s:if>

<s:if test="#request['bpmCharts']==null">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/Chart.min.js"></script>
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-charts.js"></script>
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/angular-chart.min.js"></script>
    <s:set var="bpmCharts" value="true" scope="request"/>
</s:if>

<!-- Part related to jsoneditor angular-->
<s:if test="#request['ngjsoneditor']==null">
    <link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jsoneditor.min.css" rel="stylesheet">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jsoneditor.min.js"></script>
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/ng-jsoneditor.min.js"></script>
    <s:set var="ngjsoneditor" value="true" scope="request"/>
</s:if>

<div  class="ibox float-e-margins"  id="<%=cId%>" ng-controller="CaseMilestoneChartController as vm">
    <div class="ibox-title">
        <h5>BPM case charts</h5>
        <div class="ibox-tools">
            <a class="collapse-link"> 
                <i class="fa fa-chevron-up"></i> 
            </a> 
            <a class="close-link">
                <i class="fa fa-times"></i>
            </a> 
        </div>
    </div>
    <s:if test="%{null != milestones}">
    <div class="ibox-content" style="display: block;">
        <div class="row">
            <div class="col-lg-8 col-md-6 col-sm-12">
                <canvas id="doughnut" class="chart-base" chart-type="vm.mod.chartType"
                        chart-data="vm.mod.chart.data" chart-labels="vm.mod.chart.labels" chart-options="vm.mod.chart.options" chart-colors="vm.mod.chart.colors">
                </canvas> 
            </div>
            <div class="col-lg-4 col-md-6 col-sm-12">
                <div class="well well-sm" ng-repeat="ms in vm.mod.details" style="margin-bottom: 2px;">
                    <span class="label" ng-class="vm.ui.getLabelClass(ms)">{{vm.ui.getLabelText(ms)}}</span> {{ms["milestone-name"]}}
                </div>
            </div>
        </div>

        <br>
        <!--        <div class="panel panel-danger">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-bug" aria-hidden="true"></i> Config 
                            <small>Es. <code>chartType</code> can be switched to [pie,doughnut]<br> Milestone can be added or setted as achieved</small></h3>
                    </div>
                    <div class="panel-body" style="min-height:300px">
                        <div ng-jsoneditor ng-model="vm.mod" options="{}"></div>
                    </div>
                </div>-->
    </div>
    </s:if>
</div>    

<script>
    (function () {
    <s:if test="milestones != null">
        bootBpmChartsComponents('<%=cId%>',<s:property value="milestones" escapeHtml="false" escapeJavaScript="false"/>);

        angular.element(document).ready(function () {
            angular.bootstrap(document.getElementById('<%=cId%>'), ['<%=cId%>']);
        });
    </s:if>
    })();
</script>
