(function () {
    'use strict';

    angular.module('app').controller('signReserveAddCtrl', sign);

    sign.$inject = ['$location','reserveSignSvc','$state']; 

    function sign($location, reserveSignSvc,$state) {        
        var vm = this;
    	vm.model = {};						//创建一个form对象
        vm.title = '新增预签收收文';        		//标题
        vm.reserveAdd = function () {
        	reserveSignSvc.reserveAdd(vm);
        };       
    }
})();
