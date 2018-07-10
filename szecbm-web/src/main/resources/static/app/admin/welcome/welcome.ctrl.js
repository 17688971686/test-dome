(function () {
    'use strict';

    angular.module('myApp').config(welcomeConfig);

    welcomeConfig.$inject = ["$stateProvider"];

    function welcomeConfig($stateProvider) {
        $stateProvider.state('welcome', {
            url: "/welcome",
            templateUrl: util.formatUrl('admin/welcome'),
            controllerAs: "vm",
            controller: welcomeCtrl
        });
    }

    welcomeCtrl.$inject = ["$scope", "operatorLogSvc"];

    function welcomeCtrl($scope, operatorLogSvc) {
        var vm = this;

        // 用户操作日志
        vm.getOperatorLogList = function () {
            operatorLogSvc.getOperatorLogList(vm, {
                "$orderby": "createdDate desc",
                "$top": 5
            });
        }

    }

})();