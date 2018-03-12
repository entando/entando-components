<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>

<script src="<wp:resourceURL />plugins/jpkiebpm/administration/js/jbpm-component-config.js"></script>



<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />:
           <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.configPage"/>
    </li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.configPage"/>
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div class="mb-20" >

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode"/>

    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
        <s:param name="selectedNode" value="pageCode"></s:param>
    </s:action>

    <s:form action="save" namespace="/do/bpm/Page/SpecialWidget/BpmCaseProgressViewer" class="form-horizontal" data-ng-app="caseProgressApp" data-ng-controller="CaseProgressConfigCtrl as vm">
        <p class="noscreen">
            <wpsf:hidden name="pageCode"/>
            <wpsf:hidden name="frame"/>
            <wpsf:hidden name="widgetTypeCode"/>
        </p>

        <div class="panel panel-default">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp"/>
            </div>
            <div class="panel-body">
                <p class="h5 margin-small-vertical">
                    <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                    <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}"/>
                </p>
                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <strong><s:text name="message.title.FieldErrors"/></strong>
                        <ul>
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property/></li>
                                    </s:iterator>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <s:set var="isknowledgeSourcePathSetted" value="%{knowledgeSourcePath != null && knowledgeSourcePath != ''}"/>
                <s:set var="isProcessPathSetted" value="%{processPath != null && processPath != ''}"/>


                <div class="form-horizontal">
                    <div class="form-group">
                        <label class="control-label col-xs-4" for="knowledgeSource">
                            <s:text name="knowledgeSource"/>
                        </label>
                        <div class="col-xs-6">
                            <s:select list="knowledgeSource" id="knowledgeSourcePath" name="knowledgeSourcePath"  
                                      listValue="value.hostname">
                            </s:select>
                        </div>
                        <s:if test="#isknowledgeSourcePathSetted">
                            <div class="col-xs-2">
                                <wpsf:submit action="changeKnowledgeSourceForm" value="%{getText('label.changeForm')}"
                                             cssClass="btn btn-warning"/>
                            </div>
                        </s:if>
                        <s:else>
                            <div class="col-xs-2">
                                <wpsf:submit action="chooseKnowledgeSourceForm" value="%{getText('label.chooseForm')}"
                                             cssClass="btn btn-success"/>
                            </div>
                        </s:else>
                    </div>
                </div>


                <s:if test="#isknowledgeSourcePathSetted">
                    <div class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-xs-4" for="processPath">
                                <s:text name="Process"/>
                            </label>
                            <div class="col-xs-6">
                                <s:select list="process" id="processPath" name="processPath"  
                                          listKey="containerId"
                                          listValue="containerId">
                                </s:select>
                            </div>

                            <s:if test="#isProcessPathSetted">
                                <div class="col-xs-2">
                                    <wpsf:submit action="changeForm" value="%{getText('label.changeForm')}"
                                                 cssClass="btn btn-warning"/>
                                </div>
                            </s:if>
                            <s:else>
                                <div class="col-xs-2">
                                    <wpsf:submit action="chooseForm" value="%{getText('label.chooseForm')}"
                                                 cssClass="btn btn-success"/>
                                </div>
                            </s:else>
                        </div>
                    </div>
                </s:if>
                <s:if test="#isProcessPathSetted">


                    <div class="container-fluid">




                        <div class="row">
                            <div class="col-lg-4">
                                <p>KIE Case
                                    <sup>*</sup>
                                </p>
                                <div class="form-group">
                                    <label for="caseSelector">Cases</label>
                                    <select class="form-control" id="caseSelector" ng-options="caseDef.name for caseDef in vm.data.caseDefinitions.definitions track by caseDef['container-id']"
                                            ng-model="vm.form.caseDef"></select>
                                </div>

                            </div>
                            <div class="col-lg-4">
                                <p>Select the type of Progress bar
                                    <sup>*</sup>
                                </p>
                                <div class="radio" ng-repeat="pbType in vm.ui.data.progressBar.types track by pbType.id">
                                    <label>
                                        <input type="radio" ng-model="vm.form.progressBarType" name="progressbar-type" ng-value="pbType">{{pbType.name}}
                                    </label>
                                </div>

                            </div>
                            <div class="col-lg-4">
                                <p>Other features</p>
                                <div class="checkbox" ng-repeat="extInfo in vm.ui.data.progressBar.additionalInfos track by extInfo.id">
                                    <label>
                                        <input type="checkbox" ng-value="extInfo" ng-checked="extInfo.default" ng-model="vm.form.additionalSettings[extInfo.id]"
                                               ng-true-value="true" ng-false-value="false"> {{extInfo.name}}
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <table id="sort" class="grid table table-bordered table-hover" ng-show="vm.form.caseDef">
                                <thead>
                                    <tr>

                                        <th class="text-center col-md-1">Visible</th>
                                        <th class=" col-md-9">Milestone Name</th>
                                        <th class="text-center col-md-2">Completed
                                            <br>
                                            <small>(Even by Default)</small>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="milestone in vm.form.caseDef.milestones track by milestone['milestone-id']">

                                        <td class="text-center">
                                            <input type="checkbox" ng-model="milestone.visible">
                                        </td>

                                        <td class="field">{{ milestone['milestone-name']}}</td>

                                        <td class="text-center">
                                            <input type="text" class="form-control" width="20" ng-model="milestone.percentage" />
                                        </td>

                                    </tr>

                                </tbody>
                            </table>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                <button class="btn btn-sm btn-primary pull-right m-t-n-xs" ng-click="vm.ui.save()" type="button">
                                    <strong>Generate Config</strong>
                                </button>
                            </div>
                        </div>

                        <!-- Debug info section-->
                        <div class="panel panel-info">
                            <div class="panel-heading">
                                <i class="fa fa-bug"></i> DEV INFO (TO BE REMOVED) Widget Configuration Viewer
                            </div>
                            <div class="panel-body">
                                <pre>{{vm.data.widgetConfig ? (vm.data.widgetConfig | json) : "Click save to generate configuration"}}</pre>
                            </div>
                            <div class="panel-heading">
                                <i class="fa fa-bug"></i> DEV INFO (TO BE REMOVED) Case Definitions
                            </div>
                            <div class="panel-body">
                                <pre>{{vm.data.caseDefinitions|json}}</pre>
                            </div>

                        </div>

                    </div>

                    <!--Please store your JSON output in following hidden input in order to be sent to widget front end-->
                    <!--Please dont change the name or the id-->
                    <!--<input type="hidden" name="frontEndMilestonesData" id="frontEndMilestonesData" ng-model=""/>-->
                    <!--If you use the above hidden input, you must comment out the following tag so the form submitted with your data (instead of default data)-->

                    <input type="hidden" name="frontEndMilestonesData" id="frontEndMilestonesData" ng-value="vm.ui.defToJSONEscaped()"/>


                    <!--You also need to comment out line 98 & 114 in org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.BpmCaseProgressWidgetAction class -->
                </s:if>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-6">
                        <input type="button" cssClass="btn btn-primary" id="milestonetablesavebt" value="Apply"/>
                        <!--ng-click="setfrontEndMilestonesData()" />-->
                    </div>
                    <div class="col-xs-6">

                        <wpsf:submit disabled="!#isProcessPathSetted" type="button" ng-click="vm.ui.save()" cssClass="btn btn-primary pull-right"
                                     action="save">
                            <s:text name="%{getText('label.save')}"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>

        </s:form>
    </div>

    <script type="text/javascript">

        <s:if test="casesDefinitions != null">
        var caseDefinitionData = <s:property value="casesDefinitions" escapeJavaScript="false" escapeHtml="false"/>;
        </s:if>
        <s:else>
        var caseDefinitionData = undefined;
        </s:else>

        <s:if test="savedConfiguration != null">
        var savedConfiguration = <s:property value="savedConfiguration" escapeJavaScript="false" escapeHtml="false"/>;
        </s:if>
        <s:else>
        var savedConfiguration = undefined;
        </s:else>

        bootBpmComponent(caseDefinitionData, savedConfiguration);
    </script>


