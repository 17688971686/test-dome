(function () {
    'use strict';

    angular
        .module('app')
        .controller('mytestEditCtrl', mytest);

    mytest.$inject = ['$location', 'mytestSvc', '$state'];

    function mytest($location, mytestSvc, $state) {
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
            mytestSvc.createMytest(vm);
        };
        vm.update = function () {
            mytestSvc.updateMytest(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                mytestSvc.getMytestById(vm);
            }
        }
    }
})();
