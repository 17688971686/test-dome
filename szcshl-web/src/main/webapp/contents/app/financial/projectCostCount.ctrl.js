(function () {
    'use strict';

    angular.module('app').controller('projectCostCountCtrl', projectCostCount);

    projectCostCount.$inject = ['$location', 'projectCostCountSvc','$state','$http'];

    function projectCostCount($location, projectCostCountSvc,$state,$http) {
        var vm = this;
        vm.title = '项目评审费统计';
        vm.model={};

        //项目评审费分类统计
        vm.proCostClassifyCountList = function(){
            $state.go('proCostClassifyCountList');
        }
        activate();
        function activate() {
            projectCostCountSvc.projectCostTotal(vm,function(data){
                vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
            });
        }
    }
})();
