<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>





<s:if test="#request['bpmcss']==null">
    <link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jbpm-widget-ext.css" rel="stylesheet">
    <s:set var="bpmcss" value="true" scope="request"/>
</s:if>
    
<%--<s:if test="#request['angular']==null">
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>
    <s:set var="angular" value="true" scope="request"/>
</s:if>-->

<%--<s:if test="#request['bpmComments']==null">
    <script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-comments.js"></script>
    <s:set var="bpmComments" value="true" scope="request"/>
</s:if>-->

<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>
<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <!--Select Case Instanse Option-->
        <form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseInstanceSelector/selectCaseInstance.action"/>" method="post" class="form-horizontal">
            <s:if test="knowledgeSourceId != null">
                <s:hidden name="knowledgeSourceId" escapeHtml="false" escapeJavaScript="false"/>
            </s:if>
            <s:if test="containerid != null">
                <s:hidden name="containerid" escapeHtml="false" escapeJavaScript="false"/>
            </s:if>
            <s:if test="channelPath != null">
                <s:hidden name="channelPath" escapeHtml="false" escapeJavaScript="false"/>
            </s:if>
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="control-label col-md-4 col-xs-12" for="processPath">Case Instance</label>
                    <div class=" col-md-8 col-xs-12">
                        <s:select list="cases" id="casePath" name="casePath" cssClass="form-control">
                        </s:select>
                        </div>
                        <div class="col-md-12 col-xs-12">
                        <wpsf:submit type="button" value="Select"
                                     cssClass="btn btn-primary pull-right" />
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>