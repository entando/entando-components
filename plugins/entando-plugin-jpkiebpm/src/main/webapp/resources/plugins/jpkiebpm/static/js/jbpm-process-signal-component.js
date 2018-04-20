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

var bootSignalComponent = (function ngApp(appId, resourceUrl, savedConfig) {
	'use strict';

	angular.module(appId, [ 'kie-commons', 'widgets-shared-data' ]).directive(
			'processSignal', ProcessSignal).controller(
			'ProcessSignalController', ProcessSignalController).controller(
			'ProcessEntandoController', ProcessEntandoController).run(runBlock);

	function ProcessEntandoController(SharedDataServices) {
		var vm = this;
		vm.config = SharedDataServices.getSharedData();
		for ( var attrname in savedConfig) {
			if (!vm.config[attrname]) {
				vm.config[attrname] = savedConfig[attrname];
			}
		}
	}

	function ProcessSignal() {
		return {
			restrict : 'E',
			scope : {},
			templateUrl : resourceUrl
					+ 'plugins/jpkiebpm/static/fragments/process-signal-tpl.html',
			controllerAs : 'vm',
			controller : 'ProcessSignalController',
			bindToController : {
				form : '=options'

			}
		// link: link
		};
	}

	function ProcessSignalController($scope, $rootScope, $log, $timeout,
			KieServerService,$interval) {
		var vm = this;

		vm.ui = {
			process : {
				isCurrentActive : function(tested) {
					return vm.form.process ? vm.form.process["process-instance-id"] === tested["process-instance-id"]
							: false;
				},
				signals : function() {
					return vm.data.signals ? vm.data.signals : [];
				},
				signal : {
					send : function() {
						sendSignal(vm.mod.actualSignal.name,
								vm.mod.actualSignal.value)
					},
					set : function(signal) {
						vm.mod.actualSignal.name = signal;
					}
				}
			}

		};
		
		vm.data = {
			title : "Change the title",
			description : 'Description goes here!'
		};
		vm.mod = {
			actualSignal : {}
		};

		// init
		init();

		function init() {
			$scope.$watch('vm.form', function(newProcess) {
				loadProcessSignals()
			});

			var lastProcessId = -1;
			$interval(function(){

				if(lastProcessId != vm.form.process['process-instance-id'] ){
					loadProcessSignals()
					lastProcessId = vm.form.process['process-instance-id'] 
				}
				
			},100)
			
			
			$rootScope.$on('progress:refresh', function refres() {
				loadProcessSignals();
			});

		}

		function sendSignal(signal, value) {
			KieServerService.core.server.container.process.instance.signal
					.send(vm.form.container['container-id'],
							vm.form.process['process-instance-id'], signal,
							value).then(function(res) {
						$rootScope.$emit('progress:refresh', "signal sent");
					})
		}

		function loadProcessSignals() {
			if (vm.form.process && Object.keys(vm.form.process).length > 0) {
				KieServerService.core.server.container.process.instance.signal
						.list(vm.form.container['container-id'],
								vm.form.process['process-instance-id']).then(
								function(signals) {
									vm.data.signals = signals;
								});
			}
		}

	}

	function runBlock($rootScope, $log, $http) {
		$http.defaults.headers.common['Accept'] = 'application/json';
	}
});
