(function(){
    'use strict';
    angular.module('app').controller('projectStopInfoCtrl' , projectStopInfo);
    projectStopInfo.$inject = ['$state' , 'pauseProjectSvc'];
    function projectStopInfo( $state , pauseProjectSvc){
        var vm = this ;
        var signId = $state.params.signId ;
        vm.sign = {};
        activate();
        function activate(){
            pauseProjectSvc.initProject(signId,function(data){
                vm.sign = data;
                //评审天数-剩余工作日
                vm.sign.countUsedWorkday = vm.sign.reviewdays-vm.sign.surplusdays;
            });
            pauseProjectSvc.getListInfo(signId , function(data){
                vm.projectStopList = data;
            });
        }
    }
})();