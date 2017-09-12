(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiyearEditCtrl', monthlyMultiyear);

    monthlyMultiyear.$inject = ['$location', 'monthlyMultiyearSvc', '$state'];

    function monthlyMultiyear($location, monthlyMultiyearSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加月报简报历史数据';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新月报简报';
        }

        vm.createHistory = function () {
            monthlyMultiyearSvc.createmonthlyMultiyear(vm);
        };
        vm.update = function () {
            monthlyMultiyearSvc.updatemonthlyMultiyear(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                monthlyMultiyearSvc.getmonthlyMultiyearById(vm);
            }
        }
    }
})();
