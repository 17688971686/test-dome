(function () {
    'use strict';

    angular.module('app').controller('adminDoTaskCtrl', allDoTask);

    allDoTask.$inject = ['adminSvc'];

    function allDoTask(adminSvc) {
        var vm = this;
        vm.title = '在办任务';
           //查询
        vm.query=function () {
            vm.gridOptions.dataSource.read();
        }

        activate();
        function activate() {
            adminSvc.doingTaskGrid(vm);
            adminSvc.workName(vm);
        }
    }
})();
