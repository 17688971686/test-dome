(function () {
    'use strict';

    angular.module('app').controller('assistUnitUserEditCtrl', assistUnitUser);

    assistUnitUser.$inject = ['$location', 'assistUnitUserSvc', '$state'];

    function assistUnitUser($location, assistUnitUserSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加协审单位用户';
        vm.isUserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新协审单位用户';
        }

        vm.create = function () {
            assistUnitUserSvc.createAssistUnitUser(vm);
        };
        vm.update = function () {
            assistUnitUserSvc.updateAssistUnitUser(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                assistUnitUserSvc.getAssistUnitUserById(vm);
            }
            
            assistUnitUserSvc.getAssistUnit(vm);
        }
    }
})();
