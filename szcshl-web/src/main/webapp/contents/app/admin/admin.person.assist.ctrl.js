(function () {
    'use strict';

    angular.module('app').controller('adminPersonAssistCtrl', personAssist);

    personAssist.$inject = ['$location', 'adminSvc', 'flowSvc','pauseProjectSvc'];

    function personAssist($location, adminSvc, flowSvc,pauseProjectSvc) {
        var vm = this;
        vm.title = '个人经办项目';
        vm.model = {};
        activate();
        function activate() {
            vm.showwin = false;
            adminSvc.personMainTasksGrid(vm);
        }

    }
})();
