(function () {
    'use strict';

    angular.module('app').controller('workprogramEditCtrl', workprogram);

    workprogram.$inject = ['$location','workprogramSvc','$state']; 

    function workprogram($location,workprogramSvc,$state) {        
        var vm = this;
    	vm.work = {};						//创建一个form对象
        vm.title = '创建评审方案';        	//标题
         
        vm.work.signId = $state.params.signid;		//这个是收文ID
        
        workprogramSvc.initPage(vm);
        workprogramSvc.findOrgs(vm);//查找主管部门
        //查询评估部门
        vm.findUsersByOrgId = function(type){
        	workprogramSvc.findUsersByOrgId(vm,type);
        }
        vm.create = function () {
        	
        	workprogramSvc.createWP(vm);
        };      
        vm.findReviewDept = function(){
        	alert("ssd");
        }
    }
})();
