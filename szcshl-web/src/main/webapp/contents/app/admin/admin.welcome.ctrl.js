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

    adminWelCome.$inject = ['$location','adminSvc'];

    function adminWelCome($location, adminSvc) {
        var vm = this;
        vm.title = '主页';
        activate();
        function activate() {
            adminSvc.initWelComePage(function(data){
                console.log(data);
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
        //旧的初始化方法
        /*adminSvc.initAnnountment(vm);
         adminSvc.findtasks(vm);
         adminSvc.findendTasks(vm);
         adminSvc.findHomePluginFile(vm);*/
    }
})();
