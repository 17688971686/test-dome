(function () {
    'use strict';

    angular.module('app').controller('addRegisterFileEditCtrl', addRegisterFile);

    addRegisterFile.$inject = ['$location', 'addRegisterFileSvc', '$state'];

    function addRegisterFile($location, addRegisterFileSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加登记补充资料';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新登记补充资料';
        }

        vm.create = function () {
            addRegisterFileSvc.createAddRegisterFile(vm);
        };
        vm.update = function () {
            addRegisterFileSvc.updateAddRegisterFile(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                addRegisterFileSvc.getAddRegisterFileById(vm);
            }
        }
    }
})();
