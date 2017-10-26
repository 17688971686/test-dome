/**
 * 停用
 */
(function () {
    'use strict';

    angular.module('app').controller('financialManagerEditCtrl', financialManager);

    financialManager.$inject = ['$location', 'financialManagerSvc', '$state' , 'signSvc' , 'bsWin' , 'expertReviewSvc' , 'adminSvc'];

    function financialManager($location, financialManagerSvc, $state , signSvc , bsWin , expertReviewSvc , adminSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '评审费统计管理';
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.model = {};
        vm.isuserExist = false;
        vm.id = $state.params.id;
        vm.financial.businessId = $state.params.businessId;
      
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '评审费统计管理';
        }
        //评审费放表业务对象
        vm.businessFlag = {
       		 expertReviews : [],   	
       }
        

        /**
         * 评审费录入弹出框
         */
        vm.reviewCostWindow = function(reviewCostObject){
            financialManagerSvc.sumFinancial(vm , reviewCostObject.businessId);
            financialManagerSvc.initFinancialProject(reviewCostObject.businessId , function(data){
                vm.model = {};
                vm.model.businessId = reviewCostObject.businessId;
                vm.model.projectName = reviewCostObject.projectname;
                vm.model.assissCost = reviewCostObject.totalCost;
                vm.model.paymentData = reviewCostObject.payDate;
                // vm.model = data.financialDto;
                vm.financials = data.financiallist;

                $("#reviewCostAddWindow").kendoWindow({
                    width: "70%",
                    height: "600px;",
                    title: "评审费录入",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "close"]
                }).data("kendoWindow").center().open();
            });

        }


        /**
         * 导出excel
         */
        vm.exportExcel = function (){
            var fileName = vm.reviewTitle + "(" + vm.payDate + ")";
            financialManagerSvc.exportExcel(vm , vm.model.businessId ,fileName );
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
            common.initJqValidation($("#fnFrom"));
            var isValid = $("#fnFrom").valid();
            if(isValid){
                financialManagerSvc.savefinancial(vm.financials,function(data){
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
            // financialManagerSvc.savefinancial(vm);
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


        /**
         * 查看评审费用
         */
        vm.findStageCostTable = function(){
            expertReviewSvc.initReview(vm.model.businessId, "", function (data) {
                vm.reviewTitle = data.reviewTitle;
                vm.payDate = data.payDate;
                vm.expertSelectedDtoList = data.expertSelectedDtoList;
                $("#stageCostWindow").kendoWindow({
                    width: "70%",
                    height: "60%",
                    title: "评审费用统计表",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            } )
        }

        /**
         * 查询
         */
        vm.queryUser = function (){

            activate();
        }

        /**
         * 重置
         */
        vm.resetQuery = function(){
            vm.model = {};
        }

        activate();
        function activate() {
            adminSvc.initSignList(function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.orgDeptList = data.reObj;
                }
            });

            financialManagerSvc.initfinancial(vm , function(data){
                vm.stageCountList = data;
            });

        }
    }
})();
