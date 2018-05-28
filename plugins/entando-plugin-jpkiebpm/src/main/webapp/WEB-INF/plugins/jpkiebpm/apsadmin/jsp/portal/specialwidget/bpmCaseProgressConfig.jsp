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

    <s:form action="save" namespace="/do/bpm/Page/SpecialWidget/BpmCaseProgressViewer" class="form-horizontal" data-ng-app="caseProgressApp" data-ng-controller="CaseProgressConfigCtrl as vm" >
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
                <s:set var="isCasesDefinitions" value="%{casesDefinitions != null && casesDefinitions != ''}"/>
                <s:set var="isChannelSetted" value="%{channel != null && channel != ''}"/>



                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-8 col-md-8 col-xs-8">

                            <div class="form-group">
                                <label for="knowledgeSource"><s:text name="knowledgeSource"/></label> 
                                <div class="input-group">

                                    <s:select list="knowledgeSource" id="knowledgeSourcePath" name="knowledgeSourcePath"  
                                              listKey="value.id"
                                              listValue="value.name" class="form-control">
                                    </s:select>
                                    <span class="input-group-btn">
                                        <s:if test="#isknowledgeSourcePathSetted">
                                            <wpsf:submit action="changeKnowledgeSourceForm" value="Change Knowledge Source"
                                                         cssClass="btn btn-warning"/>
                                        </s:if>
                                        <s:else>

                                            <wpsf:submit action="chooseKnowledgeSourceForm" value="Choose Knowledge Source"
                                                         cssClass="btn btn-success"/>
                                        </s:else>
                                    </span>
                                </div>
                            </div>
                        </div>

                    </div>

                    <s:if test="#isknowledgeSourcePathSetted">

                        <div class="row">
                            <div class="col-lg-8 col-md-8 col-xs-8">

                                <div class="form-group">
                                    <label for="Deployment Unit"><s:text name="Deployment Unit"/></label>
                                    <div class="input-group">
                                        <s:select list="process" id="container-id" name="processPath"  
                                                  listKey="containerId"
                                                  listValue="containerId" class="form-control">
                                        </s:select>
                                        <span class="input-group-btn">
                                            <s:if test="#isProcessPathSetted">
                                                <wpsf:submit action="changeForm" value="Change Deployment Unit"
                                                             cssClass="btn btn-warning"/>
                                            </s:if>
                                            <s:else>
                                                <wpsf:submit action="chooseForm" value="Choose Deployment Unit"
                                                             cssClass="btn btn-success"/>
                                            </s:else>
                                        </span>
                                    </div>
                                </div>
                            </div>

                        </div>

                    </s:if>
                    <s:if test="#isProcessPathSetted">
                        <div class="row">
                            <div class="col-lg-8 col-md-8 col-xs-8">
                                <div class="form-group">
                                    <label for="channel">Channel</label>
                                    <div class="input-group">
                                        <s:select list="channels" id="channel" name="channel" class="form-control">
                                        </s:select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </s:if>
                    <s:if test="#isCasesDefinitions">
                        <hr>
                        <div class="row">
                            <div class="col-lg-4">
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
                                        <input type="checkbox" ng-value="extInfo" ng-checked="extInfo.default || vm.form.additionalSettings[extInfo.id]" ng-model="vm.form.additionalSettings[extInfo.id]"
                                               ng-true-value="true" ng-false-value="false"> {{extInfo.name}}
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row" >
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
                                            <input type="number" class="form-control" width="20" ng-model="milestone.percentage" />
                                        </td>

                                    </tr>

                                </tbody>
                            </table>
                        </div>

                        <!-- Debug info section-->
                        <!--
                        <div class="panel panel-info">
                            <div class="panel-heading" >
                              <button class="btn btn-sm btn-primary pull-right m-t-n-xs" ng-click="vm.ui.save()" type="button">
                                    <strong>Generate Config</strong>
                                </button>
                            </div>
                            
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
                        -->
                        <input type="hidden" name="frontEndMilestonesData" id="frontEndMilestonesData" ng-value="vm.ui.defToJSONEscaped()"/>
                    </s:if>
                </div>
            </div>
        </div>
        <s:if test="channel != null">
            <wpsf:hidden name="channel"/>
        </s:if>
        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-6">
                    <!--<input type="button" cssClass="btn btn-primary" id="milestonetablesavebt" value="Apply"/>-->
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
    <pre style="display:none;">
    <!--Saved Configurations-->
        <s:if test="frontEndMilestonesData != null">
            <s:property value="frontEndMilestonesData" escapeJavaScript="false" escapeHtml="false"/>
        </s:if>
    <!--Saved Configurations-->
    <!--knowledgeSource value-->
        <s:if test="configID != null">
            <s:property value="configID" escapeJavaScript="false" escapeHtml="false"/>
        </s:if>
    <!--/knowledgeSource value-->

    <!--kieContainerListJson value-->
        <s:if test="kieContainerListJson != null">
            <s:property value="kieContainerListJson" escapeJavaScript="false" escapeHtml="false"/>
        </s:if>
    <!--/kieContainerListJson value-->
    <!--knowledgeSourceJson value-->
        <s:if test="knowledgeSourceJson != null">
            <s:property value="knowledgeSourceJson" escapeJavaScript="false" escapeHtml="false"/>
        </s:if>
    <!--/knowledgeSourceJson value-->
    </pre>
</div>

<script type="text/javascript">

    <s:if test="configID != null">
    var configName = "<s:property value="configID" escapeJavaScript="false" escapeHtml="false"/>";
    </s:if>
    <s:else>
    var configName = undefined;
    </s:else>
    <s:if test="frontEndMilestonesData != null && !frontEndMilestonesData.equals('')">
    var savedConfiguration = <s:property value="frontEndMilestonesData" escapeJavaScript="false" escapeHtml="false"/>;
    </s:if>
    <s:else>
    var savedConfiguration = undefined;
    </s:else>
    <s:if test="casesDefinitions != null">
    var caseDefinitionData = <s:property value="casesDefinitions" escapeJavaScript="false" escapeHtml="false"/>;
    bootBpmComponent(caseDefinitionData, savedConfiguration, configName);
    </s:if>
    <s:else>
    //Case definition data is required to boot application
    //value retrieved = [<s:property value="casesDefinitions" escapeJavaScript="false" escapeHtml="false"/>;]
    </s:else>


</script>