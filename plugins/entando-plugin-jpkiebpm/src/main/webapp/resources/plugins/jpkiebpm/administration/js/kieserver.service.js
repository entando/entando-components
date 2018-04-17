var kieCommons = (function kieCommons(baseUrlSrv) {
  'use strict';

  angular.module('kie-commons',[])
    .service('KieServerService', KieServerService);

  function KieServerService($http, $q, $log, $sce) {

    this.case = {
      comments: {
        get: getComments,
        post: postComments,
        delete: deleteComments
      }
    }

    this.core = {
      server: {
        container: {
          list: coreServerContainerList,
          process: {
            instances: coreServerContainerProcessInstances,
            instance: {
              signal: {
                list: coreServerContainerProcessInstanceSignals,
                send: coreServerContainerProcessInstanceSignalSend
              }
            },
            image: coreServerContainerProcessImages,
            task: {
              user: {
                list: coreServerContainerProcessUserTasks
              }
            }



          }
        }
      }
    }



    function coreServerContainerProcessUserTasks(containerId, pId, options) {
      if (!containerId || !pId) {
        var errDesc = "Missing required arguments [container-id,'process-instance-id'] = [" + containerId + "," + pId + "]";
        $log.error(errDesc);
        var errPromise = $q.defer();
        return errPromise.reject(errDesc)
      }
      options = options ? options : {};

      /*
		 * server/containers/{id}/processes/definitions/{pId}/tasks/users
		 * Retrieves user tasks definitions that are present in given process
		 * and container
		 */

  	var baseUrl = baseUrlSrv + "kie-server/services/rest"
      return $http.get(baseUrl + '/server/containers/' + containerId + "/processes/definitions/" + pId + "/tasks/users")
        .then(
          function success(response) {
            return response.data.task;
          },
          function (err) {
            return $q.reject(err);
          }
        );
    }


    function coreServerContainerProcessInstanceSignals(containerId, instanceId, options) {
      if (!containerId || !instanceId) {
        var errDesc = "Missing required arguments [container-id,'process-instance-id'] = [" + containerId + "," + instanceId + "]";
        $log.error(errDesc);
        var errPromise = $q.defer();
        return errPromise.reject(errDesc)
      }

      options = options ? options : {};

      // curl -X GET
		// "http://localhost:8080/kie-server/services/restserver/containers/{containerId}/processes/instances/{processInstanceId}/signals"
		// -H "accept: application/json"
  	var baseUrl = baseUrlSrv + "kie-server/services/rest"
      return $http.get(baseUrl + '/server/containers/' + containerId + "/processes/instances/" + instanceId + "/signals")
        .then(
          function success(response) {
            return response.data;
          },
          function (err) {
            return $q.reject(err);
          }
        );
    }

    function coreServerContainerProcessInstanceSignalSend(containerId, instanceId, signal, value, options) {
      if (!containerId || !instanceId || !signal) {
        var errDesc = "Missing required arguments [container-id,'process-instance-id'] = [" + containerId + "," + instanceId + "]";
        $log.error(errDesc);
        var errPromise = $q.defer();
        return errPromise.reject(errDesc)
      }

      options = options ? options : {};

      // curl -X POST
		// "http://localhost:8080/kie-server/services/restserver/containers/{containerId}/processes/instances/signal/{signal
		// name}?instanceId={instanceId}" -H "accept: application/json" -H
		// "content-type: application/json" -d "\"{data value string}\""
  	var baseUrl = baseUrlSrv + "kie-server/services/rest"
      return $http.post(baseUrl + '/server/containers/' + containerId + "/processes/instances/" + instanceId + "/signal/" + signal, value || {}, {
          headers: {
            "Content-Type": "application/json"
          }
        })
        .then(
          function success(response) {
            return response.data;
          },
          function (err) {
            return $q.reject(err);
          }
        );
    }

    function coreServerContainerProcessImages(options) {
      if (options && !('container' in options) && ('process' in options)) {
        var errDesc = "Missing required arguments [container-id,'process-instance-id'] = [" + options.container['container-id'] + "," + options.progress['process-instance-id'] + "]";
        $log.error(errDesc);
        var errPromise = $q.defer();
        return errPromise.reject(errDesc)
      }
  	var baseUrl = baseUrlSrv + "kie-server/services/rest"
      return $http.get(baseUrl + '/server/containers/' + options.container['container-id'] + "/images/processes/instances/" + options.process['process-instance-id'], {
        headers: {
          Accept: 'application/svg+xml'
        }
      }).then(
        function success(response) {
          return $sce.trustAsHtml(responsiveFix(response.data));
        },
        function (err) {
          return $q.reject(err);
        }
      );
    }

    function responsiveFix(svgString) {
      var parser = new DOMParser();
      var svg = parser.parseFromString(svgString, "text/xml").children[0];
      var h = svg.getAttribute("height")
      var w = svg.getAttribute("width")
      svg.removeAttribute("height")
      svg.removeAttribute("width")
      svg.setAttribute("viewBox", "0 0 " + w + " " + h);

      return svg.outerHTML;
    }



    function coreServerContainerProcessInstances(options) {
      if (options && !('container-id' in options)) {
        var errDesc = "Missing required arguments [container-id] = [" + options['container-id'] + "]";
        $log.error(errDesc);
        var errPromise = $q.defer();
        return errPromise.reject(errDesc)
      }
  	var baseUrl = baseUrlSrv + "kie-server/services/rest"
      return $http.get(baseUrl + '/server/containers/' + options['container-id'] + "/processes/instances/").then(
        function success(response) {
          return response.data['process-instance'];
        },
        function (err) {
          return $q.reject(err);
        }
      );
    }

    function coreServerContainerList(options) {
  
    	var baseUrl = baseUrlSrv + "kie-server/services/rest"
      return $http.get(baseUrl + '/server/containers').then(
        function success(response) {
          return response.data.result['kie-containers']['kie-container'];
        },
        function (err) {
          return $q.reject(err);
        }
      );
    }





    function deleteComments(commentId, options) {
      if (!options.caseId || !options.containerId || !commentId) {
        var errDesc = "Missing required arguments [id,caseId] = [" + options.containerId + "," + options.caseId + "]";
        $log.error(errDesc);
        var errPromise = $q.defer();
        return errPromise.reject(errDesc)
      }
  	var baseUrl = baseUrlSrv + "kie-server/services/rest"
      return $http.delete(baseUrl + '/server/containers/' + options.containerId + "/cases/instances/" + options.caseId + "/comments/" + commentId).then(
        function success(response) {
          return response.data;
        },
        function (err) {
          return $q.reject(err);
        }
      );

    }


    function postComments(msg, options) {
      if (!options.caseId || !options.containerId || !msg) {
        var errDesc = "Missing required arguments [id,caseId] = [" + options.containerId + "," + options.caseId + "]";
        $log.error(errDesc);
        var errPromise = $q.defer();
        return errPromise.reject(errDesc)
      }
  	var baseUrl = baseUrlSrv + "kie-server/services/rest"
      return $http.post(baseUrl + '/server/containers/' + options.containerId + "/cases/instances/" + options.caseId + "/comments", "\"" + msg + "\"").then(
        function success(response) {
          return response.data;
        },
        function (err) {
          return $q.reject(err);
        }
      );

    }

    function getComments(options) {
      if (!options.caseId || !options.containerId) {
        var errDesc = "Missing required arguments [id,caseId] = [" + options.containerId + "," + options.caseId + "]";
        $log.error(errDesc);
        var errPromise = $q.defer();
        return errPromise.reject(errDesc)
      }
  	var baseUrl = baseUrlSrv + "kie-server/services/rest"
      return $http.get(baseUrl + '/server/containers/' + options.containerId + "/cases/instances/" + options.caseId + "/comments?page=0&pageSize=100").then(
        function success(response) {
          return response.data.comments;
        },
        function (err) {
          return $q.reject(err);
        }
      );
    }


  }
})
