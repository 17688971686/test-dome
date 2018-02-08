(function () {
    'use strict';

    angular.module('app')
    .filter('date', function(){
        return function(val){
            if(val){
                return (new Date(val.CompatibleDate())).Format("yyyy-MM-dd");
            }else{
                return "";
            }
        }
    }).controller('adminCtrl', admin);

    admin.$inject = ['$location','adminSvc','$state','$rootScope'];

    function admin($location, adminSvc,$state,$rootScope) {
        var vm = this;
        vm.title = '待办项目';
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

                adminSvc.gtasksGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                adminSvc.gtasksGrid(vm);
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
            var tab = $("#searchform").find('input,select');
            $.each(tab, function(i, obj) {
                obj.value = "";
            });
        }

        vm.countWorkday=function(){
        	adminSvc.countWorakday(vm);
        }

    }
})();
