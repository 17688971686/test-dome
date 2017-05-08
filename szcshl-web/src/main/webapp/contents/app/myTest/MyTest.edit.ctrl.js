(function () {
    'use strict';

    angular.module('app').controller('myTestEditCtrl', myTest);

    myTest.$inject = ['$location', 'myTestSvc', '$state'];

    function myTest($location, myTestSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加My test';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新My test';
        }

        vm.create = function () {
            myTestSvc.createMyTest(vm);
        };
        vm.update = function () {
            myTestSvc.updateMyTest(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                myTestSvc.getMyTestById(vm);
            }
        }
    }
})();
