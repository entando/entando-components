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

var bootBpmProgressBarComponents = (function (appId) {
    'use strict';

    angular.module(appId, [])
            .directive('progressBar', progressBar)
            //.service('BpmService', BpmService)
            .controller('ProgressBarCtrl', ProgressBarCtrl);

    function progressBar() {
        return {
            restrict: 'E',
            scope: {},
            template: [
                '<ng-include src="vm.ui.pickTemplate()"/>'
            ].join(''),
            controllerAs: 'vm',
            controller: 'ProgressBarCtrl',
            bindToController: {
                options: '=',
                caseData: '='
            }
            //link: link
        };
    }




    function ProgressBarCtrl($filter, $log) {

        var vm = this;

        var templateMap = {
            basic: 'basic-tpl',
            stacked: 'stacked-tpl'
        }

        vm.ui = {
            milestonePercentage: calculateMilestonePercentage,
            milestoneComplete: isMilestoneComplete,
            totalCaseCompletedPercentage: totalCaseCompletedPercentage,
            milestoneCompletedStyles: milestoneCompletedStyles,
            countVisibleMilestones: countVisibleMilestones,
            filterVisibleMiletones: filterCurrentVisibleMilestones,
            countAchievedMilestones: countAchievedMilestones,
            instanceName: instanceName,
            showMilestonesLabels: showMilestonesLabels,
            showNumberOfTasks: showNumberOfTasks,
            pickTemplate: pickTemplateFromMap(templateMap)
        }

        //vm.options = \\ui options
        //vm.caseData = \\case instance data

        function showMilestonesLabels() {
            var out = false;
            if (vm.options)
                angular.forEach(vm.options.ui.additionalSettings, function (value) {
                    if ('show-milestones' === value) {
                        out = true;
                    }
                })
            return out;
        }

        function showNumberOfTasks() {
            var out = false;
            if (vm.options)
                angular.forEach(vm.options.ui.additionalSettings, function (value) {
                    if ('show-number-of-tasks' === value) {
                        out = true;
                    }
                })
            return out;
        }


        function pickTemplateFromMap(templateMap) {
            return function pickTemplate() {
                if (vm.options && vm.options.ui) {
                    return templateMap[vm.options.ui["progress-bar-type"]];
                }
                return templateMap.basic;
            }
        }

        function instanceName() {
            return vm.options ? vm.options.name : '';
        }

        function filterCurrentVisibleMilestones() {
            return filterVisibleMiletones(vm.options, vm.caseData);
        }

        function milestoneCompletedStyles() {
            return ['progress-bar-success'];
        }


        function totalCaseCompletedPercentage() {
            var count = 0;
            angular.forEach(filterVisibleMiletones(vm.options, vm.caseData), function (ms) {


                if (ms["milestone-achieved"]) {
                    count += ms.percentage;
                }
            });
            return count;
        }

        function countAchievedMilestones() {
            return filterAchievedMilestones(vm.options, vm.caseData).length;
        }

        function isMilestoneComplete(milestone) {
            if (milestone) {
                return milestone['milestone-achieved'];
            }

            return false;
        }

        function calculateMilestonePercentage(milestone) {
            return milestone.percentage;
        }

        function countVisibleMilestones() {
            return filterVisibleMiletones(vm.options, vm.caseData).length
        }

        function filterAchievedMilestones(options, caseData) {
            var msVisible = filterVisibleMiletones(options, caseData)

            return _filterMiletones(msVisible, {
                "milestone-achieved": true
            });
        }

        function filterVisibleMiletones(options, caseData) {
            var visibleMilestones = _filterMiletones(mergeMilestonesConfigAndData(options, caseData), {
                "visible": true
            });
            return visibleMilestones;
        }



        var old_options = undefined;
        var old_caseInstance = undefined;
        var merged = undefined;

        function mergeMilestonesConfigAndData(options, caseInstance) {

            if (!options || !caseInstance) {
                return [];
            }
            if (angular.equals(old_options, options) && angular.equals(old_caseInstance, caseInstance)) {
                return merged;
            }


            old_options = options;
            old_caseInstance = caseInstance;
            merged = [];
            angular.forEach(options.milestones, function extendConf(ms) {
                var found = $filter('filter')(caseInstance.milestones, {
                    "milestone-name": ms['milestone-name']
                })
                if (found.length === 1) {
                    merged.push(angular.extend({}, ms, found[0]));
                }
            });
            $log.info(merged)
            return merged;
        }

        function _filterMiletones(instance, filterMap) {
            return $filter('filter')(instance, filterMap);
        }

    }
})