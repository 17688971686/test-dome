(function () {
    'use strict';

    angular.module('app').controller('sysdeptCtrl', sysdept);

    sysdept.$inject = [ 'sysdeptSvc'];

    function sysdept(sysdeptSvc) {
        var vm = this;
        vm.model = {};
        vm.title = '部门小组列表';

        activate();
        function activate() {
            sysdeptSvc.listGrid(vm.gridOptions);
        }


    }//E_sysdept
})();
