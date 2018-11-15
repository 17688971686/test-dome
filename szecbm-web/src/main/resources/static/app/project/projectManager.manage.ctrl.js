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

    projectManagerCtrl.$inject = ["$state","projectManagerSvc","bsWin"];

    function projectManagerCtrl($state,projectManagerSvc,bsWin) {
        var vm = this;
        vm.model = {};
        vm.tableParams = {};
   /*     projectManagerSvc.findOrgUser(function(data){
            vm.principalUsers = data;
        });*/
        //获取项目列表
        projectManagerSvc.rsTableControl(vm);

        vm.filterSearch = function(){
            $('#listTable').bootstrapTable('refresh');
        }

        //导出项目信息
        vm.expProinfo = function () {
            vm.status = '1';
            vm.tableParams.$filter =  util.buildOdataFilter("#toolbar",null);
            vm.tableParams.$orderby = "createdDate desc";
           projectManagerSvc.createProReport(vm);

        }

        //作废项目
        vm.cancel = function (pro) {
            projectManagerSvc.checkProPriUser(pro.id,function(data){
                if(data.flag){
                    vm.model = pro;
                    vm.model.status = '2';
                    bsWin.confirm("是否作废项目", function () {
                        projectManagerSvc.cancelInvestProject(vm);
                    })
                }else{
                    bsWin.alert("您无权进行编辑操作！");
                }
            });
        }

        vm.editProj = function(projId){
            projectManagerSvc.checkProPriUser(projId,function(data){
                if(data.flag){
                    $state.go("projectManageEdit",{id:projId,flag:"edit"});
                }else{
                    bsWin.alert("您无权进行编辑操作！");
                }
            });
        }

    }
})();