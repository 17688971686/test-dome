(function () {
    'use strict';

    angular.module('app').controller('assistCostCountCtrl', assistCostCount);

    assistCostCount.$inject = ['$location', 'assistCostCountSvc','$state','$http'];

    function assistCostCount($location, assistCostCountSvc,$state,$http) {
        var vm = this;
        vm.title = '协审费用统计管理';
        vm.financials = new Array;
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.financial.signid = $state.params.signid;
        
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
    	    var signid =vm.financial.signid;
        	vm.financial = {};
        	vm.financial.chargeType ="协审费用录入"; 
        	vm.financial.signid = signid ;
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
