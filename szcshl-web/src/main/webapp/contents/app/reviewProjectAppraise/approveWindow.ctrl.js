(function(){
    'use strict';
    angular.module('app').controller('approveWindowCtrl' , approveWindow);
    approveWindow.$inject = ['$state','reviewProjectAppraiseSvc','bsWin'];
    function approveWindow($state , reviewProjectAppraiseSvc,bsWin){
        var vm = this;
        var signId = $state.params.signId;
        vm.model = {};

        activate();
        function  activate(){
            reviewProjectAppraiseSvc.initBySignId(signId,function(data){
                vm.model = data;
            })
        }

        /**
         * 发起流程
         */
        vm.commitApprove = function (){
            common.initJqValidation();
            var isValid = $('#form').valid();
            if(isValid){
                reviewProjectAppraiseSvc.saveApply(vm.model,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        vm.model = data.reObj;
                        reviewProjectAppraiseSvc.startFlow(vm.model.id,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                bsWin.success("操作成功！",function(){
                                    $state.go("personDtasks");
                                });
                            }
                        });
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("优秀评审报告填报表单未填写正确！");
            }
        }
    }
})();