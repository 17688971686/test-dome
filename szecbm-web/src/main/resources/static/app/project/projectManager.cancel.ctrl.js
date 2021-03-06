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

    projectManagerCancelCtrl.$inject = ["$scope","projectManagerSvc","bsWin","$state"];

    function projectManagerCancelCtrl($scope,projectManagerSvc,bsWin,$state) {
        $scope.csHide("cjgl");
        var vm = this;
        vm.model = {};
        vm.tableParams = {};

        //获取项目列表
        projectManagerSvc.bsTableCancelManagement(vm);

        vm.filterSearch = function(){
            $('#listTable').bootstrapTable('refresh');
        }

        //导出项目信息
        vm.expProinfo = function () {
            vm.status = '2';
            vm.tableParams.$filter =  util.buildOdataFilter("#toolbar",null);
            vm.tableParams.$orderby = "createdDate desc";
            projectManagerSvc.createProReport(vm);
        }

        vm.restore = function (pro) {
            projectManagerSvc.checkProPriUser(pro.id,function(data){
                if(data.flag){
                    vm.model = pro;
                    vm.model.status = '1';
                    bsWin.confirm("是否恢复项目", function () {
                        projectManagerSvc.restoreInvestProject(vm);
                    })
                }else{
                    bsWin.alert("您无权进行编辑操作！");
                }
            });
        }

        vm.delete = function (id) {
            projectManagerSvc.checkProPriUser(id,function(data){
                if(data.flag){
                    bsWin.confirm("是否删除项目，删除后数据不可恢复", function () {
                        projectManagerSvc.deleteGovernmentInvestProject(id);
                    })
                }else{
                    bsWin.alert("您无权进行编辑操作！");
                }
            });
        }
    }

})();