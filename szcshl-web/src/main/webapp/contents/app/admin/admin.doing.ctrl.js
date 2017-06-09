(function () {
    'use strict';

    angular.module('app').controller('adminDoingCtrl', admin);

    admin.$inject = ['$location','adminSvc']; 

    function admin($location, adminSvc) {
        var vm = this;
        vm.title = '在办任务';
             
        activate();
        function activate() {
        	adminSvc.dtasksGrid(vm);
        }
    }
})();
