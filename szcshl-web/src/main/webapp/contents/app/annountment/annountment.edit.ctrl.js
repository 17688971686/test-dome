(function () {
    'use strict';

    angular.module('app').controller('annountmentEditCtrl', annountmentEdit);

    annountmentEdit.$inject = ['$location','$state','$http','annountmentSvc'];

    function annountmentEdit($location,$state,$http,annountmentSvc) {
        var vm = this;
        vm.title="添加通知公告";
        vm.anId=$state.params.id;
        vm.annountment={};
       
        if(vm.anId){
        	vm.isUpdate = true;
        	vm.title="更新通知公告";
        }
        if(!vm.anId){
       		vm.annountment.isStick="1";
        }
        
         active();
        function active(){
        	annountmentSvc.initAnOrg(vm);
        	if(vm.anId){
        		annountmentSvc.findAnnountmentById(vm);
        	}
        }
        
        
        vm.create=function (){
        	annountmentSvc.createAnnountment(vm);
        }
        
        
        vm.update=function (){
        	annountmentSvc.updateAnnountment(vm);
        }
    }
})();