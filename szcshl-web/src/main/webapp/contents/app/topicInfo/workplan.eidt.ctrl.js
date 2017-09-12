(function () {
    'use strict';

    angular.module('app').controller('workPlanEditCtrl', workPlan);

    workPlan.$inject = ['bsWin', '$scope', 'sysfileSvc', 'workPlanSvc','$state'];

    function workPlan(bsWin, $scope, sysfileSvc, workPlanSvc,$state) {
        var vm = this;
        vm.title = '课题研究成果鉴定会工作方案编辑';
        vm.workplan = {};
        if($state.params.topicId){
            vm.workplan.topicId = $state.params.topicId;
        }

        activate();
        function activate() {
            if(vm.workplan.topicId){
                workPlanSvc.findByTopicId(vm.workplan.topicId,function(data){
                    vm.workplan = data;
                    vm.workplan.topicId = $state.params.topicId;
                })
            }
        }

        //S_保存工作方案
        vm.saveWorkPlan = function(){
            workPlanSvc.save(vm.workplan,function (data) {
                if(data.flag || data.reCode == 'ok'){
                    vm.workplan = data.reObj;
                    bsWin.alert("保存成功！");
                }else{
                    bsWin.error(data.reMsg);
                }
            })
        }//E_saveWorkPlan

    }
})();
