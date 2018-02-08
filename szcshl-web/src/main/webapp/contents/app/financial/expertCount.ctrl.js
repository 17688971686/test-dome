(function () {
    'use strict';

    angular.module('app').controller('exportCountCtrl', exportCount);

    exportCount.$inject = ['$location', 'exportCountSvc','$state','$http'];

    function exportCount($location, exportCountSvc,$state,$http) {
        var vm = this;
        vm.title = '专家费统计管理';
        vm.financials = new Array;
        vm.searchModel = {};
        vm.searchModel.beginTime = (new Date()).halfYearAgo();
        vm.searchModel.endTime = (new Date()).Format("yyyy-MM-dd");
        vm.model={};
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.financial.signid = $state.params.signid;
        
        //S 输入数字校验
        vm.inputIntegerValue = function(checkValue,idSort){
        	if(exportCountSvc.isUnsignedInteger(checkValue)){
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
    	    var signid =vm.financial.signid;
        	vm.financial = {};
        	vm.financial.chargeType ="评审项目"; 
        	vm.financial.signid = signid ;
        	vm.financial.projectName= projectName;
        	vm.financial.paymentData= paymentData;
            vm.financials.push(vm.financial);
            vm.i++;
        }// end
       
       //保存报销记录
       vm.saveFinancial = function (){
    	   exportCountSvc.savefinancial(vm);
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
    				 exportCountSvc.deleteexportCount(vm,idsStr);
    		   }
    	   }
       }
        //表单查询
        vm.searchForm = function(){
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        /**
         * 专家评审费明细导出
         */
        vm.excelExport = function(){
            vm.fileName = "专家评审费明细";
            // Begin:dataSource
            // End:dataSource
         //   console.log(vm.model.beginTime);
            vm.exportData = $("#statisticalGrid").data("kendoGrid")._data;
            exportCountSvc.excelExport(vm,vm.exportData,vm.fileName);
        }

        //重置查询表单
        vm.formReset = function(){
            vm.searchModel = {};
        }
        activate();
        function activate() {
            exportCountSvc.grid(vm);
        }
    }
})();
