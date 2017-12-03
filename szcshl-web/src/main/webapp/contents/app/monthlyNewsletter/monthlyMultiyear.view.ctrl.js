(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiyViewCtrl', monthlyMultiyView);

    monthlyMultiyView.$inject = ['monthlyMultiyearSvc', 'sysfileSvc', '$state', 'bsWin', '$scope'];

    function monthlyMultiyView(monthlyMultiyearSvc, sysfileSvc, $state, bsWin, $scope) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '月报详情页面';
        vm.suppletter = {};//文件稿纸对象
        vm.id = $state.params.id;

        activate();
        function activate() {
            monthlyMultiyearSvc.getmonthlyMultiyearById(vm.id, function (data) {
                vm.suppletter = data;
            });
        }

    }
})();
