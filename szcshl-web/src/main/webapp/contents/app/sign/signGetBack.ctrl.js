/**
 * Created by Administrator on 2017/11/10.
 */
(function () {
    'use strict';
    angular.module('app').controller('signGetBackCtrl', signGetBack);

    signGetBack.$inject = ['signSvc','$state','flowSvc','signFlowSvc','bsWin'];

    function signGetBack(signSvc,$state,flowSvc,signFlowSvc,bsWin) {
        var vm = this;

        vm.getBack=function (taskId,businessKey) {
            signSvc.getBack(vm,taskId,businessKey);
        }
        active();
        function active() {
            signSvc.signGetBackGrid(vm);
        }


    }
})();
