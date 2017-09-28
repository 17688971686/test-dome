(function () {
    'use strict';

    angular.module('app').controller('expertPaymentCountCtrl', expertPaymentCount);

    expertPaymentCount.$inject = ['$location', 'expertPaymentCountSvc','$state','$http'];

    function expertPaymentCount($location, expertPaymentCountSvc,$state,$http) {
        var vm = this;
        vm.title = '专家缴税统计管理';
        vm.model={};
        vm.financials = new Array;
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.financial.signid = $state.params.signid;
        vm.model.beginTime = $state.params.beginTime;
        //S 输入数字校验
        vm.inputIntegerValue = function(checkValue,idSort){
        	if(expertPaymentCountSvc.isUnsignedInteger(checkValue)){
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
    	   expertPaymentCountSvc.savefinancial(vm);
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
    				 expertPaymentCountSvc.deleteexpertPaymentCount(vm,idsStr);
    		   }
    	   }
       }

        //按月份统计专家明细
        vm.countExpertCostDetail = function(){
            expertPaymentCountSvc.expertCostDetailTotal(vm,function(data){
                vm.expertCostTotalInfo = data.reObj.expertCostTotalInfo
             var trCount = $("#expertCostTable tr").length;
                 for(var i=1;i<trCount;i++){
                     $("#option"+i).remove();
                 }
                createExpertCostTable(vm.expertCostTotalInfo);

            });
        }

        vm.countExpertCost = function () {
            var timeArr =  vm.model.beginTime.split("-");
            vm.year = timeArr[0];
            vm.month = timeArr[1];
            expertPaymentCountSvc.expertCostTotal(vm,function(data){
                vm.expertCostTotalInfo = data.reObj.expertCostTotalInfo
            });
        }

        vm.getExpertCoustDetail = function () {
            $state.go('expertPaymentDetailCountList',{beginTime:vm.model.beginTime});
        }

        /**
         * 专家评审费统计导出
         */
        vm.excelExport = function(){
            vm.fileName = "专家评审费统计报表"+vm.model.beginTime;
            expertPaymentCountSvc.excelExport(vm,vm.expertCostTotalInfo,vm.fileName);
        }
        activate();
        function activate() {
            if(vm.model.beginTime==""){
                var date=new Date;
                var year=date.getFullYear();
                var month=date.getMonth()+1;
                month =(month<10 ? "0"+month:month);
                vm.model.beginTime = year.toString()+"-"+month.toString();
                vm.year = year;
                vm.month = month;
            }else{
               var timeArr =  vm.model.beginTime.split("-");
               vm.year = timeArr[0];
               vm.month = timeArr[1];
            }

            expertPaymentCountSvc.expertCostTotal(vm,function(data){
                vm.expertCostTotalInfo = data.reObj.expertCostTotalInfo
            });

        }

        //生成专家评审费明细表格
        function  createExpertCostTable(expertCostTotalInfo) {
            var expertCostTr="";
            var rowIndex = 0;
            if(expertCostTotalInfo.length>0){
             for(var i=0;i<expertCostTotalInfo.length;i++){
                 rowIndex++;
                 expertCostTr += "<tr id='option"+rowIndex+"'>";
                 expertCostTr += "<td colspan='5'>";
             if(expertCostTotalInfo[i].name.length==2){
                 expertCostTr +="<span style='margin-left: 6.5%;'><strong>"+expertCostTotalInfo[i].name+"</strong></span>";
             }else{
                 expertCostTr +="<span style='margin-left: 5%;'><strong>"+expertCostTotalInfo[i].name+"</strong></span>";
             }
             expertCostTr +="<span style='margin-left: 8%;'></span><strong>"+expertCostTotalInfo[i].expertNo+"</strong></span>";
             expertCostTr +="<span style='margin-left: 65%;'></span><strong>合计:</strong></span>";
             expertCostTr +="<span style='margin-left: 4%;'></span><strong>"+expertCostTotalInfo[i].monthTotal+"</strong></span>";
             expertCostTr += "</td>";
             expertCostTr += "<td class='text-center'>";
             expertCostTr += expertCostTotalInfo[i].reviewcost;
             expertCostTr += "</td>"
             expertCostTr += "<td class='text-center'>";
             expertCostTr += expertCostTotalInfo[i].reviewtaxes;
             expertCostTr += "</td>"
             expertCostTr += "</tr>";
             if(expertCostTotalInfo[i].expertCostDetailCountDtoList.length>0){
             for (var j=0;j<expertCostTotalInfo[i].expertCostDetailCountDtoList.length;j++){
                 rowIndex++;
                 var expertCostDetailTr="";
                 expertCostDetailTr += "<tr id='option"+rowIndex+"'>";
                 expertCostDetailTr += "<td class='text-center' colspan='2'>";
                 expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewTitle;
                 expertCostDetailTr += "</td>"
                 expertCostDetailTr += "<td class='text-center'>";
                 expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewType;
                 expertCostDetailTr += "</td>"
                 expertCostDetailTr += "<td class='text-center'>";
                 if(expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewDate!=undefined){
                     expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewDate;
                 }
                 expertCostDetailTr += "</td>"
                 expertCostDetailTr += "<td class='text-center'>";
                 if(expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewDate!=undefined){
                     expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewDate;
                 }
                 expertCostDetailTr += "</td>"
                 expertCostDetailTr += "<td class='text-center'>";
                 expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewcost;
                 expertCostDetailTr += "</td>"
                 expertCostDetailTr += "<td class='text-center'>";
                 expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewtaxes;
                 expertCostDetailTr += "</td>"
                 expertCostDetailTr += "</tr>";
             }
                 expertCostTr += expertCostDetailTr;
             }

             }
               $("#expertCostHead").after(expertCostTr);
             }

        }
    }
})();
