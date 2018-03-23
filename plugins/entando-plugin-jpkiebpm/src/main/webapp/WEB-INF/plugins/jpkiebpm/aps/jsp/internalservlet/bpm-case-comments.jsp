<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jbpm-widget-ext.css" rel="stylesheet">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery-ui.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-component.js"></script>


<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>
<div class="row">
    <div class="col-md-12">

        <form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseInstanceComments/postComment.action"/>" method="post" class="form-horizontal">
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
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-6">
                        <label class="control-label" for="commentInput">Comments ID</label>
                        <wpsf:textfield name="caseCommentId"/>
                    </div>
                    <div class="col-xs-6">
                        <label class="control-label" for="commentInput">Comments</label>
                        <wpsf:textfield name="commentInput"/>
                    </div>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-4">
                        <wpsf:submit type="button" action="updateComment" value="Update" cssClass="btn btn-primary pull-right" />
                    </div>
                    <div class="col-xs-4">
                        <wpsf:submit type="button" action="postComment" value="Submit" cssClass="btn btn-primary pull-right" />
                    </div>
                    <div class="col-xs-4">
                        <wpsf:submit type="button" action="deleteComment" value="Delete" cssClass="btn btn-primary pull-right" />
                    </div>
                </div>
            </div>
        </form>

        <s:property value="comments" escapeHtml="false" escapeJavaScript="false"/>

    </div>
</div>