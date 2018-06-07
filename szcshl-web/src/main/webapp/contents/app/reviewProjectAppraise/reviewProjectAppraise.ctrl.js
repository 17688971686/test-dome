
(function () {
    'use strict';

    angular.module('app').controller('reviewProjectAppraiseCtrl', reviewProjectAppraise);

    reviewProjectAppraise.$inject = ['adminSvc', 'reviewProjectAppraiseSvc','$state','$rootScope'];

    function reviewProjectAppraise(adminSvc, reviewProjectAppraiseSvc,$state,$rootScope) {
        var vm = this;
        vm.title = '优秀评审报告列表';
        vm.orgDeptList = [];
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
            //初始化查询参数
            adminSvc.initSignList(function (data) {
                if (data.flag || data.reCode == 'ok') {
                    vm.orgDeptList = data.reObj;
                }
            });

            if($rootScope.view[vm.stateName]){
                var preView = $rootScope.view[vm.stateName];
                //恢复查询条件
                if(preView.gridParams){
                    vm.gridParams = preView.gridParams;
                }
                //恢复表单参数
                if(preView.data){
                    vm.model = preView.data.model;
                }
                //恢复页数页码
                if(preView.queryParams){
                    vm.queryParams=preView.queryParams;
                }
                reviewProjectAppraiseSvc.appraisingProjectGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                reviewProjectAppraiseSvc.appraisingProjectGrid(vm);
            }
        }

        vm.searchSignList = function () {
            vm.saveView();
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        vm.formReset = function(){
            vm.model = {};
        }
    }
})();

