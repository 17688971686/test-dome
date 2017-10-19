(function () {
    'use strict';

    angular.module('app').controller('assistCostCountListCtrl', assistCostCountList);

    assistCostCountList.$inject = ['$location', 'assistCostCountSvc', '$state','$http'];

    function assistCostCountList($location, assistCostCountSvc, $state,$http) {
        var vm = this;
        vm.title = '协审费统计';
        vm.signAssistCost = {};

        activate();
        function activate() {
            assistCostCountSvc.findSingAssistCostCount(vm.signAssistCost,function (data) {
                vm.signAssistCostCounList = data;
            });
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
