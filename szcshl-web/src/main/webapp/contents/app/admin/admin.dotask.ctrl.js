(function () {
    'use strict';

    angular.module('app').controller('adminDoTaskCtrl', allDoTask);

    allDoTask.$inject = ['adminSvc'];

    function allDoTask(adminSvc) {
        var vm = this;
        vm.title = '在办任务';

        activate();
        function activate() {
            adminSvc.doingTaskGrid(vm);
        }

    }
})();
