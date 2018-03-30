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

var bootBpmChartsComponents = (function (appId, caseInstanceData) {
    'use strict';

    angular.module(appId, ['chart.js', 'ng.jsoneditor'])
            .controller('CaseMilestoneChartController', CaseMilestoneChartController)
            .service('ChartKieServerService', ChartKieServerService)

    function CaseMilestoneChartController($filter, $scope, ChartKieServerService) {
        var vm = this;

        vm.mod = {
            chartType: 'doughnut'
        }


        vm.ui = {
            getLabelText: function (ms) {
                if (ms["milestone-achieved"]) {
                    return "Completed";
                }
                return "In Progress";
            },
            getLabelClass: function (ms) {
                if (ms["milestone-achieved"]) {
                    return "label-primary";
                }
                return "label-warning";
            }
        }


        var chartPrototype = {
            labels: [],
            data: [],
            options: {
                responsive: true,
                legend: {
                    display: true,
                    position: 'left'
                },
                title: {
                    display: true,
                    text: ''
                },
                animation: {
                    animateScale: true,
                    animateRotate: true
                }
            }

        }
        //init 
        init();



        function init() {
            readDetails().then(function () {
                generateChartData();
                $scope.$watch('vm.mod.details', generateChartData);
            });
        }

        function generateChartData() {

            vm.mod.chart = angular.copy(chartPrototype);
            vm.mod.chart.labels.push('To Do');
            vm.mod.chart.labels.push('In Progress');
            vm.mod.chart.labels.push('Completed');

            vm.mod.chart.data.push(0);
            vm.mod.chart.data.push($filter('filter')(vm.mod.details, {
                "milestone-achieved": false
            }).length);
            vm.mod.chart.data.push($filter('filter')(vm.mod.details, {
                "milestone-achieved": true
            }).length);


            vm.mod.chart.colors = ['#B5B7CC', '#DEDDDE', '#1AB393'];



        }

        function readDetails() {
            return ChartKieServerService.case.details.read().then(function (res) {
                return vm.mod.details = res;
            });
        }
    }

    function ChartKieServerService($q) {

        this.case = {
            details: {
                read: function readRoles() {
                    var defer = $q.defer();

                    defer.resolve(caseInstanceData);
                    return defer.promise;
                }
            }
        };
    }

})