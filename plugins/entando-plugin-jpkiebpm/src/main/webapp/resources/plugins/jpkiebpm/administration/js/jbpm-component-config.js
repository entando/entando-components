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

var bootBpmComponent = (function ngApp(caseDefinitionData,savedConfiguration,configName) {
    'use strict';

    if(!caseDefinitionData && savedConfiguration){
        console.log("WARN:Rebuilding config from saved data");
        caseDefinitionData = {definitions : []}
        caseDefinitionData.definitions.push(angular.copy(savedConfiguration));
    }


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
                    "container-id": vm.form.caseDef["container-id"],
                    "name": vm.form.caseDef.name,
                    "knowledge-source-id": configName||"default",
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


        function loadConfiguration(configuration) {
            var matchingConf = $filter('filter')(vm.data.caseDefinitions.definitions, {
              id: configuration.id
            })
    
            if (matchingConf || matchingConf.length == 1) {
              matchingConf = matchingConf[0];
              angular.extend(matchingConf, configuration);
              vm.form.caseDef = matchingConf;
              vm.form.progressBarType = $filter('filter')(  vm.ui.data.progressBar.types,{id:matchingConf.ui['progress-bar-type']})[0];
    
              angular.forEach(matchingConf.ui.additionalSettings,function pushAddSetting(value){ 
                vm.form.additionalSettings[value]=true;
    
              });
    
            } else {
              $log.error("No configuration found for case definition id [" + configuration.id + "]");
            }
    
    
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

            BpmService.progressBar.loadConfig().then(
                function (savedData) {
                  if (savedData) {
                    loadConfiguration(savedData);
                  }
                }
              )
        }

        init();


    }

    function BpmService($http, $q) {
        this.data = {
            caseDefinitions: getCaseDefinitions,
        }
        this.progressBar = {
            loadConfig: getSavedConfiguration
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


        function getSavedConfiguration(){
            var promise = $q(
                function loadCaseInstanceData(resolve, reject) {
                    resolve(savedConfiguration);
                });
            return promise;

            /*
            return $http.get('/mocks/savedConfiguration.json').then(
                function (res) {
                    return res.data;
                }
            )*/
        }
    }
});
