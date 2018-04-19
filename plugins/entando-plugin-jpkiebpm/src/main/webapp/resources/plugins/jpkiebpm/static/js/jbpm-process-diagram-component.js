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

var bootProcessDiagramComponent = (function ngApp(appId, resourceUrl, savedConfig) {
	'use strict';

	angular.module(appId, [ 'ngSanitize','kie-commons' ])
			.directive('processDiagram',	ProcessDiagram)
			.controller('ProcessDiagramController',ProcessDiagramController)
			.controller('ProcessEntandoController', ProcessEntandoController)
			.run(runBlock);

	function ProcessEntandoController() {
		var vm = this;
		vm.config = savedConfig
	}

	function ProcessDiagram() {
		return {
			restrict : 'E',
			scope : {},
			templateUrl : resourceUrl
					+ 'plugins/jpkiebpm/static/fragments/process-diagram-tpl.html',
			controllerAs : 'vm',
			controller : 'ProcessDiagramController',
			bindToController : {
				form : '=options'

			}
		// link: link
		};
	}

	function ProcessDiagramController($scope,$rootScope, $log, KieServerService) {
	    var vm = this;


	    vm.ui = {
	      process: {
	        isCurrentActive: function (tested) {
	          return vm.form.process ? vm.form.process["process-instance-id"] === tested["process-instance-id"] : false;
	        },
	        diagram: function () {
	          return vm.data.images ? vm.data.images : "Select a process instance ...";
	        }
	      }


	    };
	    vm.form = vm.options;
	    vm.data = {};
	    vm.mod = {};

	    //init 
	    init();
	   


	    function init() {
	      $scope.$watch('vm.form.process', function (newProcess) {
	        if (newProcess) {
	          loadProcessInstancesImage();
	        }
	      })
	    }
	    $rootScope.$on('progress:refresh', function refres(){
	      loadProcessInstancesImage();
	    });


	    function loadProcessInstancesImage() {
	      KieServerService.core.server.container.process.image({
	        container: vm.form.container,
	        process: vm.form.process
	      }).then(function (svg) {
	        vm.data.images = svg;
	      });
	    }

	  }

	function runBlock($rootScope, $log, $http) {
		$http.defaults.headers.common['Accept'] = 'application/json';
	}
});
