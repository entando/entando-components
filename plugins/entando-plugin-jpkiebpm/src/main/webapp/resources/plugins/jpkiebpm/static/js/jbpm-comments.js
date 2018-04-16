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

var bootBpmCommentsComponents = (function (appId,comments) {
    'use strict';

    angular.module(appId, [])
            .controller('CommentsController', CommentsController)
            .service('KieServerService', KieServerService);

    function CommentsController($log, KieServerService) {
        var vm = this;


        vm.ui = {
            minsAgo: function (comment) {

                var seconds = (((new Date).getTime()) - comment['added-at']['java.util.Date']) / 1000;
                if (seconds < 59) {
                    return Math.floor(seconds) + " minutes ago";
                }
                var minutes = seconds / 60;
                if (minutes < 59) {
                    return Math.floor(minutes) + " minutes ago";
                }
                var hours = minutes / 60;
                if (hours < 24) {
                    return Math.floor(hours) + " hours ago";
                }

                var days = hours / 24;

                return Math.floor(days) + " days ago";
            }
        };

        vm.mod = {
            comments: undefined
        };
        
        //init 
        init();



        function init() {
            getComments();
        }





        function getComments() {
            $log.info("Calling KIE server");
            KieServerService.case.comments.get(vm.mod.case).then(
                    function success(response) {
                        $log.info(response)
                        vm.mod.comments = response;

                    },
                    function error(err) {
                        $log.error(err);
                    }
            )


        }
    }
    function KieServerService($q) {

        this.case = {
            comments: {
                get: getComments
            }
        }


       
        function getComments(options) {
            var deferred = $q.defer();
            deferred.resolve(comments);
            return deferred.promise;
        }


    }
    
})