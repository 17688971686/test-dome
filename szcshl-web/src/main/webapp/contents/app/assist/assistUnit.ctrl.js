(function () {
    'use strict';

    angular.module('app').controller('assistUnitCtrl', assistUnit);

    assistUnit.$inject = ['$location', 'assistUnitSvc','$state','$rootScope'];

    function assistUnit($location, assistUnitSvc,$state,$rootScope) {
        var vm = this;
        vm.title = '协审单位';
        vm.model={};
        vm.resource = {};
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.gridOptions.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});

        }
        activate();
        function activate() {
            if($rootScope.view[vm.stateName]){
                var preView = $rootScope.view[vm.stateName];
                //恢复grid
                if(preView.gridParams){
                    vm.gridParams = preView.gridParams;
                }
                //恢复表单参数
                if(preView.data){
                    vm.model = preView.data.model;
                }
                //恢复数据
                /*vm.project = preView.data.project;*/
                //恢复页数页码
                if(preView.queryParams){
                    vm.queryParams=preView.queryParams;
                }

                assistUnitSvc.grid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                assistUnitSvc.grid(vm);
            }

        }

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


    }
})();
