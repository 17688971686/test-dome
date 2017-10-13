(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterQueryCtrl', addSuppLetterQuery);
    addSuppLetterQuery.$inject = ['$location', 'addSuppLetterQuerySvc', '$state', 'bsWin'];
    function addSuppLetterQuery($location, addSuppLetterQuerySvc, $state, bsWin) {
        var vm = this;
        vm.suppletter = {}; //补充资料对象$state
        vm.id = $state.params.id;//主键ID
        vm.title = '登记补充资料';

        activate();
        function activate() {
        	//补充资料函审批列表
        	addSuppLetterQuerySvc.approveGrid(vm);
        	//补充资料函查询列表
        	addSuppLetterQuerySvc.addQueryGrid(vm);
        	
        }

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    addSuppLetterSvc.deleteAddSuppLetter(vm, id);
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

    }
})();