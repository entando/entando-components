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

var bootBpmComponent = (function ngApp(resourceUrl) {
	'use strict';

	angular.module('bpm-process-diagram-config', [ 'kie-commons' ])
			.directive('processConfig', ProcessConfig)
			.controller('ProcessConfigController', ProcessConfigController)
			.controller('ProcessConfigEntandoController',ProcessConfigEntandoController)
			.run(runBlock);
	
	
	
	function ProcessConfigEntandoController(){
		var vm = this;
		vm.config = {
				container:{},
				process:{},
				widget:{}
		};
	}

	function ProcessConfig() {
		return {
			restrict : 'E',
			scope : {},
			templateUrl : resourceUrl
					+ 'plugins/jpkiebpm/administration/fragments/process-diagram-config-tpl.html',
			controllerAs : 'vm',
			controller : 'ProcessConfigController',
			bindToController : {
				form : '=options'
			},
			replace : false,
			link: function link(){
				console.log(arguments)
			}
		};
	}

	function ProcessConfigController($scope, $rootScope, $log, $timeout,
			KieServerService) {
		var vm = this;

		vm.ui = {
			containers : function() {
				return vm.data.containers || [];
			},
			container : {
				select : function(selected) {
					vm.form.container = selected;
					loadProcessInstances();
				},
				isCurrentActive : function(tested) {
					return vm.form.container ? vm.form.container['container-id'] === tested['container-id']
							: false;
				},
				current : function() {
					return vm.form.container;
				}
			},
			processes : {
				instances : function() {
					return vm.data.processes || [];
				}
			},
			process : {
				select : function(selected) {
					vm.form.process = selected;
					//loadProcessUserTasks();
				},
				isCurrentActive : function(tested) {
					return vm.form.process ? vm.form.process["process-instance-id"] === tested["process-instance-id"]
							: false;
				},
				current : function() {
					return vm.form.process;
				},
				diagram : function() {
					return vm.data.images ? vm.data.images : "Loading";
				},
//				tasks : function() {
//					return vm.data.tasks ? vm.data.tasks : [];
//				},
//				task : {
//					select : function selectTask(newTask) {
//						vm.form.task = newTask;
//					},
//					current : function currentTask() {
//						return vm.form.task;
//					}
//				}
			}

		};
		
		vm.data = {};
		vm.mod = {};

		// init
		init();

		function init() {
			loadContainers();

		}

		function loadContainers() {
			KieServerService.core.server.container.list().then(
					function(containers) {
						vm.data.containers = containers;
					});
		}

		function loadProcessInstances() {
			KieServerService.core.server.container.process.instances(
					vm.form.container).then(function(processes) {
				vm.data.processes = processes;
			});
		}
//
//		function loadProcessUserTasks() {
//			KieServerService.core.server.container.process.task.user.list(
//					vm.form.container['container-id'],
//					vm.form.process['process-id']).then(function(tasks) {
//				vm.data.tasks = tasks;
//			});
//		}

	}
	
	
	function runBlock($rootScope, $log, $http) {
		$http.defaults.headers.common['Accept'] = 'application/json';
	}
});
