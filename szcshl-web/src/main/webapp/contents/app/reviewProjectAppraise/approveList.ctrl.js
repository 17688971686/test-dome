(function(){
    'use strict';
    angular.module('app').controller('approveListCtrl'  , approveList);
    approveList.$inject = ['reviewProjectAppraiseSvc'];
    function approveList(reviewProjectAppraiseSvc){
        var vm = this ;
        vm.approve = true;
        vm.idea = false;
        vm.appraise={};

        activate();
        function activate(){
            reviewProjectAppraiseSvc.approveListGrid(vm);
        }

        /**
         * 审批处理弹出框
         * @param id
         */
        vm.dealWindow = function(id){

            reviewProjectAppraiseSvc.getAppraiseById(id , function(data){
                vm.appraise = data;
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
         * 保存审批意见
         */
        vm.commitApprove = function (){
            reviewProjectAppraiseSvc.saveApprove(vm);
        }


    }
})();