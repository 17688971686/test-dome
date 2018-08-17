(function () {
    'use strict';

    angular.module('app').controller('adminAgendaCtrl', adminAgenda);

    adminAgenda.$inject = ['bsWin','adminSvc','flowSvc'];

    function adminAgenda(bsWin, adminSvc,flowSvc) {
        var vm = this;
        vm.title = '待办任务';
        activate();
        function activate() {
            adminSvc.agendaTaskGrid(vm);
            adminSvc.workName(vm);
        }

        /**
         * 通过流程类别查找
         */
        vm.query = function(){
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
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

