(function () {
    'use strict';

    angular.module('app').controller('registerFileCtrl', registerFile);

    registerFile.$inject = ['$location','registerFileSvc','$state','$http']; 

    function registerFile($location, registerFileSvc,$state,$http) {
        var vm = this;
        vm.title = '待办事项';
        vm.addregister={};
        vm.model={};
        vm.model.signid=$state.params.signid;
        activate();
        function activate() {
        	vm.showPrint=false;
        	registerFileSvc.grid(vm);
        }
        vm.print = function(){
        	//vm.registerFileList={};
    		//vm.signdto={};
        	common.registerFilePrint(vm,{$http: $http});
        }
    }
})();
