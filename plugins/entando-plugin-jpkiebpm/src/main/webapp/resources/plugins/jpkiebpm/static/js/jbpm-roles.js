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

var bootBpmRolesComponents = (function (appId,rolesData) {
    'use strict';

    angular.module(appId, [])
            .controller('RolesController', RolesController)
            .service('RolesKieServerService', RolesKieServerService)
            .filter('rolesFilter', rolesFilter);

    function RolesKieServerService($q) {

        this.case = {
            roles: {
                read: function readRoles() {
                    var defer = $q.defer();

                    defer.resolve(rolesData);
                    return defer.promise;
                }
            }
        }
    }

    function RolesController($log, RolesKieServerService) {
        var vm = this;
        vm.ui = {
            rolesFilter: 'all',
            setFilter: function(value){
                vm.ui.rolesFilter=value;
            },
            getUser: function(role){
                if(role.users){
                    return role.users.join(" ")
                }
                return "";
            },
            getGroup :  function(role){
                if(role.groups){
                    return role.groups.join(" ")
                }
                return "";
            }
        };

        vm.mod = {
            roles: undefined
        }

        //init 
        init();



        function init() {
            readRoles();
        }

        function readRoles() {
            RolesKieServerService.case.roles.read().then(function (res) {
                vm.mod.roles = res["role-assignments"];
            });
        }

    }



    function rolesFilter() {

        return function (input, opts) {

            if (!opts || "all" === opts) {
                return input;
            }

            var out = [];
            if ("unassigned" === opts) {
                angular.forEach(input, function (value) {
                    if ((!value.users || value.users.length === 0) && (!value.groups || value.groups.length === 0)) {
                        out.push(value)
                    }
                })
            }
            if ("assigned" === opts) {
                angular.forEach(input, function (value) {
                    if ((value.users && value.users.length > 0) || (value.groups && value.groups.length > 0)) {
                        out.push(value)
                    }
                })
            }
            return out;

        }

    }

})