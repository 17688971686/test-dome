(function () {
    'use strict';

    angular.module('app').controller('assertStorageBusinessCtrl', assertStorageBusiness);

    assertStorageBusiness.$inject = ['$location', 'assertStorageBusinessSvc'];

    function assertStorageBusiness($location, assertStorageBusinessSvc) {
        var vm = this;
        vm.title = '固定资产申购流程';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    assertStorageBusinessSvc.deleteAssertStorageBusiness(vm, id);
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
            assertStorageBusinessSvc.grid(vm);
        }

        //表单查询
        vm.searchForm = function(){
            vm.gridOptions.dataSource._skip="";
            vm.gridOptions.dataSource.read();
        }

        //重置查询表单
        vm.formReset = function(){
            vm.searchModel = {};
        }
    }
})();
