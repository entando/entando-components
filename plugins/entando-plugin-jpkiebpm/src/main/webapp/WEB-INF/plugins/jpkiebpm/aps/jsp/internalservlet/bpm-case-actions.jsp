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

<s:if test="#request['bpmComments']==null">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-comments.js"></script>
    <s:set var="bpmComments" value="true" scope="request"/>
</s:if>


<div  class="ibox float-e-margins">
    <div class="ibox-title">
        <h5>Case Instance Details</h5>
        <div class="ibox-tools">
            <a class="collapse-link"> 
                <i class="fa fa-chevron-up"></i> 
            </a> 
            <a class="close-link"> <i class="fa fa-times"></i> </a> 
        </div>
    </div>
    <div class="ibox float-e-margins">
        <div class="ibox-content">
            <form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseInstanceActions/view.action"/>" method="post" class="form-horizontal" >
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
            <s:property value="caseInstanceDetails" escapeHtml="false" escapeJavaScript="false"/>

        </div>
    </div>
</div>
