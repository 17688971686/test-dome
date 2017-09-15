(function () {
    'use strict';

    angular.module('app').controller('assertStorageBusinessEditCtrl', assertStorageBusiness);

    assertStorageBusiness.$inject = ['$location', 'assertStorageBusinessSvc', '$state'];

    function assertStorageBusiness($location, assertStorageBusinessSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加固定资产申购流程';
        vm.isuserExist = false;
        vm.businessId = $state.params.businessId;
        if (vm.businessId) {
            vm.isUpdate = true;
            vm.title = '更新固定资产申购流程';
        }

        vm.create = function () {
            assertStorageBusinessSvc.createAssertStorageBusiness(vm);
        };
        vm.update = function () {
            assertStorageBusinessSvc.updateAssertStorageBusiness(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                assertStorageBusinessSvc.getAssertStorageBusinessById(vm);
            }
        }
    }
})();
