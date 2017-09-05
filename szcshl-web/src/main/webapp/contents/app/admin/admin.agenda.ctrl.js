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
        }
    }
})();

