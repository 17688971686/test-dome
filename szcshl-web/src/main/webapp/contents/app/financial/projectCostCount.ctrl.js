(function () {
    'use strict';

    angular.module('app').controller('projectCostCountCtrl', projectCostCount);

    projectCostCount.$inject = ['$location', 'projectCostCountSvc','adminSvc','$state','$http','expertReviewSvc','bsWin'];

    function projectCostCount($location, projectCostCountSvc,adminSvc,$state,$http,expertReviewSvc,bsWin) {
        var vm = this;
        vm.title = '项目评审费统计';
        vm.model={};
        vm.model.beginTime = (new Date()).halfYearAgo();
        vm.model.endTime = (new Date()).Format("yyyy-MM-dd");

        //项目评审费分类统计
        vm.proCostClassifyCountList = function(){
            $state.go('proCostClassifyCountList');
        }

        vm.proCostCount = function () {
            projectCostCountSvc.projectCostTotal(vm,function(data){
                vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
            });
        }
        //查看项目的专家详细信息
        vm.queyCostWindow=function (data) {
            expertReviewSvc.initReview(data.businessId, "", function (data) {
                vm.titleName = "专家评审费";
                vm.expertReview = data;
                vm.reviewTitle = data.reviewTitle;
                vm.payDate = data.payDate;
                vm.businessId=vm.expertReview.businessId;
                vm.expertSelectedDtoList = data.expertSelectedDtoList;
                if( vm.expertSelectedDtoList && vm.expertSelectedDtoList.length >0){
                    vm.showReviewCost = true;
                    $("#expertCostWindow").kendoWindow({
                        width: "70%",
                        height: "600px;",
                        title: vm.titleName ,
                        visible: false,
                        modal: true,
                        closable: true,
                        actions: ["Pin", "Minimize", "Maximize", "close"]
                    }).data("kendoWindow").center().open();
                }else{
                    bsWin.alert("没有专家");
                }

            });

        }

        // vm.initFinancial = function (businessId) {
        //     var url = $state.href('financialManager',{businessId:businessId});
        //     window.open(url,'_blank');
        // }

        /**
         * 导出excel
         */
        vm.costExportExcel = function (){
            var fileName = vm.reviewTitle + "(" + vm.payDate + ")";
            projectCostCountSvc.exportExcel(vm , vm.businessId ,fileName );
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
