(function () {
    'use strict';

    angular.module('app').controller('projectCostCountCtrl', projectCostCount);

    projectCostCount.$inject = ['$location', 'projectCostCountSvc','$state','$http'];

    function projectCostCount($location, projectCostCountSvc,$state,$http) {
        var vm = this;
        vm.title = '项目评审费统计';
        vm.model={};

        //按月份统计专家明细
        vm.countExpertCostDetail = function(){
            projectCostCountSvc.expertCostDetailTotal(vm,function(data){
                vm.expertCostTotalInfo = data.reObj.expertCostTotalInfo
             var trCount = $("#expertCostTable tr").length;
                 for(var i=1;i<trCount;i++){
                     $("#option"+i).remove();
                 }
                projectCostCountSvc(vm.expertCostTotalInfo);

            });
        }

        vm.countExpertCost = function () {
            var timeArr =  vm.model.beginTime.split("-");
            vm.year = timeArr[0];
            vm.month = timeArr[1];
            projectCostCountSvc.expertCostTotal(vm,function(data){
                vm.expertCostTotalInfo = data.reObj.expertCostTotalInfo
            });
        }

        vm.getExpertCoustDetail = function () {
            $state.go('expertPaymentDetailCountList',{beginTime:vm.model.beginTime});
        }
        activate();
        function activate() {
            projectCostCountSvc.projectCostTotal(vm,function(data){
                vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
            });

        }
    }
})();
