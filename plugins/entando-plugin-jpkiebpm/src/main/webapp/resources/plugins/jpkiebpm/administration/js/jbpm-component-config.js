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

var bootBpmComponent = (function ngApp(caseDefinitionData, knowledgeSourcesData) {
    'use strict';
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
            knowledgeSource: undefined,
            caseDef: undefined
        };
        vm.ui = {
            updateCaseDefs: loadCaseDefOnSelectedKS,
            defToJSONEscaped: defToJSONEscaped
        };
        
        function defToJSONEscaped() {
            return angular.toJson({definitions: [vm.form.caseDef]});

        }


        function loadCaseDefOnSelectedKS() {
            loadCaseDefinition(vm.form.knowledgeSource);
        }


        function loadKnowledgeSources() {
            BpmService.knowledgeSources()
                    .then(function success(res) {
                        vm.knowledgeSources.all = res.data;
                    }, function errHandler(error) {
                        $log.error("Ops... something goes wrong!", err);
                    })
        }


        function loadCaseDefinition(knowledgeSource) {

            BpmService.caseDefinition(knowledgeSource)
                    .then(function (res) {
                        vm.defs.all = res.data.definitions;
                    }, function (err) {
                        $log.error("Ops... something goes wrong!", err);
                    });
        }



        function init() {
            loadKnowledgeSources();
        }

        init();
    }

    function BpmService($http, $q) {


        this.caseDefinition = readCaseDefinition;
        this.knowledgeSources = knowledgeSources;
        function readCaseDefinition() {
            var promise = $q(
                    function loadCaseDefData(resolve, reject) {
                        var milestones = caseDefinitionData.definitions[0].milestones;
                        angular.forEach(milestones, function addFields(value, key) {
                            value.visible = true;
                            value.percentage = Math.floor(100 / milestones.length);
                        }
                        )
                        resolve({data: caseDefinitionData});
                    });
            return promise;
        }


        function knowledgeSources() {
            var promise = $q(
                    function mockKSs(resolve, reject) {
                        resolve({data: knowledgeSourcesData})
                    });
            return promise;
        }


    }
});
