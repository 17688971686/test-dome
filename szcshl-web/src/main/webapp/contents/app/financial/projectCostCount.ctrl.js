(function () {
    'use strict';

    angular.module('app').controller('projectCostCountCtrl', projectCostCount);

    projectCostCount.$inject = ['$location', 'projectCostCountSvc','adminSvc','$state','$http'];

    function projectCostCount($location, projectCostCountSvc,adminSvc,$state,$http) {
        var vm = this;
        vm.title = '项目评审费统计';
        vm.model={};

        //项目评审费分类统计
        vm.proCostClassifyCountList = function(){
            $state.go('proCostClassifyCountList');
        }

        vm.proCostCount = function () {
            projectCostCountSvc.projectCostTotal(vm,function(data){
                vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
            });
        }

        vm.initFinancial = function (businessId) {
            var url = $state.href('financialManager',{businessId:businessId});
            window.open(url,'_blank');
        }

        //重置查询表单
        vm.formReset = function(){
            vm.model = {};
        }

        activate();
        function activate() {
            adminSvc.initSignList(function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.orgDeptList = data.reObj;
                }
            });
            projectCostCountSvc.projectCostTotal(vm,function(data){
                vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
            });
        }
    }
})();
