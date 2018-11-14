<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
<s:if test="#request['ngjsoneditor']==null">
    <link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jsoneditor.min.css" rel="stylesheet">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jsoneditor.min.js"></script>
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/ng-jsoneditor.min.js"></script>
    <s:set var="ngjsoneditor" value="true" scope="request"/>
</s:if>

<s:if test="#request['bpmCaseFile']==null">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-case-file.js"></script>
    <s:set var="bpmCaseFile" value="true" scope="request"/>
</s:if>


<div  class="ibox float-e-margins"  id="<%=cId%>" ng-controller="CaseFileController as vm">
    <div class="ibox-title">
        <h5>Case Instance File</h5>
        <div class="ibox-tools">
            <a class="collapse-link"> 
                <i class="fa fa-chevron-up"></i> 
            </a> 
            <a class="close-link"> <i class="fa fa-times"></i> </a> 
        </div>
    </div>
    <s:if test="%{null != casefile}">
    <div class="ibox-content">
        <div class="panel-body" style="min-height:400px">
            <div ng-jsoneditor ng-model="vm.mod.caseFile" options="{}"></div>
            <form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseInstanceFile/updateData.action"/>" method="post" class="form-horizontal" >
                <%--<s:form namespace="/ExtStr2/do/bpm/FrontEnd/CaseInstanceFile" action="updateData" method="post" cssClass="form-horizontal">--%>
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

                <s:hidden name="data" id="data" cssClass="form-control" value="{{vm.mod.caseFile|json}}" />
                <h4>Data id (key) to be deleted</h4>
                <s:textfield name="dataId" id="dataId" cssClass="form-control"/>

                <wpsf:submit
                    type="button" 
                    action="updateData" 
                    name="updateData" 
                    cssClass="btn btn-warning btn-sm" 
                    >Edit
                </wpsf:submit>

                <wpsf:submit 
                    type="button" 
                    action="deleteData" 
                    name="deleteData" 
                    cssClass="btn btn-default btn-sm" 
                    >Remove
                </wpsf:submit>
            </form>

        </div>
    </div>
    </s:if>
</div>    


<script type="text/javascript">

    (function () {
    <s:if test="casefile != null">
        bootBpmCaseFileComponents('<%=cId%>', <s:property value="casefile" escapeHtml="false" escapeJavaScript="false"/>);

        angular.element(document).ready(function () {
            angular.bootstrap(document.getElementById('<%=cId%>'), ['<%=cId%>']);
        });

    </s:if>
    })();
</script>