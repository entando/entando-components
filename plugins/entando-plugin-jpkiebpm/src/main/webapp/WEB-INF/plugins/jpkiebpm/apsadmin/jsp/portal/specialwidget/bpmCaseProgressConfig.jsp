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

<div class="mb-20" data-ng-app="caseProgressApp" data-ng-controller="CaseProgressConfigCtrl as vm">

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

                <div class="form-horizontal">
                    <div class="form-group">
                        <label class="control-label col-xs-2" for="processPath">
                            <s:text name="Process"/>
                        </label>
                        <div class="col-xs-5">
                            <s:if test="!#isProcessPathSetted">
                                <s:select list="process" id="processPath" name="processPath"  value="processPathDefaultValue">
                                </s:select>
                            </s:if>
                            <s:else>
                                <s:select disabled="true" list="process" id="processPath" name="processPath" value="processPathDefaultValue">
                                </s:select>
                                <%--<s:hidden name="processPath"/>--%>
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

                    <div class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-xs-4" for="knowledgeSources">
                                <s:text name="Knowledge Sources"/>
                            </label>
                            <div class="col-xs-8">
                                <select ng-options="knowledgeSource.name for knowledgeSource in vm.knowledgeSources.all track by knowledgeSource.id" ng-model="vm.form.knowledgeSource" ng-change="vm.ui.updateCaseDefs()">
                                </select>

                            </div>

                        </div>
                        <div class="form-group" ng-show="vm.form.knowledgeSource">
                            <label class="control-label col-xs-4">Cases</label>
                            <div class="col-xs-8">
                                <select ng-options="caseDef.name for caseDef in vm.defs.all track by caseDef['container-id']" ng-model="vm.form.caseDef">
                                </select>
                            </div>
                        </div>
                    </div>

                    <hr/>         
                    <%--<s:property value="casesDefinitions"/>--%>
                    <br />
                    <table id="sort" class="grid table table-bordered table-hover" ng-show="vm.form.caseDef">
                        <thead>
                            <tr>

                                <th class="text-center table-w-5">Visible</th>
                                <th class="table-w-20">Milestone Name</th>
                                <th class="text-center table-w-20">Completed (Even by Default)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="milestone in vm.form.caseDef.milestones track by milestone['milestone-id']">

                                <td class="text-center">
                                    <input type="checkbox" ng-model="milestone.visible">
                                </td>

                                <td class="field text-center">{{ milestone['milestone-name']}}</td>

                                <td class="text-center">
                                    <input type="text" width="20" ng-model="milestone.percentage"/>
                                </td>

                            </tr>

                        </tbody>
                    </table>

                    <br />
                    <!--strurt-->

                    <%--<s:textarea name="casesDefinitions" id="casesDefinitions" ></s:textarea>--%>
                    <%--<wpsf:textfield name="casesDefinitions" id="casesDefinitions"></wpsf:textfield>--%>
                </s:if>
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
                        (function ngApp(){

                        angular.module('caseProgressApp', [])
                                .controller('CaseProgressConfigCtrl', CaseProgressConfigCtrl)
                                .service("BpmService", BpmService);
                                function CaseProgressConfigCtrl($log, BpmService) {
                                var vm = this;
                                        //hold all known knoledge sources
                                        vm.knowledgeSources = {};
                                        //hold all case definitions by selected knowledge source                        
                                        vm.defs = {};
                                        //data selected by the user
                                        vm.form = {
                                        knowledgeSource : undefined,
                                                caseDef : undefined
                                        };
                                        vm.ui = {updateCaseDefs : loadCaseDefOnSelectedKS};
                                        function loadCaseDefOnSelectedKS(){
                                        loadCaseDefinition(vm.form.knowledgeSource);
                                        }


                                function loadKnowledgeSources(){
                                BpmService.knowledgeSources()
                                        .then(function success(res){
                                        vm.knowledgeSources.all = res.data;
                                        }, function errHandler(error){
                                        $log.error("Ops... something goes wrong!", err);
                                        })
                                }


                                function loadCaseDefinition(knowledgeSource){

                                BpmService.caseDefinition(knowledgeSource)
                                        .then(function(res){
                                        vm.defs.all = res.data.definitions;
                                        }, function(err){
                                        $log.error("Ops... something goes wrong!", err);
                                        });
                                }



                                function init(){
                                loadKnowledgeSources();
                                }

                                init();
                                }

                        function BpmService($http, $q){


                        this.caseDefinition = readCaseDefinition;
                                this.knowledgeSources = knowledgeSources;
                                function readCaseDefinition(){
                                var promise = $q(
                                        function mockData(resolve, reject){
                                        resolve({data:fakeDataCases});
                                        });
                                        return promise;
                                }


                        function knowledgeSources(){
                        var promise = $q(
                                function mockKSs(resolve, reject){
                                resolve({data:fakeKSs})
                                });
                                return promise;
                        }

                        var fakeKSs = [{
                        name: "Knowledge Source A",
                                id:"kbs-a"
                        },
                        {
                        name: "Knowledge Source B",
                                id:"kbs-b"
                        }];
                                var fakeDataCases = <s:property value="casesDefinitions"/>;
//                                {
//                        "definitions": [
//                        {
//                        "name": "Order for IT hardware",
//                                "id": "itorders.orderhardware",
//                                "version": "1.0",
//                                "case-id-prefix": "IT",
//                                "container-id": "itorders_1.0.0-SNAPSHOT",
//                                "adhoc-fragments": [
//                                {
//                                "name": "Prepare hardware spec",
//                                        "type": "HumanTaskNode"
//                                },
//                                {
//                                "name": "Milestone 1: Order placed",
//                                        "type": "MilestoneNode"
//                                },
//                                {
//                                "name": "Milestone 2: Order shipped",
//                                        "type": "MilestoneNode"
//                                },
//                                {
//                                "name": "Milestone 3: Delivered to customer",
//                                        "type": "MilestoneNode"
//                                },
//                                {
//                                "name": "Hardware spec ready",
//                                        "type": "MilestoneNode"
//                                },
//                                {
//                                "name": "Manager decision",
//                                        "type": "MilestoneNode"
//                                }
//                                ],
//                                "roles": {
//                                "owner": 1,
//                                        "manager": 1,
//                                        "supplier": 2
//                                },
//                                "milestones": [
//                                {
//                                "milestone-name": "Milestone 1: Order placed",
//                                        "milestone-id": "_DCD97847-6E3C-4C5E-9EE3-221C04BE42ED",
//                                        "milestone-mandatory": false,
//                                        "visible": true,
//                                        "percentage": "30"
//                                },
//                                {
//                                "milestone-name": "Milestone 2: Order shipped",
//                                        "milestone-id": "_343B90CD-AA19-4894-B63C-3CE1906E6FD1",
//                                        "milestone-mandatory": false,
//                                        "visible": true,
//                                        "percentage": "30"
//                                },
//                                {
//                                "milestone-name": "Milestone 3: Delivered to customer",
//                                        "milestone-id": "_52AFA23F-C087-4519-B8F2-BABCC31D68A6",
//                                        "milestone-mandatory": false,
//                                        "visible": true,
//                                        "percentage": "30"
//                                },
//                                {
//                                "milestone-name": "Hardware spec ready",
//                                        "milestone-id": "_483CF785-96DD-40C1-9148-4CFAFAE5778A",
//                                        "milestone-mandatory": false,
//                                        "visible": false,
//                                        "percentage": "30"
//                                },
//                                {
//                                "milestone-name": "Manager decision",
//                                        "milestone-id": "_79953D58-25DB-4FD6-94A0-DFC6EA2D0339",
//                                        "milestone-mandatory": false,
//                                        "visible": true,
//                                        "percentage": "10"
//                                }
//                                ],
//                                "stages": []
//                        }
//                        ]
//                        };
                        }
                        })();


    </script>


