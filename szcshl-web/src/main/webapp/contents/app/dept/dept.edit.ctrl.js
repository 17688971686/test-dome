(function () {
    'use strict';

    angular.module('app').controller('deptEditCtrl', dept);

    dept.$inject = ['$location', 'deptSvc', '$state'];

    function dept($location, deptSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '编辑部门';
        vm.isuserExist = false;
        vm.deptId = $state.params.deptId;
        if (vm.deptId) {
            vm.isUpdate = true;
            vm.title = '更新部门';
        }

        vm.create = function () {
            deptSvc.createDept(vm);
        };
        vm.update = function () {
            deptSvc.updateDept(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                deptSvc.getDeptById(vm);
            }
        }
    }
})();
