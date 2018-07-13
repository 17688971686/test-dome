(function () {
    'use strict';

    angular.module('myApp').config(proCancelConfig);

    proCancelConfig.$inject = ["$stateProvider"];

    function proCancelConfig($stateProvider) {
        //项目管理列表页
        $stateProvider.state('projectManageCancel', {
            url: "/projectManageCancel",
            controllerAs: "vm",
            templateUrl: util.formatUrl('project/html/cancel'),
            controller: projectManagerCancelCtrl
        });
    }

    projectManagerCancelCtrl.$inject = ["$scope","projectManagerSvc"];

    function projectManagerCancelCtrl($scope,projectManagerSvc) {
        $scope.csHide("cjgl");
        var vm = this;
        //获取项目列表
        projectManagerSvc.bsTableCancelManagement(vm);
    }

})();