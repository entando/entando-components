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

var bootBpmComponent = (function ngApp(serverList, allServerTest) {
    'use strict';
    angular.module('caseServersApp', [])
            .controller('CaseServersController', CaseServersController)
            .service("BpmService", BpmService)
            .filter('trusted', ['$sce', function ($sce) {
                    return function (url) {
                        return $sce.trustAsResourceUrl(url);
                    };
                }]);
    ;
    function CaseServersController($filter, $log, $sce, BpmService) {

        var vm = this;
        var formIDFix = {};
        vm.ui = {
            getForm: getFormID,
            connectionTest: connectionTest
        }

        vm.data = {
            servers: [],
            healtCheck: {}

        };

        function connectionTest(ks) {
            if (vm.data.healtCheck) {
                var hcData = $filter('filter')(vm.data.healtCheck, {"kie-server-id": ks.id}, true);

                return hcData.length === 1 ? hcData[0] : undefined;
            }
        }

        function getFormID(ks) {
            if (!formIDFix[ks.id]) {
                formIDFix[ks.id] = $sce.trustAsResourceUrl('srv-' + ks.id);
            }
            return formIDFix[ks.id];
        }

        //init function  
        function init() {
            BpmService.data.servers().then(function (servers) {
                vm.data.servers = servers;
            });

            BpmService.util.healthCheck().then(function (hc) {
                vm.data.healtCheck = hc;
            });
        }

        init();
    }

    function BpmService($http, $q) {

        this.data = {
            servers: getServersList
        }
        this.util = {
            healthCheck: execHealthCheck
        }

        function getServersList() {
            //Entando data injection
            var promise = $q(
                    function loadServersList(resolve, reject) {
                        resolve(serverList);
                    });
            return promise;
            /*
             return $http.get('/mocks/caseInstanceDef.json').then(
             function (res) {
             return res.data;
             }
             )*/
        }


        function execHealthCheck() {
            var promise = $q(
                    function healtCheck(resolve, reject) {
                        resolve(allServerTest);
                    });
            return promise;
        }
    }
});
