/* 
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var bootBpmComponents = (function ngApp(caseDefinitionData,caseInstanceMilestones) {
    'use strict';

    //Case Definition with Milestones Configurations:

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


        function calculatePercentageForMilestone(ms) {
            if (!ms.percentage) {
                ms.percentage = Math.floor((Math.random() * 40) + 1);
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

});
