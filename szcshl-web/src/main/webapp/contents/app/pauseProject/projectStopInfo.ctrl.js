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
                if(vm.sign.reviewstage == '可行性研究报告' || vm.sign.reviewstage == '项目概算'){

                    vm.sign.countUsedWorkday = 15-vm.sign.surplusdays;
                }else{

                    vm.sign.countUsedWorkday = 12-vm.sign.surplusdays;
                }
            });
            pauseProjectSvc.getListInfo(signId , function(data){
                vm.projectStopList = data;
            });
        }
    }
})();