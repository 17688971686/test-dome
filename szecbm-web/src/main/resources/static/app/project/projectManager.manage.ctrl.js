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

    projectManagerCtrl.$inject = ["$scope","projectManagerSvc","bsWin"];

    function projectManagerCtrl($scope,projectManagerSvc,bsWin) {
        var vm = this;
        vm.model = {};
        vm.tableParams = {};
   /*     projectManagerSvc.findOrgUser(function(data){
            vm.principalUsers = data;

        });*/

        //获取项目列表
        projectManagerSvc.rsTableControl(vm);



        //导出项目信息
        vm.expProinfo = function () {
            vm.status = '1';
            vm.tableParams.$filter =  util.buildOdataFilter("#toolbar",null);
            vm.tableParams.$orderby = "createdDate desc";
           projectManagerSvc.createProReport(vm);

        }

        //作废项目
        vm.cancel = function (pro) {
            vm.model = pro;
            vm.model.status = '2';
            bsWin.confirm("是否作废项目", function () {
                projectManagerSvc.cancelInvestProject(vm);
            })
        }
    }

})();