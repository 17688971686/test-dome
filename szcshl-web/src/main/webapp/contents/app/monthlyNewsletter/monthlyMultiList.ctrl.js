(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiFileCtrl', monthlyMultiFile);

    monthlyMultiFile.$inject = ['$location', 'monthlyMultiyearSvc','$state','bsWin','$rootScope'];

    function monthlyMultiFile($location, monthlyMultiyearSvc,$state,bsWin,$rootScope) {
        var vm = this;
        vm.title = '月报简报查询';

        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.multiyearGrid.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});

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

                monthlyMultiyearSvc.monthlyMultiyearGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                monthlyMultiyearSvc.monthlyMultiyearGrid(vm);
            }

        }

        //查询
         vm.addSuppQuery = function(){
             vm.multiyearGrid.dataSource._skip=0;
             vm.multiyearGrid.dataSource.read();
         }
         //重置
         vm.resetAddSupp = function(){
        	 var tab = $("#form").find('input,select');
 			$.each(tab, function(i, obj) {
 				obj.value = "";
 			});
         }
    }
})();
