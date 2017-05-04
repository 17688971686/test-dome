(function () {
    'use strict';

    angular.module('app').controller('workprogramEditCtrl', workprogram);

    workprogram.$inject = ['$location','workprogramSvc','$state']; 

    function workprogram($location,workprogramSvc,$state) {        
        var vm = this;
    	vm.work = {};						//创建一个form对象
        vm.title = '创建评审方案';        	//标题
         
        vm.work.signId = $state.params.signid;		//这个是收文ID
        alert(vm.work.signId);
        
        workprogramSvc.initPage(vm);
       
        vm.create = function () {
        	workprogramSvc.createWP(vm);
        };       
    }
})();
