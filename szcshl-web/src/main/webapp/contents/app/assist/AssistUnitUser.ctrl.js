(function () {
    'use strict';

    angular.module('app').controller('assistUnitUserCtrl', assistUnitUser);

    assistUnitUser.$inject = ['$location', 'assistUnitUserSvc'];

    function assistUnitUser($location, assistUnitUserSvc) {
        var vm = this;
        vm.title = '协审单位人员';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    assistUnitUserSvc.deleteAssistUnitUser(vm, id);
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
        
        vm.queryAssistUnitUser=function(){
        	assistUnitUserSvc.queryAssistUnitUser(vm);
        }

        activate();
        function activate() {
            assistUnitUserSvc.grid(vm);
        }
    }
})();
