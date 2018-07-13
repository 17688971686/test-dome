(function () {
    'use strict';

    angular.module('myApp').config(projectConfig);

    projectConfig.$inject = ["$stateProvider"];

    function projectConfig($stateProvider) {
        //项目管理列表页
        $stateProvider.state('projectManage', {
            url: "/projectManage",
            controllerAs: "vm",
            templateUrl: util.formatUrl('project/html/list'),
            controller: projectManagerCtrl
        });
    }

    projectManagerCtrl.$inject = ["$scope","projectManagerSvc"];

    function projectManagerCtrl($scope,projectManagerSvc) {
        var vm = this;
        //获取项目列表
        projectManagerSvc.bsTableControlForManagement(vm);
    }

})();