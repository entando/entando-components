<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery-ui.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>

<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>
<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <h1>Hello BPMS World!</h1>
        <br />

        <!--Select Case Instanse Option-->
        <form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/selectCaseInstance.action"/>" method="post" class="form-horizontal">
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

       

        <!--//Select Case Instanse Option
        <br />
        Case Definition with Milestones Configurations:

        <br />
        <s:property value="frontEndMilestonesData" escapeHtml="false" escapeJavaScript="false"/>

        <br />
        <br />       

        Case Instance List:

        <br />
        <s:property value="cases" escapeHtml="false" escapeJavaScript="false"/>

        <br />
        <br />
        Selected Case Instance Milestone:
        <br />

        <s:property value="caseInstanceMilestones" escapeHtml="false" escapeJavaScript="false"/>
        <s:property/>
        
        -->

        
         <s:if test="caseInstanceMilestones!=null">
         <div  data-ng-app="caseProgressApp" ng-controller="ProgressBarCtrl as vm">


            <div class="progress progress-striped">

                <div ng-repeat="ms in vm.def.milestones "  ng-style="{width: vm.ui.getPercentage(ms)+'%'}" class="progress-bar progress-bar-success">
                    <span  class="sr-only">{{ms["milestone-name"]}}</span>
                </div>

            </div>

        </div>
         </s:if>
    </div>
</div>
<script type="text/javascript">
    (function ngApp() {


        //Case Definition with Milestones Configurations:


        var caseDefinitionData = <s:property value="frontEndMilestonesData" escapeHtml="false" escapeJavaScript="false"/>;

        //Case Instance List:
        //var caseInstances = <s:property value="cases" escapeHtml="false" escapeJavaScript="false"/>;

        //Selected Case Instance Milestone:
    <s:if test="caseInstanceMilestones != null">
        var caseInstanceMilestones = <s:property value="caseInstanceMilestones" escapeHtml="false" escapeJavaScript="false"/>;
    </s:if>
    <s:else>
        var caseInstanceMilestones = undefined;
    </s:else>




        angular.module('caseProgressApp', [])
                .controller('ProgressBarCtrl', ProgressBarCtrl)
                .service("BpmService", BpmService);

        function ProgressBarCtrl($log, BpmService) {
            var vm = this;
            //hold all known knoledge sources
            vm.knowledgeSources = {};
            //hold all case definitions by selected knowledge source                        
            vm.caseInstance = {};
            //data selected by the user
            vm.def = {

                caseDef: undefined,
                milestones: undefined
            };
            vm.ui = {
                updateCaseDefs: loadCaseDefOnSelectedKS,
                caseInstanceStatus: loadCaseInstanceStatus,
                getPercentage: calculatePercentageForMilestone
            };


            function calculatePercentageForMilestone(ms){
                if(!ms.percentage){
                    ms.percentage =Math.floor((Math.random() * 40) + 1);
                }
                return  ms.percentage;
            }



            function loadCaseDefOnSelectedKS() {
                loadCaseDefinition(vm.form.knowledgeSource);
            }


            function loadCaseInstanceStatus(caseInstanceId) {
                BpmService.caseInstanceStatus(caseInstanceId)
                        .then(function success(res) {
                            vm.caseInstance.milestones = res.data;
                        }, function errHandler(error) {
                            $log.error("Ops... something goes wrong!", err);
                        })
            }


            function loadCaseDefinition(knowledgeSource) {

                BpmService.caseDefinition(knowledgeSource)
                        .then(function (res) {
                            vm.def.caseDef = res.data.definitions;
                            vm.def.milestones = res.data.definitions[0].milestones;

                        }, function (err) {
                            $log.error("Ops... something goes wrong!", err);
                        });
            }



            function init() {
                loadCaseDefinition();
                loadCaseInstanceStatus();

            }

            init();
        }

        function BpmService($http, $q) {
            this.caseDefinition = readCaseDefinition;
            this.caseInstanceStatus = readCaseInstanceStatus;

            function readCaseDefinition() {
                var promise = $q(
                        function loadCaseDefData(resolve, reject) {
                            resolve({data: caseDefinitionData});
                        });
                return promise;
            }


            function readCaseInstanceStatus(caseInstanceId) {
                var promise = $q(
                        function loadCaseInstanceData(resolve, reject) {
                            resolve({data: caseInstanceMilestones});
                        });
                return promise;
            }

        }

    })();
</script>