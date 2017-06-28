(function () {
    'use strict';

    angular.module('app').controller('assistUnitCtrl', assistUnit);

    assistUnit.$inject = ['$location', 'assistUnitSvc'];

    function assistUnit($location, assistUnitSvc) {
        var vm = this;
        vm.title = '协审单位';
        vm.resource = {};

        vm.del = function (id) {
            vm.id = id;
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认要删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    vm.resource = {};
                    assistUnitSvc.deleteAssistUnit(vm, id);
                }
            });
        }

        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: "请选择数据"
                });
            } else {
                common.confirm({
                    vm: vm,
                    title: "",
                    msg: "确认要删除数据吗？",
                    fn: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < selectIds.length; i++) {
                            ids.push(selectIds[i].value);
                        }
                        var idStr = ids.join(",");
                        assistUnitSvc.deleteAssistUnit(vm, idStr);
                    }
                });
            }
        }

        vm.queryAssistUnit = function () {
            assistUnitSvc.queryAssistUnit(vm);
        }

        activate();
        function activate() {
            assistUnitSvc.grid(vm);
        }
    }
})();
