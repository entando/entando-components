<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String cId = java.util.UUID.randomUUID().toString();
%>
<s:if test="#request['bpmcss']==null">
    <link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jbpm-widget-ext.css" rel="stylesheet">
    <s:set var="bpmcss" value="true" scope="request"/>
</s:if>

<s:if test="#request['angular']==null">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/angular.min.js"></script>
    <s:set var="angular" value="true" scope="request"/>
</s:if>

<s:if test="#request['bpmProgressBar']==null">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-progressbar-component.js"></script>
    <s:set var="bpmProgressBar" value="true" scope="request"/>
</s:if>

<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>

<form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view.action"/>" method="post" class="form-horizontal" >
    <s:if test="casePath != null">
        <s:hidden name="casePath" escapeHtml="false" escapeJavaScript="false"/>
    </s:if>
    <s:if test="knowledgeSourceId != null">
        <s:hidden name="knowledgeSourceId" escapeHtml="false" escapeJavaScript="false"/>
    </s:if>
    <s:if test="containerid != null">
        <s:hidden name="containerid" escapeHtml="false" escapeJavaScript="false"/>
    </s:if>
    <s:if test="channelPath != null">
        <s:hidden name="channelPath" escapeHtml="false" escapeJavaScript="false"/>
    </s:if>
</form>
<div  class="ibox float-e-margins">

    <div class="ibox-title">
        <h5>Case progress status</h5>
        <div class="ibox-tools">
            <a class="collapse-link">
                <i class="fa fa-chevron-up"></i>
            </a>
            <a class="close-link"> <i class="fa fa-times"></i> </a>
        </div>
    </div>
    <s:if test="%{null != caseInstanceMilestones}">
        <div class="ibox-content">
            <div  id="<%=cId%>" ng-controller="ProgressBarCtrl as vm" class="ibox float-e-margins" >
                <script type="text/ng-template" id="basic-tpl">
                    <h5><small ng-show="vm.ui.showNumberOfTasks()">Tasks {{vm.ui.countAchievedMilestones()}} of {{vm.ui.countVisibleMilestones()}}</small></h5>
                    <div class="progress progress-labeled">
                    <div ng-style="{width: vm.ui.totalCaseCompletedPercentage() + '%'}" ng-class="vm.ui.milestoneCompletedStyles()"class="progress-bar">
                    <span>{{vm.ui.totalCaseCompletedPercentage()+'%'}}</span>
                    </div>
                    </div>
                </script>
                <script type="text/ng-template" id="stacked-tpl">
                    <h5><small ng-show="vm.ui.showNumberOfTasks()">Tasks {{vm.ui.countAchievedMilestones()}} of {{vm.ui.countVisibleMilestones()}}</small></h5>
                    <div class="progress progress-labeled">
                    <div ng-repeat="ms in vm.ui.filterVisibleMiletones()"ng-style="{width: ((100/vm.ui.countVisibleMilestones()) + '%')}" ng-class="{'progress-bar-success':vm.ui.milestoneComplete(ms) }" class="progress-bar">
                    <span>{{vm.ui.milestoneComplete(ms)?"COMPLETED":"IN PROGRESS"}}</span>
                    </div>
                    </div>
                    <div class="progress progress-labels" ng-show="vm.ui.showMilestonesLabels()">
                    <span ng-repeat="ms in vm.ui.filterVisibleMiletones()" ng-style="{width: ((100/vm.ui.countVisibleMilestones()) + '%')}" class="progress-bar progress-bar-label">
                    {{ms["milestone-name"]}}
                    </span>
                    </div>
                </script>
                <progress-bar options='<s:property value="frontEndMilestonesData" escapeHtml="false" escapeJavaScript="false"/>' case-data='<s:property value="caseInstanceMilestones" escapeHtml="false" escapeJavaScript="false"/>'></progress-bar>
            </div>
        </div>
    </s:if>

</div>
<s:if test="%{null != caseInstanceMilestones}">
    <script type="text/javascript">
        (function () {
            bootBpmProgressBarComponents('<%=cId%>');
            angular.element(document).ready(function () {
                angular.bootstrap(document.getElementById('<%=cId%>'), ['<%=cId%>']);
            });
        })();
    </script>
</s:if>

