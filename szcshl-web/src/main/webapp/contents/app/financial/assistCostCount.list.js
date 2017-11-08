(function () {
    'use strict';

    angular.module('app').controller('assistCostCountListCtrl', assistCostCountList);

    assistCostCountList.$inject = ['assistCostCountSvc','$state','addCostSvc'];

    function assistCostCountList(assistCostCountSvc,$state,addCostSvc) {
        var vm = this;
        vm.signAssistCost = {};                 //搜索对象
        vm.signAssistCost.beginTime = "2017-11-08";
        vm.signAssistCost.endTime = new Date().toISOString().slice(0,10);
        console.log(vm.signAssistCost.endTime);
        vm.costType = $state.params.costType;

        activate();
        function activate() {
            assistCostCountSvc.findSingAssistCostCount(vm.signAssistCost,function (data) {
                vm.signAssistCostList = data;
            });
        }

        /**
         * 协审费录入
         * @param object
         */
        vm.addCostWindow = function(object,id){
            addCostSvc.initAddCost(vm,vm.costType,object,id);
        }

        //查询
        vm.queryAssistCost = function(){
            activate();
        }
        //重置
        vm.assistCostReset = function(){
            vm.signAssistCost = {};
        }

    }
})();
