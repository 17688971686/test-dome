(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterQueryCtrl', addSuppLetterQuery);
    addSuppLetterQuery.$inject = ['$location', 'addSuppLetterQuerySvc', '$state', 'bsWin', 'orgSvc'];
    function addSuppLetterQuery($location, addSuppLetterQuerySvc, $state, bsWin, orgSvc) {
        var vm = this;
        vm.suppletter = {}; //补充资料对象$state
        vm.id = $state.params.id;//主键ID
        vm.isQuery = true;
        vm.model = {};

        activate();
        function activate() {
            //补充资料函查询列表
            addSuppLetterQuerySvc.addQueryGrid(vm);
            //根据部门视图查询部门列表
            orgSvc.queryOrgList(vm, function (data) {
                vm.model.org = data;
            })
        }

        /**
         * 查询
         */
        vm.quarySuppContent = function () {
            vm.queryGridOptions.dataSource._skip="";
            vm.queryGridOptions.dataSource.read();
        }

        /**
         * 重置
         */
        vm.resetSuppContent = function () {
            var tab = $("#supQueryForm").find('input,select');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        /**
         * 查看拟补充资料函详情
         * @param item
         */
        vm.showSuppLetterDetail = function(item){
            vm.suppletter = item || {};
            $("#suppLetterDetailDiv").kendoWindow({
                width: "70%",
                height: "620px",
                title: "拟补充资料函详情",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

    }
})();
