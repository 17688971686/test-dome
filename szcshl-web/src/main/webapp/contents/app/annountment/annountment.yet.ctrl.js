(function () {
    'use strict';

    angular.module('app').controller('annountmentYetCtrl', annountmentYet);

    annountmentYet.$inject = ['$location', '$state', '$http', 'annountmentYetSvc','$rootScope'];

    function annountmentYet($location, $state, $http, annountmentYetSvc,$rootScope) {
        var vm = this;
        vm.title = "通知公告列表";
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.gridOptions.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});

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

                annountmentYetSvc.grid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                annountmentYetSvc.grid(vm);
            }

        }

        //查看详情
        vm.detail = function(id){
            if(id){
                vm.saveView();
                $state.go('annountmentDetail', {id: id});
            }
        }

        //查询
        vm.findAnnountment = function(){
            vm.gridOptions.dataSource._skip=0;
        	vm.gridOptions.dataSource.read();
        }

        
         //重置
        vm.resetAnnountment=function(){
        	var tab=$("#annountmentYetform").find('input,select');
        	$.each(tab,function(i,obj){
        		obj.value="";
        	});
        }
    }
})();