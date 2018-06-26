/**
 * 停用
 */
(function () {
    'use strict';

    angular.module('app').controller('assistCostCountEditCtrl', assistCostCount);

    assistCostCount.$inject = ['bsWin', 'assistCostCountSvc', 'addCostSvc', '$state'];

    function assistCostCount(bsWin, assistCostCountSvc, addCostSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '协审费录入';
        vm.signAssistCost = {};
        vm.signAssistCost.beginTime = (new Date()).halfYearAgo();
        vm.signAssistCost.endTime = (new Date()).Format("yyyy-MM-dd");

        vm.costType = $state.params.costType;
        vm.financials = [];

        activate();
        function activate() {
            vm.isSubmit = true;
            vm.nodata = false;
            assistCostCountSvc.findSingAssistCostList(vm.signAssistCost, function (data) {
                vm.isSubmit = false;
                vm.signAssistCostList = data;
                if(!vm.signAssistCostList || vm.signAssistCostList.length == 0){
                    vm.nodata = true;
                }
            });
        }

        //查询
        vm.queryAssistCost = function () {
            activate();
        }
        //重置
        vm.assistCostReset = function () {
            vm.signAssistCost = {};
        }

        /**
         * 协审费录入
         * @param object
         */
        vm.addCostWindow = function(object,id){
            debugger;
            addCostSvc.initAddCost(vm,vm.costType,object,id);
        }
    }
})();
