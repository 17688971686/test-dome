(function(){
    'use strict';
    angular.module('app').controller('reviewProjectAppraiseEditCtrl' , reviewProjectAppraiseEdit);
    reviewProjectAppraiseEdit.$inject = ['reviewProjectAppraiseSvc'];
    function reviewProjectAppraiseEdit(reviewProjectAppraiseSvc){
        var vm = this ;
        vm.title = "评审项目评优列表";
        vm.approve = false;
        vm.idea = true;
        activate();
        function activate(){
            reviewProjectAppraiseSvc.endProjectGrid(vm);
        }


        /**
         * 评优申请弹出窗
         * @param vm
         * @param signId
         */
        vm.appraisingWindow = function(signId , projectName){
            vm.appraise = {};
            vm.appraise.signId = signId;
            vm.appraise.projectName = projectName;
            reviewProjectAppraiseSvc.initProposer(function(data){
                vm.appraise.proposerName = data.proposerName;
                vm.appraise.generalConductorOpinion = "9";
                reviewProjectAppraiseSvc.appraiseWindow(vm);
            })

        }

        /**
         * 取消
         */
        vm.closewin=function(){
            window.parent.$("#appraiseWindow").data("kendoWindow").close()
        }

        /**
         * 提交评优申请
         */
        vm.commitApprove = function(){
            reviewProjectAppraiseSvc.saveApply(vm);
        }

    }
})();