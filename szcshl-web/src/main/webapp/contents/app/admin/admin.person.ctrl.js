(function () {
    'use strict';

    angular.module('app').controller('adminPersonDoingCtrl', adminPersonDoing);

    adminPersonDoing.$inject = ['$location', 'adminSvc', 'flowSvc','pauseProjectSvc','$state','$rootScope'];

    function adminPersonDoing($location, adminSvc, flowSvc,pauseProjectSvc,$state,$rootScope) {
        var vm = this;
        vm.title = '个人经办项目';
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

                adminSvc.personMainTasksGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                adminSvc.personMainTasksGrid(vm);
            }

        }

        /**
         * 查询
         */
        vm.querySign = function(){
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        /**
         * 重置
         */
        vm.resetForm = function(){
            vm.model = {};
        }

        /**
         * 项目暂停
         * @param signid
         */
        vm.pauseProject = function (signid) {
            pauseProjectSvc.findPausingProject(vm,signid);
        }

        /**
         * 流程激活
         * @param signid
         */
        vm.startProject = function (signid) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认激活吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    flowSvc.activeFlow(vm, signid);
                }
            })
        }
    }
})();
