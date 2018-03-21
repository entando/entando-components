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
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <!--Select Case Instanse Option-->
        <form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseInstanceSelector/selectCaseInstance.action"/>" method="post" class="form-horizontal">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="control-label col-xs-4" for="processPath">Select a Case Instance</label>
                    <div class="col-xs-6">
                        <s:select list="cases" id="casePath" name="casePath">
                        </s:select>

                        <s:hidden name="frontEndMilestonesData" id="frontEndMilestonesData"></s:hidden>
                        </div>
                        <div class="col-xs-2">
                        <wpsf:submit type="button" value="Select"
                                     cssClass="btn btn-primary pull-right" />
                    </div>
                </div>
            </div>
        </form>


        <!--Select Case Instanse Option-->
        <%--
        <br />
        Case Definition with Milestones Configurations:

        <br />
        <s:property value="frontEndMilestonesData" escapeHtml="false" escapeJavaScript="false"/>

                <br />       
       
                Case Instance List:
        
                <br />
        <s:property value="cases" escapeHtml="false" escapeJavaScript="false"/>

                <br />
                <br />
                Selected Case Instance Milestone:
                <br />

        <s:property value="caseInstanceMilestones" escapeHtml="false" escapeJavaScript="false"/>
       <s:property/>--%>




    </div>
</div>