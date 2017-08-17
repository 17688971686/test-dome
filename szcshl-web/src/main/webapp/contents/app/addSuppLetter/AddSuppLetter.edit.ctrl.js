(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterEditCtrl', addSuppLetter);

    addSuppLetter.$inject = ['$location', 'addSuppLetterSvc', '$state'];

    function addSuppLetter($location, addSuppLetterSvc, $state) {
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
            addSuppLetterSvc.createAddSuppLetter(vm);
        };
        vm.update = function () {
            addSuppLetterSvc.updateAddSuppLetter(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                addSuppLetterSvc.getAddSuppLetterById(vm);
            }
        }
    }
})();
