(function () {
    'use strict';

    angular.module('app').controller('assistCostCountEditCtrl', assistCostCount);

    assistCostCount.$inject = ['bsWin', 'assistCostCountSvc', 'expertReviewSvc', '$http'];

    function assistCostCount(bsWin, assistCostCountSvc, expertReviewSvc, $http) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '协审费录入';
        vm.signAssistCost = {};
        vm.financials = [];

        activate();
        function activate() {
            assistCostCountSvc.findSingAssistCostList(vm.signAssistCost, function (data) {
                vm.signAssistCostList = data;
            });
        }

        //查询
        vm.queryAssistCost = function () {
            activate();
        }
        //重置
        vm.assistCostReset = function () {
            vm.signAssistCost = {};
        }

        /**
         * 添加费用弹出框
         */
        vm.addCost = function (signAssistCostObj) {
            assistCostCountSvc.initAssistlProject(signAssistCostObj.signId, function (data) {
                vm.model = {};
                vm.model.businessId = signAssistCostObj.signId;
                vm.model.projectName = signAssistCostObj.projectName;
                vm.model.assistUnit = signAssistCostObj.assistUnit;
                vm.model.assissCost = signAssistCostObj.planCost;
                vm.model.paymentData = signAssistCostObj.payDate;

                vm.financials = data.financiallist;
                //assistCostCountSvc.sumAssistCount(vm);
                //直接在前端计算总额
                vm.countCost();
                $("#addCostWindow").kendoWindow({
                    width: "70%",
                    height: "600px;",
                    title: "协审费录入",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "close"]
                }).data("kendoWindow").center().open();
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
                $("#financialCount").html(totalCost);
            }
        }
        //查看协审费用发放表
        vm.findAssistCostTable = function () {
            expertReviewSvc.initReview(vm.model.businessId, "", function (data) {
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
            })
            //$state.go('findAssistCostTable',{signid: vm.financial.businessId});
        }

        //S 协审费用录入
        vm.assistCostAdd = function () {
            var financial = {};
            financial.chargeType = "8";
            financial.businessId = vm.model.businessId;
            financial.projectName = vm.model.projectName;
            financial.paymentData = vm.model.paymentData;
            if(!vm.financials){
                vm.financials = [];
            }
            vm.financials.push(financial);
        }
        //E 协审费用录入

        //保存报销记录
        vm.saveAssistCost = function () {
            common.initJqValidation($("#fnFrom"));
            var isValid = $("#fnFrom").valid();
            if(isValid){
                assistCostCountSvc.saveAssistCost(vm.financials,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        vm.financials = data.reObj;
                        bsWin.success(data.reMsg);
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }else{
                bsWin.error("费用信息填写不正确！");
            }
        }

        //删除协审费用记录
        vm.deleteAssistCost = function () {
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
                    assistCostCountSvc.deleteassistCostCount(idsStr,function(data){
                        bsWin.alert("操作成功！！");
                    });
                }
            }
        }

        /**
         * 改变费用名称时判断是否已存在
         * @param index
         */
        vm.changeName = function (index, changeName) {
            if (vm.financials.length > 0) {
                for (var i = 0; i < (vm.financials.length) - 1; i++) {
                    if (vm.financials[i].chargeName != undefined && vm.financials[i].chargeName == changeName) {
                        bsWin.alert("该费用已经录入，不能重复录入！");
                        vm.financials[index] = {};
                    }
                }
            }
        }
    }
})();
