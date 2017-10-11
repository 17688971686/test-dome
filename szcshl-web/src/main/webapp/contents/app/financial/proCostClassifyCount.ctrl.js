(function () {
    'use strict';

    angular.module('app').controller('proCostClassifyCountCtrl', proCostClassifyCount);

    proCostClassifyCount.$inject = ['$location', 'projectCostCountSvc','$state','$http'];

    function proCostClassifyCount($location, projectCostCountSvc,$state,$http) {
        var vm = this;
        vm.title = '项目评审费分类统计';
        vm.model={};

        //查看汇总
        vm.projectCostCountList = function () {
            $state.go('projectCostCountList');
        }

        activate();
        function activate() {
            projectCostCountSvc.projectCostClassifyCout(vm,function(data){
                vm.proReviewClassifyDetailDtoList = data.reObj.proReviewClassifyDetailDtoList;
                vm.proReviewClassifyCountDtoList = data.reObj.proReviewClassifyCountDtoList;
            });
        }
    }
})();
