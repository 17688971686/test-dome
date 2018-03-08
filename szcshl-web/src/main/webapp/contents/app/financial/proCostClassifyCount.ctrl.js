(function () {
    'use strict';

    angular.module('app').controller('proCostClassifyCountCtrl', proCostClassifyCount);

    proCostClassifyCount.$inject = ['$location', 'projectCostCountSvc','adminSvc','$state','$http'];

    function proCostClassifyCount($location, projectCostCountSvc,adminSvc,$state,$http) {
        var vm = this;
        vm.title = '项目评审费分类统计';
        vm.model={};
        vm.page=0;
        vm.proReviewClassifyDetailDtoList=[];
        vm.proReviewClassifyCountDtoList=[];
        vm.model.beginTime = (new Date()).halfYearAgo();
        vm.model.endTime = new Date().Format("yyyy-MM-dd");
        //查看汇总
        vm.projectCostCountList = function () {
            $state.go('projectCostCountList');
        }

        vm.initFinancial = function (businessId) {
            var url = $state.href('financialManager',{businessId:businessId});
            window.open(url,'_blank');
        }

        vm.proCostCount = function () {
            projectCostCountSvc.projectCostClassifyCout(vm,function(data){
   /*             vm.proReviewClassifyDetailDtoList = data.reObj.proReviewClassifyDetailDtoList;*/
                vm.proReviewClassifyCountDtoList = data.reObj.proReviewClassifyCountDtoList;
                if (data.reObj != undefined) {
                    data.reObj.proReviewClassifyDetailDtoList.forEach(function (obj, x) {
                        vm.proReviewClassifyDetailDtoList.push(obj);
                    });
                }
                if (data.reObj.proReviewClassifyDetailDtoList != undefined && data.reObj.proReviewClassifyDetailDtoList.length !=0) {
                    vm.page++;
                    vm.proCostCount();
                } else {

                }
            });
        }
        vm.proCostClassifyCount = function () {
            vm.proReviewClassifyDetailDtoList=[];
            vm.proReviewClassifyCountDtoList=[];
            vm.page=0;
            vm.proCostCount();

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
            /*vm.proCostCount();*/
/*            projectCostCountSvc.projectCostClassifyCout(vm,function(data){
                if(data.reObj.proReviewClassifyDetailDtoList.length!=0){
                    vm.page++;
                    vm.proCostClassifyCount();
                }
               /!* vm.proReviewClassifyDetailDtoList = data.reObj.proReviewClassifyDetailDtoList;
                vm.proReviewClassifyCountDtoList = data.reObj.proReviewClassifyCountDtoList;*!/
            });*/
        }
    }
})();
