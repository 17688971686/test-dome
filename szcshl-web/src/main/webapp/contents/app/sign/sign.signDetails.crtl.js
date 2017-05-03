(function () {
    'use strict';

    angular.module('app').controller('signDetailsCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location, signSvc,$state) {        
        var vm = this;
    	vm.model = {};	//创建一个form对象   	
        vm.title = '填写报审登记表';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID
       
        vm.flow = {};
        vm.flow.taskId= '';
        
        signSvc.initFillData(vm); 
    	
        vm.completeFill = function (){
        	
        	 signSvc.completeFill(vm);
        }
       //申报登记编辑
       vm.updateFillin = function (){
    	   
    	   signSvc.updateFillin(vm);
    	   
       }
       
    
       
    }
})();
