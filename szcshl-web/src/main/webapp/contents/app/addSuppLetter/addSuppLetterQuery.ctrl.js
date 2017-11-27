(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterQueryCtrl', addSuppLetterQuery);
    addSuppLetterQuery.$inject = ['$location', 'addSuppLetterQuerySvc', '$state', 'bsWin','orgSvc' ];
    function addSuppLetterQuery($location, addSuppLetterQuerySvc, $state, bsWin,orgSvc) {
        var vm = this;
        vm.suppletter = {}; //补充资料对象$state
        vm.id = $state.params.id;//主键ID
        vm.title = '拟补充资料函查询';
        // vm.suppletter.fileType = "1";
        vm.isQuery = true;
        vm.model={};

        activate();
        function activate() {
        	//补充资料函查询列表
        	addSuppLetterQuerySvc.addQueryGrid(vm);
        	//根据部门视图查询部门列表
            orgSvc.queryOrgList(vm,function (data) {
                vm.model.org=data;
            })
        }

        /**
         * 查询
         */
        vm.quarySuppContent = function(){
            vm.queryGridOptions.dataSource.read();
        }

        /**
         * 重置
         */
        vm.resetSuppContent = function(){
            var tab = $("#supQueryForm").find('input,select');
            $.each(tab, function (i, obj) {

                // obj.attr("selected",false);
                obj.value = "";
            });
        }

      /*  vm.del = function (id) {
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
        };*/

    }
})();
