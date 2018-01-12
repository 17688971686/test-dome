(function () {
    'use strict';

    angular.module('app').controller('bookBuyBusinessCtrl', bookBuyBusiness);

    bookBuyBusiness.$inject = ['$location', 'bookBuyBusinessSvc'];

    function bookBuyBusiness($location, bookBuyBusinessSvc) {
        var vm = this;
        vm.title = '图书采购流程查询';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    bookBuyBusinessSvc.deleteBookBuyBusiness(vm, id);
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
            bookBuyBusinessSvc.grid(vm);
        }

        //表单查询
        vm.searchForm = function(){
            vm.gridOptions.dataSource.read();
        }

        //重置查询表单
        vm.formReset = function(){
            vm.searchModel = {};
        }
    }
})();
