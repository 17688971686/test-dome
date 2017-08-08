(function () {
    'use strict';

    angular.module('app').factory('suppletterSvc', suppletter);

    suppletter.$inject = ['$rootScope', '$http'];

    function suppletter($rootScope, $http) {

        var service = {
        }
        return service;
    }
})();