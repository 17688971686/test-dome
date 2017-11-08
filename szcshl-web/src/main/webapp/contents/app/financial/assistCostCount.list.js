(function () {
    'use strict';

    angular.module('app').controller('assistCostCountListCtrl', assistCostCountList);

    assistCostCountList.$inject = ['assistCostCountSvc','$state','addCostSvc'];

    function assistCostCountList(assistCostCountSvc,$state,addCostSvc) {
        var vm = this;
        vm.signAssistCost = {};                 //搜索对象
        vm.signAssistCost.beginTime = (new Date()).halfYearAgo();
        vm.signAssistCost.endTime = (new Date()).Format("yyyy-MM-dd");
        vm.costType = $state.params.costType;

        activate();
        function activate() {
            vm.isSubmit = true;
            vm.nodata = false;
            assistCostCountSvc.findSingAssistCostCount(vm.signAssistCost,function (data) {
                vm.isSubmit = false;
                vm.signAssistCostList = data;
                console.log(vm.signAssistCostList);
                if(!vm.signAssistCostList || vm.signAssistCostList.length == 0){
                    vm.nodata = true;
                }

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
