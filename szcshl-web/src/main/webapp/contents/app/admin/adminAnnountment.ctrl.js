(function () {
    'use strict';

    angular.module('app').controller('adminAnnountmentCtrl', adminAnnountment);

    adminAnnountment.$inject = ['$location','$state','adminSvc']; 

    function adminAnnountment($location, $state,adminSvc) {
        var vm = this;
        vm.title = '通知公告详情页';
        vm.anId=$state.params.id;
        vm.annountmentList={};
        var annountmentList={};
             
        activate();
        function activate() {
        	adminSvc.findAnnountmentById(vm);
        	adminSvc.initFile(vm);
        }
        
        vm.post=function(id){
        	vm.anId=id;
        	adminSvc.findAnnountmentById(vm,id);
        	
        }
        
        vm.next=function(id){
        	vm.anId=id;
        	adminSvc.findAnnountmentById(vm,id);
        }
        
        vm.upload=function (sysFileId){
        	adminSvc.upload(vm,sysFileId);
        
        }
    }
})();
