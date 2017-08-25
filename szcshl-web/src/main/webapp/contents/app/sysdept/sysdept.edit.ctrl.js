(function () {
    'use strict';

    angular.module('app').controller('sysdeptEditCtrl', sysdept);

    sysdept.$inject = [ 'sysdeptSvc'];

    function sysdept(sysdeptSvc) {
        var vm = this;
        vm.model = {};
        vm.title = '部门小组编辑';
        vm.model.id = $state.params.id;
        if (vm.model.id) {
            vm.isUpdate = true;
            vm.title = '更新部门';
        }


        activate();
        function activate() {
            if(vm.isUpdate){
                sysdeptSvc.findById(vm.model.id,function (data) {
                    vm.model = data;
                });
            }
        }


    }//E_sysdept
})();
