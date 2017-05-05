(function () {
    'use strict';

    angular.module('app').controller('signFillinCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location, signSvc, $state) {        
        var vm = this;
    	vm.model = {};		//创建一个form对象   	
        vm.title = '填写报审登记表';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID
       
        vm.flowDeal = false;		//是否是流程处理标记
        
        signSvc.initFillData(vm);    	
        
        //申报登记编辑
        vm.updateFillin = function (){   	   
    	   signSvc.updateFillin(vm);  	   
        }
             
       //申报登记编辑
       vm.updateFillin = function (){
    	   signSvc.updateFillin(vm);
       }
       
       //根据部门查询用户
       vm.findUsersByOrgId = function(type){
    	   signSvc.findUsersByOrgId(vm,type);
       }

    }
})();
