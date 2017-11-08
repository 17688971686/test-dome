(function(){
    'use strict';
    angular.module('app').factory('addCostSvc' , addCost);
    addCost.$inject = ['bsWin' , 'financialManagerSvc' , 'expertReviewSvc' , 'assistCostCountSvc'];
    function addCost(bsWin , financialManagerSvc , expertReviewSvc , assistCostCountSvc){
        var service = {
            initAddCost : initAddCost ,     //初始化财务录入
        }
        return service;

        function initAddCost(vm , costType , object,id){
            vm.financial = {};
            //该判断用于项目签收流程中的财务办理
            if (!object.businessId) {
                object.businessId = object.signId;
                object.projectname = object.projectName;
            }

            vm.financial.businessId = object.businessId;
            vm.financial.projectName = object.projectname;

            if (costType == "REVIEW") {
                vm.windowName = "项目评审费录入";
                vm.financial.businessType = "SIGN";

            } else if (costType == "ASSIST") {
                vm.windowName = "项目协审费录入";
                vm.financial.businessType = "SIGN";
            }
            /**
             * 导出excel
             */
            vm.costExportExcel = function (){
                var fileName = vm.reviewTitle + "(" + vm.payDate + ")";
                financialManagerSvc.exportExcel(vm , vm.financial.businessId ,fileName );
            }

            /**
             * 计算总金额
             */
            vm.countCost = function(){
                var totalCost = 0;
                if(vm.financials && vm.financials.length > 0){
                    angular.forEach(vm.financials,function (f,i) {
                        if(f.charge){
                            totalCost += f.charge;
                        }
                    })
                }
                $("#financialCount").html(totalCost);
            }

            /**
             * 改变费用名称时判断是否已存在
             * @param index
             */
            vm.changeName = function (index, changeName) {
                if (vm.financials.length > 0) {
                    for (var i = 0; i < (vm.financials.length) - 1; i++) {
                        if(i != index){
                            if (vm.financials[i].chargeName != undefined && vm.financials[i].chargeName == changeName) {
                                bsWin.alert("该费用已经录入，不能重复录入！");
                                vm.financials[index] = {};
                            }
                        }
                    }
                }
            }

            //添加报销记录
            vm.addFinancial =  function () {
                var financial = {};
                financial.chargeType = "8";
                financial.businessId = vm.financial.businessId;
                financial.projectName = vm.financial.projectName;
                financial.paymentData = vm.financial.paymentData;
                if(!vm.financials){
                    vm.financials = [];
                }
                vm.financials.push(financial);
            }// end

            //保存报销记录
            vm.saveFinancial = function (){
                common.initJqValidation($("#fnFrom"));
                var isValid = $("#fnFrom").valid();
                if(isValid){
                    financialManagerSvc.savefinancial(vm.financials,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            vm.financials = data.reObj;
                            bsWin.success(data.reMsg);
                            $("#addCostWindow").data("kendoWindow").close();
                        }else{
                            bsWin.error(data.reMsg);
                        }
                    });
                }else{
                    bsWin.error("费用信息填写不正确！");
                }
                // financialManagerSvc.savefinancial(vm);
            }

            //删除报销记录
            vm.deleteFinancial = function(){
                var isChecked = $("#financialsTable input[name='financialsCheck']:checked");
                if(isChecked.length < 1){
                    bsWin.alert("请选择要删除的记录！");
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
                        financialManagerSvc.deleteFinancialManager(idsStr,function(data){

                        });
                    }
                }
            }

            $("#"+id).kendoWindow({
                width: "70%",
                height: "560",
                title: vm.windowName,
                visible: false,
                modal: true,
                closable: true,
                open: function () {
                    //初始化报销费用列表
                    financialManagerSvc.initFinancialProject(vm.financial, function(data){
                        //1、获取已经添加的费用列表
                        vm.financials = data.financiallist;
                        //如果已经有项目费用，则计算总额
                        if(vm.financials && vm.financials.length > 0){
                            vm.countCost();
                            vm.financial.paymentData = vm.financials[0].paymentData;
                        }
                        //2、查找专家评审费
                        expertReviewSvc.initReview(vm.financial.businessId, "", function (data) {
                            vm.expertReview = data;
                        });

                        //项目才有协审费，不是项目，没有协审费
                        if( costType == "ASSIST"){
                            vm.searchCost = {};
                            vm.searchCost.signId =  vm.financial.businessId;
                            assistCostCountSvc.findSingAssistCostCount(vm.searchCost,function(data) {
                                vm.signAssistCostCounList = data;
                            });
                        }
                    });
                },
                close: function () {
                    vm.financial = {};
                    vm.expertReview = {};
                    vm.financials = {};
                    vm.signAssistCostCounList = {};
                },
                refresh:function(){
                    alert(2);
                },
                actions: ["Pin", "Minimize", "Maximize", "close"]
            }).data("kendoWindow").center().open();
        }


    }

})();