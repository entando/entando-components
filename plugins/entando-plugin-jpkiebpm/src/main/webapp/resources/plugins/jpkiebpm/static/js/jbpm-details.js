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

var bootBpmDetailsComponents = (function (appId, detailsData) {
    'use strict';

    angular.module(appId, [])
            .controller('CaseDetailsController', CaseDetailsController)
            .service('DetailsKieServerService', DetailsKieServerService);

    function DetailsKieServerService($q) {
        this.case = {
            details: {
                read: function readRoles() {
                    var defer = $q.defer();

                    defer.resolve(detailsData);
                    return defer.promise;
                }
            }
        }
    }

    function CaseDetailsController($log, DetailsKieServerService) {
        var vm = this;
        vm.ui = {

        };

        vm.mod = {
            details: undefined,
            partecipants: undefined
        }



        function generatePartecipants() {
            vm.mod.partecipants = {};
            angular.forEach(vm.mod.details['case-roles'], function (value) {
                angular.forEach(value.users, function (usr) {
                    vm.mod.partecipants[usr] = {'fa': 'fa-user'};
                });
                angular.forEach(value.groups, function (group) {
                    vm.mod.partecipants[group] = {'fa': 'fa-users'};
                });
            })

        }


        //init 
        init();



        function init() {
            readDetails().then(function () {
                generatePartecipants();
            });
        }

        function readDetails() {
            return DetailsKieServerService.case.details.read().then(function (res) {
                return  vm.mod.details = res;
            });
        }

    }
})