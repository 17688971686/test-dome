(function(){
    'use strict';
    angular.module('app').controller('approveWindowEditCtrl' , approveWindowEdit);
    approveWindowEdit.$inject = ['$state','reviewProjectAppraiseSvc','bsWin'];
    function approveWindowEdit($state , reviewProjectAppraiseSvc,bsWin){
        var vm = this;
        var id = $state.params.id;
        vm.model = {};

        //保存优秀评审报告申请表
        vm.addApprove=function(){
            reviewProjectAppraiseSvc.saveApply(vm.model,function () {
                bsWin.alert("操作成功");
            })
        }

        activate();
        function  activate(){
            reviewProjectAppraiseSvc.getAppraiseById(id,function(data){
                vm.model = data;
            })
        }

    }
})();