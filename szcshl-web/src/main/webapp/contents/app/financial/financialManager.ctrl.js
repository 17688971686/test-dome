(function () {
    'use strict';

    angular.module('app').controller('financialManagerCtrl', financialManager);

    financialManager.$inject = ['$location', 'financialManagerSvc','$state','$http' , 'expertReviewSvc'];

    function financialManager($location, financialManagerSvc,$state,$http , expertReviewSvc) {
        var vm = this;
        vm.title = '评审费录入';
        vm.financials = new Array;
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.financial.businessId = $state.params.businessId;
      
        //跳转到评审会发放表页面
        vm.findStageCostTable = function(){
            expertReviewSvc.initReview(vm.financial.businessId , "", function (data){
                vm.expertReview = data;
                console.log(vm.expertReview);
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
        
        //检查是否为正整数
        function isUnsignedInteger(value) {
            if ((/^(\+|-)?\d+$/.test(value)) && value > 0) {
                return true;
            } else {
                return false;
            }
        }
        
        //添加报销记录
       vm.addFinancial =  function () {
    	   	var projectName = $("#projectName").val();
    	   	var paymentData = $("#paymentData").val();
    	    var businessId =vm.financial.businessId;
        	vm.financial = {};
        	vm.financial.chargeType ="评审项目"; 
        	vm.financial.businessId = businessId ;
        	vm.financial.projectName= projectName;
        	vm.financial.paymentData= paymentData;
            vm.financials.push(vm.financial);
            vm.i++;
        }// end
       
       //保存报销记录
       vm.saveFinancial = function (){
    	   financialManagerSvc.savefinancial(vm);
       }
       //删除报销记录
       vm.deleteFinancial = function(){
    	   var isChecked = $("#financialsTable input[name='financialsCheck']:checked");
    	   if(isChecked.length < 1){
    		   common.alert({
                   vm:vm,
                   msg:"请选择要删除的记录！"
               })
    	   }else{
    		   var ids = [];
    		   for(var i = 0; i <isChecked.length ;i++){
    			   vm.financials.forEach(function( f , number){
    				   if(isChecked[i].value == f.id || f.id == undefined){
    					   vm.financials.splice(number,1);
    				   }
    				   ids.push(isChecked[i].value);
    			   });
    				var idsStr = ids.join(",");
    				 financialManagerSvc.deleteFinancialManager(vm,idsStr);
    		   }
    	   }
       }
    
        activate();
        function activate() {
            financialManagerSvc.sumFinancial(vm);
            financialManagerSvc.initFinancialProject(vm);
        }
    }
})();
