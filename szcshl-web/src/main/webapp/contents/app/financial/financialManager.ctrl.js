/**
 * 停用
 */
(function () {
    'use strict';

    angular.module('app').controller('financialManagerCtrl', financialManager);

    financialManager.$inject = ['bsWin', 'financialManagerSvc','$state','$http' , 'expertReviewSvc'];

    function financialManager(bsWin, financialManagerSvc,$state,$http , expertReviewSvc) {
        var vm = this;
        vm.title = '评审费录入';
        vm.financials = new Array;
        vm.financial = {};//财务对象
        vm.financial.businessId = $state.params.businessId;

        activate();
        function activate() {
            financialManagerSvc.initFinancialProject(vm.financial.businessId,function(data){
                vm.model = data.financialDto;
                vm.financials = data.financiallist;
                vm.countCost();
            });
        }

        /**
         * 计算总金额
         */
        vm.countCost = function(){
            if(vm.financials && vm.financials.length > 0){
                var totalCost = 0;
                angular.forEach(vm.financials,function (f,i) {
                    if(f.charge){
                        totalCost += f.charge;
                    }
                })
                $("#financialCount").html(common.removeXss(totalCost));
            }
        }

        //跳转到评审会发放表页面
        vm.findStageCostTable = function(){
            expertReviewSvc.initReview(vm.financial.businessId , "", function (data){
                vm.expertReview = data;
                vm.reviewTitle = data.reviewTitle;
                vm.payDate = data.payDate;
                vm.expertSelectedDtoList = data.expertSelectedDtoList;
                $("#stageCostWindow").kendoWindow({
                    width: "70%",
                    height: "600px",
                    title: "评审费用统计表",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            } )
        	// $state.go('findStageCostTable',{signid: vm.financial.signid});
        }


        /**
         * 导出excel
         */
        vm.exportExcel = function (){
            var fileName = vm.reviewTitle + "(" + vm.payDate + ")";
            financialManagerSvc.exportExcel(vm , vm.financial.businessId ,fileName );
        }


        //S 输入数字校验
        vm.inputIntegerValue = function(checkValue,idSort){
        	if(financialManagerSvc.isUnsignedInteger(checkValue)){
        		$("#errorsUnmber" + idSort).html("");
        	}else{
        		$("#errorsUnmber" + idSort).html("只能输入数字");
        	}
        }
        //E 输入数字校验
        
        //添加报销记录
       vm.addFinancial =  function () {
           var financial = {};
           financial.chargeType = "1";
           financial.businessId = vm.model.businessId;
           financial.projectName = vm.model.projectName;
           financial.paymentData = vm.model.paymentData;
           if(!vm.financials){
               vm.financials = [];
           }
           vm.financials.push(financial);
        }// end
       
       //保存报销记录
       vm.saveFinancial = function (){
           financialManagerSvc.savefinancial(vm.financials,function(data){
               if(data.flag || data.reCode == 'ok'){
                   vm.financials = data.reObj;
                   if(!vm.model.paymentData){
                       vm.model.paymentData = vm.financials[0].paymentData;
                   }
                   vm.countCost();
                   bsWin.success(data.reMsg);
               }else{
                   bsWin.error(data.reMsg);
               }
           });
       }

       //删除报销记录
       vm.deleteFinancial = function(){
           var isChecked = $("#financialsTable input[name='financialsCheck']:checked");
           if (isChecked.length < 1) {
               common.alert({
                   vm: vm,
                   msg: "请选择要删除的记录！"
               })
           } else {
               var ids = [];
               for (var i = 0; i < isChecked.length; i++) {
                   vm.financials.forEach(function (f, number) {
                       if (f.id == undefined || isChecked[i].value == f.id ) {
                           vm.financials.splice(number, 1);
                       }
                       if(isChecked[i].value){
                           ids.push(isChecked[i].value);
                       }
                   });
               }
               if(ids.length > 0){
                   var idsStr = ids.join(",");
                   financialManagerSvc.deleteFinancialManager(idsStr,function(data){
                       vm.countCost();
                       bsWin.alert("操作成功！！");
                   });
               }
           }
       }

    }
})();
