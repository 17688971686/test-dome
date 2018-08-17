(function(){
    'use strict';
    angular.module('app').controller('policyAddCtrl',policyAdd);
    policyAdd.$inject=['$state','fileLibrarySvc','$scope' , 'bsWin'];
    function policyAdd($state,fileLibrarySvc,$scope , bsWin){
        var vm = this;
        activate();
        function activate(){

        }

        //保存
        vm.create = function(){
            common.initJqValidation($('#policyform'));
            var isValid = $('#policyform').valid();
            if (isValid) {
                fileLibrarySvc.createPolicy(vm.model, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        vm.model = data.reObj;
                        bsWin.alert("操作成功！");
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("页面未填报完整或者为正确，请检查！");
            }
        }

    }

})();