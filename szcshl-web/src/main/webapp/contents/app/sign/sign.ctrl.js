(function () {
    'use strict';

    angular.module('app').controller('signCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location,signSvc,$state) {        
        var vm = this;
        vm.title = "收文列表";
        //initGrid
        signSvc.grid(vm);
        
        vm.querySign = function(){
        	signSvc.querySign(vm);
        }
        vm.updateSign = function(){
        	signSvc.updateSign(vm);
        }
        vm.fillSignTest = function() {
        	$state.go('fillSign', {signid: "8371bfa6-9084-4b0e-b2fd-5ea3aea51385"});
        }
    }
})();
