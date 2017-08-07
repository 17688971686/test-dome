(function () {
    'use strict';

    angular.module('app').controller('suppletterCtrl', suppletter);

    suppletter.$inject = ['$location','suppletterSvc','$state','$http']; 

    function suppletter($location, suppletterSvc,$state,$http) {
        var vm = this;
        vm.title = '待办事项';
        vm.suppletter={};
        activate();
        function activate() {
        	common.initSuppData(vm,{$http:$http,$state:$state});
        }
       /* vm.create=function(){
        }*/
    }
})();
