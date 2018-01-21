(function () {
    'use strict';

    angular.module('app').controller('adminPersonAssistCtrl', personAssist);

    personAssist.$inject = ['$location', 'adminSvc', 'flowSvc','pauseProjectSvc','$state','$rootScope'];

    function personAssist($location, adminSvc, flowSvc,pauseProjectSvc,$state,$rootScope) {
        var vm = this;
        vm.title = '个人经办项目';
        vm.model = {};
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
            vm.showwin = false;
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

    }
})();
