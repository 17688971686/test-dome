(function () {
    'use strict';

    angular.module('app').controller('assistCostCountEditCtrl', assistCostCount);

    assistCostCount.$inject = ['$location', 'assistCostCountSvc', '$state','$http'];

    function assistCostCount($location, assistCostCountSvc, $state,$http) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '协审费录入';
        vm.signAssistCost = {};
        vm.i = 0;
        activate();
        function activate() {
            assistCostCountSvc.findSingAssistCostList(vm.signAssistCost,function (data) {
                vm.signAssistCostList = data;
            });
        }

        //查询
        vm.queryAssistCost = function(){
        	assistCostCountSvc.assistCostCountList(vm ,function(data){
        		 vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
        	});
        }
        //重置
        vm.assistCostReset = function(){
        	vm.model = {};
        }
        
        vm.lightState = function(lightState){
            switch (lightState) {
                case "4":          //暂停
                    return $('#span1').html();
                    break;
                case "8":         	//存档超期
                    return $('#span5').html();
                    break;
                case "7":           //超过25个工作日未存档
                    return $('#span4').html();
                    break;
                case "6":          	//发文超期
                    return $('#span3').html();
                    break;
                case "5":          //少于3个工作日
                    return $('#span2').html();
                    break;
                case "1":          //在办
                    return "";
                    break;
                case "2":           //已发文
                    return "";
                    break;
                case "3":           //已发送存档
                    return "";
                    break;
                default:
                    return "";
                    ;

        vm.create = function () {
            assistCostCountSvc.createassistCostCount(vm);
        };
        vm.update = function () {
            assistCostCountSvc.updateassistCostCount(vm);
        };

        /**
         * 添加费用弹出框
         */
        vm.addCost = function(signId){
            vm.financial.businessId = signId;
            vm.signId =signId;
            assistCostCountSvc.initAssistlProject(vm , function(data){
                vm.model = data.financialDto;
                vm.financials = data.financiallist;

                assistCostCountSvc.sumAssistCount(vm);

                $("#addCostWindow").kendoWindow({
                    width: "70%",
                    height: "60%",
                    title: "协审费录入",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "close"]
                }).data("kendoWindow").center().open();
            });

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
            vm.i = vm.financials.length;
            var projectName = $("#projectName").val();
            var paymentData = $("#paymentData").val();
            // var businessId =vm.financial.businessId;
            vm.financial = {};
            vm.financial.chargeType ="协审费用录入";
            vm.financial.signid = vm.signId ;
            vm.financial.projectName= projectName;
            vm.financial.paymentData= paymentData;
            if(vm.i == 0){
                vm.financials.push(vm.financial);
                vm.i++;
            }else{
                if(vm.financials[vm.i -1].chargeName !=undefined){
                    vm.financials.push(vm.financial);
                    vm.i++;
                }
            }



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

        /**
         * 改变费用名称时判断是否已存在
         * @param index
         */
        vm.changeName = function(index  ,changeName){
            if(vm.financials.length >0){
                for(var i=0 ; i<(vm.financials.length)-1 ; i++){
                    if( vm.financials[i].chargeName !=undefined && vm.financials[i].chargeName == changeName){
                        bsWin.alert("该费用已经录入，不能重复录入！");
                        vm.financials[index] = {};
                    }
                }
            }

        }


            //协审费统计列表
            assistCostCountSvc.assistCostCountList(vm,function(data){
                vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
            });

            //协审费录入列表
            assistCostCountSvc.assistCostList(vm,function(data){
                vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
            });
        }
    }
})();
