(function () {
    'use strict';

    angular.module('app').controller('adminEndCtrl', admin);

    admin.$inject = ['$location','adminSvc']; 

    function admin($location, adminSvc) {
        var vm = this;
        vm.title = '办结事项';
             
        activate();
        function activate() {
        	adminSvc.etasksGrid(vm);
        }
    }
})();
