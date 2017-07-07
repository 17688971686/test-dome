(function () {
    'use strict';

    angular.module('app').controller('adminCtrl', admin);

    admin.$inject = ['$location','adminSvc']; 

    function admin($location, adminSvc) {
        var vm = this;
        vm.title = '待办事项';
             
        activate();
        function activate() {
        	adminSvc.gtasksGrid(vm);
        	adminSvc.initAnnountment(vm);
        }
        vm.countWorkday=function(){
        	adminSvc.countWorakday(vm);
        }
    }
})();
