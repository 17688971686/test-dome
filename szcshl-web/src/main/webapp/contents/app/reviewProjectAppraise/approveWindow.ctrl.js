(function(){
    'use strict';
    angular.module('app').controller('approveWindowCtrl' , approveWindow);
    approveWindow.$inject = ['$state','reviewProjectAppraiseSvc'];
    function approveWindow($state , reviewProjectAppraiseSvc){
        var vm = this;
        var signId = $state.params.signId;
        var projectName = $state.params.projectName;
        var id = $state.params.id;

        activate();
        function  activate(){
            if(id && id !=''){
                vm.approve = true;
                vm.idea = false;
                reviewProjectAppraiseSvc.getAppraiseById(id , function(data){
                    vm.appraise = data;
                    vm.appraise.generalConductorOpinion = "9";
                })
            }else{
                vm.approve = false;
                vm.idea = true;
                vm.appraise = {};
                vm.appraise.signId = signId;
                vm.appraise.projectName = projectName;
                reviewProjectAppraiseSvc.initProposer(function(data){
                    vm.appraise.proposerName = data.proposerName;
                    vm.appraise.generalConductorOpinion = "9";
                })
            }

        }

        /**
         * 保存审批意见
         */
        vm.commitApprove = function (){
            if(id && id != ''){
                reviewProjectAppraiseSvc.saveApprove(vm);
            }else{
                reviewProjectAppraiseSvc.saveApply(vm);
            }

        }
    }
})();