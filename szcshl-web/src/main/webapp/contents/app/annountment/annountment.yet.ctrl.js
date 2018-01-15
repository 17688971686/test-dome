(function () {
    'use strict';

    angular.module('app').controller('annountmentYetCtrl', annountmentYet);

    annountmentYet.$inject = ['$location', '$state', '$http', 'annountmentYetSvc'];

    function annountmentYet($location, $state, $http, annountmentYetSvc) {
        var vm = this;
        vm.title = "通知公告列表";
        active();
        function active() {
            annountmentYetSvc.grid(vm);
        }

        //查看详情
        vm.detail = function(id){
            if(id){
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