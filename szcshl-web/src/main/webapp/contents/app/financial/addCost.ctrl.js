(function () {
    'use strict';

    angular.module('app').controller('addCostCtrl', addCost);

    addCost.$inject = [ 'financialManagerSvc', '$state' ,  'bsWin' , 'expertReviewSvc' , 'adminSvc' , 'assistCostCountSvc'];

    function addCost( financialManagerSvc, $state ,  bsWin , expertReviewSvc , adminSvc ,assistCostCountSvc) {
        /* jshint validthis:true */
        var vm = this;

        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.model = {};
        vm.signAssistCost = {};//项目协审对象
        vm.isuserExist = false;
        vm.id = $state.params.id;
        vm.financial.businessId = $state.params.businessId;
        vm.costType = $state.params.costType;
        if(vm.costType == "REVIEW"){
            vm.title = '评审费统计管理';
            vm.titleName = "专家评审费";
            vm.windowName = "专家评审费录入";
        }
        if(vm.costType == "ASSIST"){
            vm.title = '协审费统计管理';
            vm.titleName = "专家协审费";
            vm.windowName = "专家协审费录入";
        }


        /**
         * 费用录入弹出框
         */
        vm.addCostWindow = function(object){
            $('#costTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
            })

            if(vm.costType == "REVIEW"){
                financialManagerSvc.sumFinancial(vm , object.businessId);
                vm.businessId = object.businessId;
                vm.projectName =  object.projectname;
            }
            if(vm.costType == "ASSIST"){
                vm.businessId = object.signId;
                vm.projectName =  object.projectName;
            }
            financialManagerSvc.initFinancialProject(vm.businessId ,  function(data){
                vm.financial = {};
                vm.financial.businessId = vm.businessId;
                vm.financial.projectName = vm.projectName;
                // vm.model.assissCost = object.totalCost;
                vm.financial.paymentData = data.financialDto.paymentData;
                expertReviewSvc.initReview(vm.financial.businessId, "", function (data) {
                    vm.expertReview = data;
                    vm.reviewTitle = data.reviewTitle;
                    vm.payDate = data.payDate;
                    vm.expertSelectedDtoList = data.expertSelectedDtoList;
                    if( vm.expertSelectedDtoList && vm.expertSelectedDtoList.length >0){
                        vm.showReviewCost = true;
                    }
                });

                vm.signAssistCost.signId =  vm.businessId;
                assistCostCountSvc.findSingAssistCostCount(vm.signAssistCost,function (data) {
                    vm.signAssistCostCounList = data;
                    if(vm.signAssistCostCounList && vm.signAssistCostCounList.length >0){
                        vm.showAssistCost = true;
                    }
                });

                vm.financials = data.financiallist;
                vm.countCost();
                $("#addCostWindow").kendoWindow({
                    width: "70%",
                    height: "600px;",
                    title: vm.windowName ,
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
                    financialManagerSvc.deleteFinancialManager(idsStr,function(data){

                    });
                }
            }
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
            vm.financial = {};
        }

        activate();
        function activate() {

            if(vm.costType == "REVIEW"){
                adminSvc.initSignList(function(data){
                    if(data.flag || data.reCode == 'ok'){
                        vm.orgDeptList = data.reObj;
                    }
                });

                financialManagerSvc.initfinancial(vm , function(data){
                    vm.stageCountList = data;
                });
            }
            if(vm.costType == "ASSIST"){
                assistCostCountSvc.findSingAssistCostList(vm.signAssistCost, function (data) {
                    vm.signAssistCostList = data;
                });
            }

        }
    }
})();
