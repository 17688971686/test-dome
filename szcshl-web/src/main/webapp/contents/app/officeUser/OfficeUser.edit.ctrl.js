(function () {
    'use strict';

    angular.module('app').controller('officeUserEditCtrl', officeUser);

    officeUser.$inject = ['$location', 'officeUserSvc', '$state'];

    function officeUser($location, officeUserSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加人员';
        vm.isuserExist = false;
        vm.officeID = $state.params.officeID;
        if (vm.officeID) {
            vm.isUpdate = true;
            vm.title = '更新人员';
        }

        vm.create = function () {
            officeUserSvc.createOfficeUser(vm);
        };
        vm.update = function () {
            officeUserSvc.updateOfficeUser(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                officeUserSvc.getOfficeUserById(vm);
            }
            officeUserSvc.getDepts(vm);//获取所有办事处
           
        }
    }
})();
