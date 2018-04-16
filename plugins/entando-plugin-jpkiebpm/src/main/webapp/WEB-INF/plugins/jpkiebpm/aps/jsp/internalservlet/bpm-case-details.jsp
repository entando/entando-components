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

<s:if test="#request['bpmDetails']==null">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-details.js"></script>
    <s:set var="bpmDetails" value="true" scope="request"/>
</s:if>

<div class="constainer-fluid" id="<%=cId%>" ng-controller="CaseDetailsController as vm">
     <form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseInstanceDetails/view.action"/>" method="post" class="form-horizontal" >
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
    <div class="ibox">
        <div class="ibox-content">
            <div class="row">
                <div class="col-lg-12">
                    <div class="m-b-md">
                        <h2>{{vm.mod.details['case-description']}}</h2>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-5">
                    <dl class="dl-horizontal">
                        <dt>Status</dt> <dd><span class="label label-primary">{{vm.mod.details['case-status']}}</span></dd>
                        <dt>Owner</dt> <dd>{{vm.mod.details['case-owner']}}</dd>
                        <dt>Milestones</dt> <dd>{{vm.mod.details['case-milestones'].length}}</dd>
                    </dl>
                </div>
                <div class="col-lg-7">
                    <dl class="dl-horizontal">
                        <dt>Started</dt> <dd ng>{{vm.mod.details['case-started-at']|date:'short'}}</dd>
                        <dt>Participants</dt>
                        <dd>
                            <span ng-repeat="(partecipant, pdata) in vm.mod.partecipants"><i class="fa" ng-class="pdata.fa"></i> {{partecipant}} </span>
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>


<script type="text/javascript">

    (function () {
    <s:if test="caseInstanceDetails != null">
                        bootBpmDetailsComponents('<%=cId%>', <s:property value="caseInstanceDetails" escapeHtml="false" escapeJavaScript="false"/>);

        angular.element(document).ready(function () {
                            angular.bootstrap(document.getElementById('<%=cId%>'), ['<%=cId%>']);
        });

    </s:if>
    })();
</script>