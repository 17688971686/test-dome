(function () {
    'use strict';

    angular.module('app').controller('annountmentDetailCtrl',annountmentDetail);

    annountmentDetail.$inject = ['$location','$state','annountmentSvc'];

    function annountmentDetail($location, $state,annountmentSvc) {
        var vm = this;
        vm.title = '通知公告详情页';
        vm.annountment = {};    //通知公告对象
        vm.annountment.anId = $state.params.id;
        activate();
        function activate() {
            annountmentSvc.findDetailById(vm,vm.annountment.anId);
        }
        
        vm.post=function(id){
            annountmentSvc.findDetailById(vm,id);
        }
        
        vm.next=function(id){
            annountmentSvc.findDetailById(vm,id);
        }
    }
})();
