(function () {
    'use strict';

    angular.module('app').controller('monthlyHistoryEditCtrl', monthlyHistory);

    monthlyHistory.$inject = ['$location', 'monthlyHistorySvc', '$state'];

    function monthlyHistory($location, monthlyHistorySvc, $state) {
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
            monthlyHistorySvc.createmonthlyHistory(vm);
        };
        vm.update = function () {
            monthlyHistorySvc.updatemonthlyHistory(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                monthlyHistorySvc.getmonthlyHistoryById(vm);
            }
        }
    }
})();
