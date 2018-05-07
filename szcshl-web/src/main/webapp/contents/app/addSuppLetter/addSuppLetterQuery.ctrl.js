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
            var tab = $("#supQueryForm").find('input,select').not(":submit, :reset, :image, :disabled,:hidden");
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        /**
         * 查看拟补充资料函详情
         * @param item
         */
        vm.showSuppLetterDetail = function(id){
            vm.isDisplay=true;
            vm.id=id;
            addSuppLetterQuerySvc.getaddSuppLetterQueryById(vm,function (data) {
                vm.suppletter =data;
                $("#suppLetterDetailDiv").kendoWindow({
                    width: "1072px",
                    height: "800px",
                    title: "拟补充资料函详情",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            });
        }

        /**
         * 删除拟补充资料函
         * @param id
         */
        vm.deleteById = function(id){
            bsWin.confirm({
                title: "询问提示",
                message: "确定删除么？",
                onOk: function () {
                    addSuppLetterQuerySvc.deleteaddSuppLetterQuery(id,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            bsWin.alert("删除成功！",function(){
                                vm.quarySuppContent();
                            });
                        }else {
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            });
        }
    }
})();
