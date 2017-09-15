(function () {
    'use strict';

    angular.module('app').controller('goodsDetailEditCtrl', goodsDetail);

    goodsDetail.$inject = ['$location', 'goodsDetailSvc', '$state'];

    function goodsDetail($location, goodsDetailSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加物品明细';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新物品明细';
        }

        vm.create = function () {
            goodsDetailSvc.createGoodsDetail(vm);
        };
        vm.update = function () {
            goodsDetailSvc.updateGoodsDetail(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                goodsDetailSvc.getGoodsDetailById(vm);
            }
        }
    }
})();
