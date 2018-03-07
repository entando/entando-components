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

var bootBpmComponents = (function (caseInstanceMilestones) {
    'use strict';

    angular.module('caseProgressApp',[])
        .service('BpmService', function ($http, $q) {
            this.data = {
                //caseInstanceDef: getCaseInstanceDef,
                caseInstanceData: getCaseInstanceData
            }


            /*  function getCaseInstanceDef() {
                 return $http.get('/mocks/caseInstanceDef.json').then(
                     function (res) {
                         return res.data;
                     }
                 )
             } */
            function getCaseInstanceData() {

                //Entando data injection
                var promise = $q(
                    function loadCaseInstanceData(resolve, reject) {
                        resolve(
                            {
                                "caseInstance": {
                                    "name": "FIXME - IT000001",

                                    "milestones": caseInstanceMilestones
                                }
                            });
                    });
                return promise;


                //Simulating a JSON/HTTP api
                /*
                return $http.get('/mocks/caseInstanceData.json').then(
                    function (res) {
                        return res.data;
                    }
                )*/
            }


        });

        angular.module('caseProgressApp')
            .controller('ProgressBarCtrl', function ($filter, $log, BpmService) {

                var vm = this;

                vm.data = {
                    caseInstance: undefined
                }

                vm.ui = {
                    milestonePercentage: calculateMilestonePercentage,
                    milestoneComplete: isMilestoneComplete,
                    totalCaseCompletedPercentage: totalCaseCompletedPercentage,
                    milestoneCompletedStyles: milestoneCompletedStyles,
                    countVisibleMilestones: countVisibleMilestones,
                    filterVisibleMiletones: filterCurrentVisibleMilestones,
                    filterAchievedMilestones: countAchievedMilestones,
                    instanceName: instanceName
                }


                function instanceName() {
                    return vm.data.caseInstance ? vm.data.caseInstance.name : '';
                }

                function filterCurrentVisibleMilestones() {
                    return filterVisibleMiletones(vm.data.caseInstance);
                }

                function milestoneCompletedStyles() {
                    return ['progress-bar-success'];
                }


                function totalCaseCompletedPercentage() {
                    var count = 0;
                    angular.forEach(filterVisibleMiletones(vm.data.caseInstance), function (ms) {
                        if (ms["milestone-achieved"]) {
                            count += ms.percentage;
                          }
                    });
                    return count;
                }

                function countAchievedMilestones() {
                    return filterAchievedMilestones(vm.data.caseInstance);
                }

                function isMilestoneComplete(milestone) {
                    return milestone['milestone-achieved'];
                }

                function calculateMilestonePercentage(milestone) {
                    return milestone.percentage;
                }

                function countVisibleMilestones() {
                    var found = filterVisibleMiletones(vm.data.caseInstance)
                    return found.length
                }

                function filterCurrentVisibleMilestones() {
                    return filterVisibleMiletones(vm.data.caseInstance);
                }

                function filterAchievedMilestones(caseInstance) {
                    return caseInstance ? filterMiletones(filterVisibleMiletones(caseInstance), { "milestone-achieved": true }) : [];
                }
                function filterVisibleMiletones(caseInstance) {
                    return caseInstance ? filterMiletones(caseInstance.milestones, { "visible": true }) : [];
                }

                function filterMiletones(instance, filterMap) {
                    return $filter('filter')(instance, filterMap);
                }
                function init() {
                    /*BpmService.data.caseInstanceDef().then(function (caseDef) {
                      vm.data.caseDef = caseDef;
                      vm.data.milestonesDef = caseDef.definitions[0].milestones;
                    });
                    */

                    BpmService.data.caseInstanceData().then(function (res) {
                        vm.data.caseInstance = res.caseInstance;
                    });
                }

                init();


            });
})

