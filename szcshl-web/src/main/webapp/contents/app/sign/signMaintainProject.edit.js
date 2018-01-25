(function () {
    'use strict';

    angular.module('app').controller('MaintainProjectEditCtrl', MaintainProjectEdit);

    MaintainProjectEdit.$inject = ['sysfileSvc','signSvc','$state','flowSvc','$scope','templatePrintSvc'];

    function MaintainProjectEdit(sysfileSvc,signSvc,$state,flowSvc,$scope,templatePrintSvc) {
        var vm = this;
        vm.title = "维护项目修改列表";
        vm.model = {};
        vm.model.signid = $state.params.signid;   //业务ID
        vm.model.processInstanceId = $state.params.processInstanceId;	//流程实例ID
        active();
        function active(){

        }


    }
})();
