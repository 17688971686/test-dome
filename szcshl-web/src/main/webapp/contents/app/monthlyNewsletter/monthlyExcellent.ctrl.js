(function () {
    'use strict';

    angular.module('app').controller('monthlyHistoryCtrl', monthlyHistory);

    monthlyHistory.$inject = ['$location', 'monthlyHistorySvc'];

    function monthlyHistory($location, monthlyHistorySvc) {
        var vm = this;
        vm.title = '月报简报历史数据';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    monthlyHistorySvc.deletemonthlyHistory(vm, id);
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
            monthlyHistorySvc.monthlyHistoryGrid(vm);
            monthlyHistorySvc.deleteGridOptions(vm);
        }
    }
})();
