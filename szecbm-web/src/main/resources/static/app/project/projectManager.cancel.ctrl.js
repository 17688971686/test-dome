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

    projectManagerCancelCtrl.$inject = ["$scope","projectManagerSvc","bsWin"];

    function projectManagerCancelCtrl($scope,projectManagerSvc,bsWin) {
        $scope.csHide("cjgl");
        var vm = this;
        vm.model = {};
        vm.tableParams = {};
        //获取项目列表
        projectManagerSvc.bsTableCancelManagement(vm);

        //导出项目信息
        vm.expProinfo = function () {
            vm.status = '2';
            projectManagerSvc.createProReport(vm);
        }

        vm.restore = function (pro) {
            vm.model = pro;
            vm.model.status = '1';
            bsWin.confirm("是否恢复项目", function () {
                projectManagerSvc.restoreInvestProject(vm);
            })
        }

        vm.delete = function (id) {
            bsWin.confirm("是否删除项目，删除后数据不可恢复", function () {
                projectManagerSvc.deleteGovernmentInvestProject(id);
            })
        }
    }

})();