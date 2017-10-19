(function () {
    'use strict';

    angular.module('app').controller('assistCostCountCtrl', assistCostCount);

    assistCostCount.$inject = ['$location', 'assistCostCountSvc','expertReviewSvc','$state','$http'];

    function assistCostCount($location, assistCostCountSvc,expertReviewSvc,$state,$http) {
        var vm = this;
        vm.title = '协审费用统计';
        vm.financials = new Array();
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.financial.businessId = $state.params.signid;
        
        //导出专家协审费用表
        vm.assistExportExcel =function(){
        	var fileName = vm.reviewTitle + "(" + vm.payDate + ")";
        	assistCostCountSvc.assistExportExcel(vm , vm.financial.businessId ,fileName );
        }
        
        //查看协审费用发放表
        vm.findAssistCostTable = function(){
        	 expertReviewSvc.initReview(vm.financial.businessId , "", function (data){
                 vm.reviewTitle = data.reviewTitle;
                 vm.payDate = data.payDate;
                 vm.expertSelectedDtoList = data.expertSelectedDtoList;
                 $("#assistCostWindow").kendoWindow({
                     width: "70%",
                     height: "600px",
                     title: "协审费用统计表",
                     visible: false,
                     modal: true,
                     closable: true,
                     actions: ["Pin", "Minimize", "Maximize", "Close"]
                 }).data("kendoWindow").center().open();
             } )
        	//$state.go('findAssistCostTable',{signid: vm.financial.businessId});
        }
        //S 输入数字校验
        vm.inputIntegerValue = function(checkValue,idSort){
        	if(assistCostCountSvc.isUnsignedInteger(checkValue)){
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
        
        //S 协审费用录入
       vm.assistCostAdd =  function () {
    	   	var projectName = $("#projectName").val();
    	   	var paymentData = $("#paymentData").val();
    	    var businessId =vm.financial.businessId;
        	vm.financial = {};
        	vm.financial.chargeType ="协审费用录入"; 
        	vm.financial.businessId = businessId ;
        	vm.financial.projectName= projectName;
        	vm.financial.paymentData= paymentData;
            vm.financials.push(vm.financial);
            vm.i++;
        }
       //E 协审费用录入
       
       //保存报销记录
       vm.saveAssistCost= function (){
    	   assistCostCountSvc.saveAssistCost(vm);
       }
       //删除协审费用记录
       vm.deleteAssistCost = function(){
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
    				 assistCostCountSvc.deleteassistCostCount(vm,idsStr);
    		   }
    	   }
       }
    
        activate();
        function activate() {
            assistCostCountSvc.sumAssistCount(vm);
            assistCostCountSvc.initAssistlProject(vm);
        }
    }
})();
