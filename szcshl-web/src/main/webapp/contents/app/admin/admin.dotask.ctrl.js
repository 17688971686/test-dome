(function () {
    'use strict';

    angular.module('app').controller('adminDoTaskCtrl', allDoTask);

    allDoTask.$inject = ['adminSvc','$state','$rootScope','bsWin','flowSvc'];

    function allDoTask(adminSvc,$state,$rootScope,bsWin,flowSvc) {
        var vm = this;
        vm.title = '在办任务';
        vm.isSuperUser = isSuperUser;
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.gridOptions.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});
        }
           //查询
        vm.query=function () {
            vm.gridOptions.dataSource._skip="";
            vm.gridOptions.dataSource.read();
        }

        activate();
        function activate() {
            if($rootScope.view[vm.stateName]){
                var preView = $rootScope.view[vm.stateName];
                //恢复grid
                if(preView.gridParams){
                    vm.gridParams = preView.gridParams;
                }
                //恢复表单参数
                if(preView.data){
                    vm.model = preView.data.model;
                }
                //恢复数据
                vm.project = preView.data.project;
                //恢复页数页码
                if(preView.queryParams){
                    vm.queryParams=preView.queryParams;
                }
                adminSvc.doingTaskGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                adminSvc.doingTaskGrid(vm);
            }

            adminSvc.workName(vm);
        }

        /**
         * 删除流程，
         * @param processInstanId
         */
        vm.deleteTask = function(processInstanId){
            if(processInstanId){
                bsWin.confirm("是否删除任务，删除数据不可恢复，操作请慎重！", function () {
                    flowSvc.deleteFlow(processInstanId,"删除任务",function(data){
                        if(data.flag || data.reCode == 'ok'){
                            bsWin.alert("操作成功！",function(){
                                vm.gridOptions.dataSource.read();
                            });
                        }else{
                            bsWin.alert(data.reMsg);
                        }
                    });
                })
            }else{
                bsWin.alert("该流程已删除！",function(){
                    activate();
                });
            }
        }
    }
})();
