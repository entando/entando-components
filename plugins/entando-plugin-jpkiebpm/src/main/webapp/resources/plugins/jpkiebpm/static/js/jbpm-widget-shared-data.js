(function WidgetSharedDataIIFE(initState) {
  'use strict';
  var sharedData = angular.extend({},initState);
  
  angular.module('widgets-shared-data', [])
    .service("SharedDataServices",SharedDataServices);
  
  
  function SharedDataServices() {
      this.getSharedData = function () {
        return sharedData;
      }
    }
})({shared:true})
