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

var bootBpmComponent = (function ngApp(caseDefinitionData) {
    'use strict';
    angular.module('caseProgressApp', [])
        .controller('CaseProgressConfigCtrl', CaseProgressConfigCtrl)
        .service("BpmService", BpmService);

    function CaseProgressConfigCtrl($filter, $log, BpmService) {

        var vm = this;

        vm.data = {
            caseDefinitions: undefined
        }
        vm.form = {
            progressBarType: undefined,
            additionalSettings: {}
        }

        vm.ui = {
            data: {
                progressBar: {
                    types: [{
                        id: 'basic',
                        name: 'Basic (Default)'
                    },
                    {
                        id: "stacked",
                        name: 'Stacked Bar with milestones'
                    }
                    ],
                    additionalInfos: [
                        {
                            id: "show-milestones",
                            name: "Show milestones"

                        },
                        {
                            id: "show-number-of-tasks",
                            name: "Show number of tasks"
                        }
                    ]
                }
            },
            save: buildWidgetConfig,
            defToJSONEscaped: defToJSONEscaped
        };

        function defToJSONEscaped() {
            return angular.toJson(vm.data.widgetConfig);
        }




        function buildWidgetConfig() {
            vm.data.widgetConfig =
                {
                    "container-id": vm.form.caseDef["case-id-prefix"],
                    "name": vm.form.caseDef.name,
                    "case-id-prefix": vm.form.caseDef["case-id-prefix"],
                    "stages": [],
                    "id": vm.form.caseDef.id,
                    "milestones": angular.copy(vm.form.caseDef.milestones),
                    "ui": {
                        "progress-bar-type": vm.form.progressBarType.id,
                        "additionalSettings": addUIAdditonalSettings(),
                    },
                    "version": "1.0"
                }
        }
        function filterVisibleMiletones(caseInstance) {
            return caseInstance ? filterMiletones(caseInstance.milestones, { "visible": true }) : [];
        }

        function filterMiletones(instance, filterMap) {
            return $filter('filter')(instance, filterMap);
        }

        function addUIAdditonalSettings() {
            var out = [];
            angular.forEach(vm.form.additionalSettings, function (value, key) {
                if (value) {
                    out.push(key);
                }
            })
            return out;
        }
        //init function  
        function init() {
            BpmService.data.caseDefinitions().then(function (caseDef) {
                angular.forEach(caseDef.definitions, function caseDefinition(def) {
                    angular.forEach(def.milestones, function extendMilestoner(milestones){
                      angular.extend(milestones,{visible:true,percentage:100/def.milestones.length})
                    })
                  })
                
                vm.data.caseDefinitions = caseDef;
            });


            //settings defaults
            vm.form.progressBarType = vm.ui.data.progressBar.types[0];
        }

        init();


    }

    function BpmService($http, $q) {
        this.data = {
            caseDefinitions: getCaseDefinitions
        }


        function getCaseDefinitions() {
            //Entando data injection
            var promise = $q(
                function loadCaseInstanceData(resolve, reject) {
                    resolve(caseDefinitionData);
                });
            return promise;

            /*
            return $http.get('/mocks/caseInstanceDef.json').then(
                function (res) {
                    return res.data;
                }
            )*/
        }
    }
});
