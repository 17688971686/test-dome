(function () {
    'use strict';

    angular.module('app').controller('${info.beanName?uncap_first}Ctrl', ${info.beanName?uncap_first});

    ${info.beanName?uncap_first}.$inject = ['$location', '${info.beanName?uncap_first}Svc'];

    function ${info.beanName?uncap_first}($location, ${info.beanName?uncap_first}Svc) {
        var vm = this;
        vm.title = '${info.comment!''}';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    ${info.beanName?uncap_first}Svc.delete${info.beanName!''}(vm, id);
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
            ${info.beanName?uncap_first}Svc.grid(vm);
        }
    }
})();
