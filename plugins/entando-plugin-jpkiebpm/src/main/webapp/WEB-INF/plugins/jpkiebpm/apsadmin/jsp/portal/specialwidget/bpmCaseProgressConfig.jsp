<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery-ui.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>


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

<div class="mb-20" data-ng-app="caseProgressApp" data-ng-controller="myCtrl">

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode"/>

    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
        <s:param name="selectedNode" value="pageCode"></s:param>
    </s:action>

    <s:form action="save" namespace="/do/bpm/Page/SpecialWidget/BpmCaseProgressViewer" class="form-horizontal">
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

                <s:set var="isProcessPathSetted" value="%{processPath != null && processPath != ''}"/>
                <s:set var="isCaseSetted" value="%{casesPath != null && casesPath != ''}"/>


                <div class="form-horizontal">
                    <div class="form-group">
                        <label class="control-label col-xs-2" for="processPath">
                            <s:text name="Process"/>
                        </label>
                        <div class="col-xs-5">
                            <s:if test="!#isProcessPathSetted">
                                <s:select list="process" id="processPath" name="processPath"
                                          listKey="containerId"
                                          listValue="processName">
                                </s:select>
                            </s:if>
                            <s:else>
                                <s:select disabled="true" list="process" id="processPath" name="processPath"
                                          listKey="containerId"
                                          listValue="processName">
                                </s:select>
                                <s:hidden name="processPath"/>

                            </s:else>
                        </div>

                        <s:if test="#isProcessPathSetted">
                            <div class="col-xs-2">
                                <wpsf:submit action="changeForm" value="%{getText('label.changeForm')}"
                                             cssClass="btn btn-warning pull-right"/>
                            </div>
                        </s:if>
                        <s:else>
                            <div class="col-xs-2">
                                <wpsf:submit action="chooseForm" value="%{getText('label.chooseForm')}"
                                             cssClass="btn btn-success pull-right"/>
                            </div>
                        </s:else>
                    </div>

                </div>

                <s:if test="#isProcessPathSetted">

                    <%--<s:property value="processPath"/>--%>
                    <%--<s:property value="cases"/>--%>

                    <div class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-xs-2" for="casesPath">
                                <s:text name="Cases"/>
                            </label>
                            <div class="col-xs-5">
                                <s:if test="!#isCaseSetted">
                                    <s:select list="cases" id="casesPath" name="casesPath" value="casesPathDefaultValue">
                                    </s:select>
                                </s:if>
                                <s:else>
                                    <s:select disabled="true" list="cases" id="casesPath" name="casesPath" value="casesPathDefaultValue">
                                    </s:select>
                                    <s:hidden name="casesPath"/>

                                </s:else>
                            </div>

                            <s:if test="#isCaseSetted">
                                <div class="col-xs-2">
                                    <wpsf:submit action="changeCaseForm" value="Change Case"
                                                 cssClass="btn btn-warning pull-right"/>
                                </div>
                            </s:if>
                            <s:else>
                                <div class="col-xs-2">
                                    <wpsf:submit action="chooseCaseForm" value="Choose Case"
                                                 cssClass="btn btn-success pull-right"/>
                                </div>
                            </s:else>
                        </div>

                    </div>

                </s:if>
                <s:if test="#isCaseSetted">
                    <hr/>

                    <!--<h1>Values:</h1>-->
                    <%--<s:property value="milestones"/>--%>
                    

                    <div class="table-responsive overflow-visible">
                        <input type="hidden" name="frontEndMilestonesData" id="frontEndMilestonesData" value=""/>
                        <h2>{{ frontEndMilestonesData }}</h2>
                        <br />
                        <h2>{{ milestones }}</h2>
                        
                        
                        
                        <br />
                        <br />
                        <h1>Direct Ourput:</h1>
                        <s:property value="milestoneJson"/>
                        <br />
                        <s:property value="milestones"/>
                        <table id="sort" class="grid table table-bordered table-hover">
                            <thead>
                                <tr>

                                    <th class="text-center table-w-5">Visible</th>
                                    <th class="table-w-20">Milestone Name</th>
                                    <th class="text-center table-w-20">% Completed (Even by Default)</th>
                                </tr>
                            </thead>
                            <tbody>

                                <tr ng-repeat="x in milestones">

                                    <td class="text-center">
                                        <input type="checkbox">
                                    </td>

                                    <td class="field text-center">{{ x }}</td>

                                    <td class="text-center">
                                        <input type="text"/>
                                    </td>

                                </tr>
                                <%-- <s:iterator var="i" status="status" value="milestones">
                                    <tr>

                                        <td class="text-center">
                                            <input type="checkbox" name="visible_${i}">
                                        </td>

                                        <td class="field text-center">${i}
                                            <input type="hidden" name="field_${i}" value="${i}"/>
                                        </td>
                                        <td class="text-center">
                                            <input type="text" name="percentage_${i}"/>
                                        </td>
                                    </tr>
                                </s:iterator>--%>

                            </tbody>
                        </table>

                        <style>
                            .ui-sortable-helper {
                                display: table;
                            }
                        </style>

                    </div>

                </s:if>
            </div>
        </div>
        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-6">
                    <input type="button" cssClass="btn btn-primary" id="milestonetablesavebt" value="Apply"/>
                    <!--ng-click="setfrontEndMilestonesData()" />-->
                </div>
                <div class="col-xs-6">

                    <wpsf:submit disabled="!#isProcessPathSetted" type="button" cssClass="btn btn-primary pull-right"
                                 action="save">
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>

<script>
    var app = angular.module('caseProgressApp', []);
    app.controller('myCtrl', function ($scope) {

        var milestoneJson = <s:property value="milestoneJson"/>;
        var milestones = <s:property value="milestones"/>;

        $scope.frontEndMilestonesData = milestoneJson;
        $scope.milestones = milestones;

//        $scope.setfrontEndMilestonesData = function () {
//
//        }

    });


</script>
