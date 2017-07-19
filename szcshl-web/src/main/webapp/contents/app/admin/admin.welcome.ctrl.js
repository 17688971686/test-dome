(function () {
    'use strict';

    angular.module('app').controller('adminWelComeCtrl', adminWelCome);

    adminWelCome.$inject = ['$location','adminSvc'];

    function adminWelCome($location, adminSvc) {
        var vm = this;
        vm.title = '主页';
        activate();
        function activate() {
        	adminSvc.initAnnountment(vm);
            adminSvc.findtasks(vm);
            adminSvc.findendTasks(vm);
            adminSvc.findHomePluginFile(vm);
        }
    }
})();
