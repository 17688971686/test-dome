(function () {
    'use strict';

    angular.module('app').controller('adminWelComeCtrl', adminWelCome).filter('FormatStrDate', function() {
        return function(input) {
            var date = new Date(input);
            var monthValue = (date.getMonth()+1) < 10 ?"0"+(date.getMonth()+1):(date.getMonth()+1);
            var dayValue = (date.getDate()) < 10 ?"0"+(date.getDate()):(date.getDate());
            var formatDate=date.getFullYear()+"/"+monthValue+"/"+dayValue;
            return formatDate
        }
    });

    adminWelCome.$inject = ['bsWin','adminSvc'];

    function adminWelCome(bsWin, adminSvc) {
        var vm = this;
        vm.title = '主页';
        activate();
        function activate() {
            adminSvc.initWelComePage(function(data){
                if(data){
                    if(data.proTaskList){
                        vm.tasksList = data.proTaskList;
                    }
                    if(data.comTaskList){
                        vm.agendaTaskList = data.comTaskList;
                    }
                    if(data.endTaskList){
                        vm.endTasksList = data.endTaskList;
                    }
                    if(data.annountmentList){
                        vm.annountmentList = data.annountmentList;
                    }
                }
            });
        }

        vm.testAlert = function(){
            bsWin.confirm({
                title: "询问提示",
                message: "该项目已经关联其他合并评审会关联，您确定要改为单个评审吗？",
                onOk: function () {
                    alert("点击确认！");
                },
                onCancel: function () {
                    alert("点击取消！");
                }
            });
        }
    }
})();
