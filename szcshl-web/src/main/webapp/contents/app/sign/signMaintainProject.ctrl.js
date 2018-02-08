(function () {
    'use strict';
    angular.module('app').controller('MaintainProjectCtrl', MaintainProject);

    MaintainProject.$inject = ['signSvc', 'flowSvc', 'signFlowSvc', 'bsWin','$state','$rootScope'];

    function MaintainProject(signSvc, flowSvc, signFlowSvc, bsWin,$state,$rootScope) {
        var vm = this;
        vm.title = "维护项目列表";
        vm.model={};
        vm.model.signdatebigen = (new Date()).oneMonthAgo();
        vm.model.signdateend = (new Date()).Format("yyyy-MM-dd");
        $("#signdatebigen").val("");
        $("#signdateend").val("");
        //需要赋值给value才会去查询

        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.gridOptions.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});

        }

        //查找
        vm.query = function () {
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        /**
         * 重置
         */
        vm.formReset = function () {
            var tab = $("#deletform").find('input,select').not(":submit, :reset, :image, :disabled,:hidden");
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        active();
        function active() {
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

                signSvc.MaintenanProjectGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                $("#signdatebigen").val(vm.model.signdatebigen);
                $("#signdateend").val(vm.model.signdateend);
                signSvc.MaintenanProjectGrid(vm);
            }

        }

    }
})();