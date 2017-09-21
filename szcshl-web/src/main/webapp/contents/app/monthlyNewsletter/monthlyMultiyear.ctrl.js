(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiyearCtrl', monthlyMultiyear);

    monthlyMultiyear.$inject = ['$location', 'monthlyMultiyearSvc'];

    function monthlyMultiyear($location, monthlyMultiyearSvc) {
        var vm = this;
        vm.title = '年度月报简报';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    monthlyMultiyearSvc.deletemonthlyMultiyear(vm, id);
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
            monthlyMultiyearSvc.monthlyMultiyearGrid(vm);
        }
    }
})();
