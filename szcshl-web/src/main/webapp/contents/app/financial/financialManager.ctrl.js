(function () {
    'use strict';

    angular.module('app').controller('financialManagerCtrl', financialManager);

    financialManager.$inject = ['$location', 'financialManagerSvc','$state','$http'];

    function financialManager($location, financialManagerSvc,$state,$http) {
        var vm = this;
        vm.title = '财务管理';
        vm.financials = new Array;
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.financial.signid = $state.params.signid;
        
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
    	    var signid =vm.financial.signid;
        	vm.financial = {};
        	vm.financial.chargeType ="评审项目"; 
        	vm.financial.signid = signid ;
        	vm.financial.projectName= projectName;
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
            financialManagerSvc.grid(vm);
            financialManagerSvc.sumFinancial(vm);
            financialManagerSvc.initFinancialProject(vm);
        }
    }
})();