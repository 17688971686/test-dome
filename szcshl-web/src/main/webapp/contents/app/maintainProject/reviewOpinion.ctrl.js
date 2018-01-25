(function () {
    'use strict';

    angular.module('app').controller('reviewOpinionCtrl', reviewOpinion);

    reviewOpinion.$inject = ['bsWin', 'signSvc','$state','dispatchSvc','workprogramSvc'];

    function reviewOpinion(bsWin, signSvc,$state,dispatchSvc,workprogramSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '修改意见';
        vm.model = {};
        vm.dispatchDoc={};
        vm.model.signid = $state.params.signid;   //业务ID
        vm.model.processInstanceId = $state.params.processInstanceId;	//流程实例ID
        activate();
        function activate() {
            // 初始化业务信息
            signSvc.initFlowPageData(vm.model.signid, function (data) {
                vm.model = data;
                vm.dispatchDoc=vm.model.dispatchDocDto;
                vm.workList=vm.model.workProgramDtoList;
            });

        }
         //保存
        vm.create=function () {

           //保存项目登记表意见
            signSvc.updateFillin(vm.model);
            //保存工作方案的意见
            for (var i=0;i<vm.workList.length;i++){
                workprogramSvc.createWP(vm.workList[i], false, vm.iscommit);
            }
            //保存发文意见
            vm.dispatchDoc.signid=vm.model.signid;
            dispatchSvc.saveDispatch(vm);

        }
    }
})();
