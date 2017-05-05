(function () {
    'use strict';

    angular.module('app').controller('signDetailsCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location, signSvc,$state) {        
        var vm = this;
    	vm.model = {};							//创建一个form对象   	
        vm.title = '查看详情信息';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID
              
        signSvc.initDetailData(vm);     	                        
    }
})();
