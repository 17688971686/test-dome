(function () {
    'use strict';

    angular.module('app').controller('proCostClassifyCountCtrl', proCostClassifyCount);

    proCostClassifyCount.$inject = ['$location', 'projectCostCountSvc','adminSvc','$state','$http'];

    function proCostClassifyCount($location, projectCostCountSvc,adminSvc,$state,$http) {
        var vm = this;
        vm.title = '项目评审费分类统计';
        vm.model={};

        //查看汇总
        vm.projectCostCountList = function () {
            $state.go('projectCostCountList');
        }

        vm.initFinancial = function (businessId) {
            var url = $state.href('financialManager',{businessId:businessId});
            window.open(url,'_blank');
        }

        vm.proCostClassifyCount = function () {
            projectCostCountSvc.projectCostClassifyCout(vm,function(data){
                vm.proReviewClassifyDetailDtoList = data.reObj.proReviewClassifyDetailDtoList;
                vm.proReviewClassifyCountDtoList = data.reObj.proReviewClassifyCountDtoList;
            });
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
            projectCostCountSvc.projectCostClassifyCout(vm,function(data){
                vm.proReviewClassifyDetailDtoList = data.reObj.proReviewClassifyDetailDtoList;
                vm.proReviewClassifyCountDtoList = data.reObj.proReviewClassifyCountDtoList;
            });
        }
    }
})();
