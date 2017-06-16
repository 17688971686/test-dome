(function () {
    'use strict';

    angular.module('app').controller('workprogramBaseEditCtrl', workprogram);

    workprogram.$inject = ['$location','workprogramSvc','$state',"$http"]; 

    function workprogram($location,workprogramSvc,$state,$http) {        
        var vm = this;
    	vm.work = {};						//创建一个form对象
        vm.title = '项目基本信息';        	//标题
        vm.work.signId = $state.params.signid;		//这个是收文ID

        activate();
        function activate() {
        	workprogramSvc.initPage(vm);
        }

        vm.create = function () {  
        	workprogramSvc.createWP(vm,false);
        };  

    }
})();
