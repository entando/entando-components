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

var bootBpmComponent = (function ngApp(serverList) {
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

    function CaseServersController($filter, $log,$sce, BpmService) {

        var vm = this;
        var formIDFix ={};
        vm.ui={
            getForm: function getFormID(ks){
                if(!formIDFix[ks.id]){
                    formIDFix[ks.id]=$sce.trustAsResourceUrl('srv-'+ks.id);
                }
                return formIDFix[ks.id];
            }
        }

        vm.data = {
            servers: []
        };

        //init function  
        function init() {
            BpmService.data.servers().then(function (servers) {

                vm.data.servers = servers;
            });


        }

        init();


    }

    function BpmService($http, $q) {
        this.data = {
            servers: getServersList,
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
    }
});
