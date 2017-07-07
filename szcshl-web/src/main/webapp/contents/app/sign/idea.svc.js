(function () {
    'use strict';

    angular.module('app').factory('ideaSvc', idea);

    idea.$inject = ['$http', '$state'];

    function idea($http, $state) {
        var service = {
        	initIdea : initIdea //初始化个人常用意见
        };
        return service;
        
        
        function initIdea(vm){
        
        	var httpOptions={
        		method: 'get',
           		url: rootPath + "/idea"
        	}
        	
        	var httpSuccess=function success(response){
        		vm.ideas=response.data;
        	}
        	
        	 common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
     }
})();