<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>
<s:if test="#request['bpmcss']==null">
    <link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jbpm-widget-ext.css" rel="stylesheet">
    <s:set var="bpmcss" value="true" scope="request"/>
</s:if>

<s:if test="#request['angular']==null">
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>
    <s:set var="angular" value="true" scope="request"/>
</s:if>

<s:if test="#request['bpmComments']==null">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-comments.js"></script>
    <s:set var="bpmComments" value="true" scope="request"/>
</s:if>

<div class="constainer-fluid">
    <div class="ibox">
        <div class="ibox-title">

            <h2 class="card-pf-title">
                <span>Roles</span>
            </h2>
        </div>
        <div class="ibox float-e-margins">
            <div class="ibox-content">
                <div class="chat-form">
                    <form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseInstanceRoles/addRole.action"/>" method="post" class="form-horizontal" >
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
                        <div class="form-group">
                            <br />
                            <h4>Roles</h4>
                            <br />
                            <s:property value="roles" escapeHtml="false" escapeJavaScript="false"/>
                            <br />
                        </div>

                        <div class="form-group">
                            <s:textfield name="caseRoleName" cssClass="form-control"/>
                        </div>
                        <div class="form-group">
                            <s:textfield name="user" cssClass="form-control"/>
                        </div>
                        <div class="form-group">
                            <s:textfield name="group" cssClass="form-control"/>
                        </div>
                        <div class="text-right">
                            <s:submit type="button" action="deleteRole" value="Delete role" cssClass="btn btn-sm btn-primary m-t-n-xs" />
                        </div>
                        <div class="text-right">
                            <s:submit type="button" action="addRole" value="Add role" cssClass="btn btn-sm btn-primary m-t-n-xs" />
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>