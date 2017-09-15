(function () {
    'use strict';

    angular.module('app').controller('goodsDetailCtrl', goodsDetail);

    goodsDetail.$inject = ['$location', 'goodsDetailSvc'];

    function goodsDetail($location, goodsDetailSvc) {
        var vm = this;
        vm.title = '物品明细';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    goodsDetailSvc.deleteGoodsDetail(vm, id);
                }
            });
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };

        activate();
        function activate() {
            goodsDetailSvc.grid(vm);
        }
    }
})();
