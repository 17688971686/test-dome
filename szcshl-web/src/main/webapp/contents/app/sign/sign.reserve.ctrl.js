(function () {
    'use strict';

    angular.module('app').controller('signReserveCtrl', sign);

    sign.$inject = ['$location','reserveSignSvc','$state']; 

    function sign($location, reserveSignSvc,$state) {        
        var vm = this;
    	vm.model = {};						//创建一个form对象
        vm.title = '预签收列表';        		//标题  
        
       vm.del = function(id){
    	   common.confirm({
          	 vm:vm,
          	 title:"",
          	 msg:"确认删除数据吗？",
          	 fn:function () {
                	$('.confirmDialog').modal('hide');             	
                	reserveSignSvc.deleteReserveSign(vm,id);
               }
           })
       }
       //查询
       vm.querySign = function (){
    	   reserveSignSvc.querySign(vm);
       }
       
        active();
        function active(){
        	reserveSignSvc.grid(vm);
        }
       
    }
})();
