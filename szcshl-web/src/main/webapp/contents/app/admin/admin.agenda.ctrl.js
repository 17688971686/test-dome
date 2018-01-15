(function () {
    'use strict';

    angular.module('app').controller('adminAgendaCtrl', adminAgenda);

    adminAgenda.$inject = ['$location','adminSvc'];

    function adminAgenda($location, adminSvc) {
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
            vm.gridOptions.dataSource._skip="";
            vm.gridOptions.dataSource.read();
        }
    }
})();

