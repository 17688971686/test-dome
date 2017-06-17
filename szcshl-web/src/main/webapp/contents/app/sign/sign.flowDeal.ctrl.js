(function () {
    'use strict';

    angular.module('app').controller('signFlowDealCtrl', sign);

    sign.$inject = ['$location', 'signSvc', '$state', 'flowSvc', 'signFlowSvc',
        '$http'];

    function sign($location, signSvc, $state, flowSvc, signFlowSvc, $http) {
        var vm = this;
        vm.title = "项目流程处理";
        vm.model = {};
        vm.flow = {};
        vm.work = {};
        vm.dispatchDoc = {};
        vm.fileRecord = {};
        vm.model.signid = $state.params.signid;
        vm.flow.taskId = $state.params.taskId; // 流程任务ID
        vm.flow.processInstanceId = $state.params.processInstanceId; // 流程实例ID
        vm.dealFlow = true;
        vm.expertReview = {};
        vm.showExpertRemark = false;// 专家评分弹窗内容显示
        vm.showExpertpayment = false;// 专家费用弹窗内容显示
        vm.MarkAndPay = true;// 专家评分费用编辑权限

        active();
        function active() {
            $('#myTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in")
                    .show(500);
            })

            // 先初始化流程信息
            flowSvc.initFlowData(vm);
            // 再初始化业务信息
            signSvc.initFlowPageData(vm);
            signSvc.initAssociateSigns(vm,vm.model.signid);
            // 初始化专家评分费用
            //flowSvc.markGrid(vm);
            //flowSvc.paymentGrid(vm);
            //初始化项目关联弹窗
             signSvc.associateGrid(vm);
        }

        // 编辑专家评分
        vm.editSelectExpert = function (id, score) {
            vm.expertReview.score = score;
            vm.expertReview.expertId = id;
            flowSvc.gotoExpertmark(vm);
        }
        // 关闭专家评分
        vm.closeEditMark = function () {
            window.parent.$("#expertmark").data("kendoWindow").close();
        }

        // 保存专家评分
        vm.saveMark = function () {
            flowSvc.saveMark(vm);
        }

        vm.editpayment = function (id) {
            vm.expertReview.expertId = id;
            flowSvc.gotopayment(vm);
        }
        // 计算应纳税额
        vm.countTaxes = function () {
            flowSvc.countTaxes(vm);
        }
        // 关闭专家费用
        vm.closeEditPay = function () {
            window.parent.$("#payment").data("kendoWindow").close();
        }
        // 保存专家费用
        vm.savePayment = function () {
            flowSvc.savePayment(vm);
        }
        // begin 添加审批意见
        vm.ideaEdit = function (options) {
            console.log(options);
            common.initIdeaData(vm, $http, options);
        }
        // end 添加审批意见

        //流程提交
        vm.commitNextStep = function () {
            if (signFlowSvc.checkBusinessFill(vm)) {
                flowSvc.commit(vm);
            } else {
                common.alert({
                    vm: vm,
                    msg: "请先完成相应的业务操作才能提交"
                })
            }
        }

        vm.commitBack = function () {
            flowSvc.rollBack(vm); // 回退到上一个环节
        }

        vm.deleteFlow = function () {
            common.confirm({
                vm: vm,
                title: "",
                msg: "终止流程将无法恢复，确认挂起么？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    flowSvc.deleteFlow(vm);
                }
            })
        }

        vm.initDealUerByAcitiviId = function () {
            flowSvc.initDealUerByAcitiviId(vm);
        }

        // 根据特定的环节隐藏相应的业务按钮
        vm.showBtByAcivitiId = function (acivitiId) {
            return vm.flow.curNodeAcivitiId == acivitiId ? true : false;
        }

        // S_跳转到 工作方案 编辑页面
        vm.addWorkProgram = function () {
            $state.go('workprogramEdit', {signid: vm.model.signid });
        }// E_跳转到 工作方案 编辑页面

        //S_跳转到 工作方案 基本信息
        vm.addBaseWP = function(){
            $state.go('workprogramBaseEdit', {signid: vm.model.signid });
        }

        // S_跳转到 发文 编辑页面
        vm.addDisPatch = function () {
            $state.go('dispatchEdit', {
                signid: vm.model.signid
            });
        }// E_跳转到 发文 编辑页面

        vm.addDoFile = function () {
            $state.go('fileRecordEdit', {
                signid: vm.model.signid
            });
        }

        // 业务判断
        vm.checkBox = function ($event, type, disabletype) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('.seleteTable input[selectType=\"' + type + '\"]').each(
                    function () {
                        var id = $(this).attr("id");
                        var value = $(this).attr("value");
                        if (id != (type + "_" + checkboxValue)) {
                            $("#" + disabletype + "_" + value)
                                .removeAttr("disabled");
                            $(this).removeAttr("checked");
                        } else {
                            $("#" + disabletype + "_" + checkboxValue)
                                .attr("disabled", "disabled");
                        }
                    });
            } else {
                $("#" + disabletype + "_" + checkboxValue)
                    .removeAttr("disabled");
            }
        }

        // checkbox 单选
        vm.checkBoxSingle = function ($event, type) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('.seleteTable input[selectType=\"' + type + '\"]').each(function () {
                    var id = $(this).attr("id");
                    var value = $(this).attr("value");
                    if (id != (type + "_" + checkboxValue)) {
                        $("#" + disabletype + "_" + value).removeAttr("disabled");
                        $(this).removeAttr("checked");
                    } else {
                        $("#" + disabletype + "_" + checkboxValue).attr("disabled", "disabled");
                    }
                });
            } else {
                $("#" + disabletype + "_" + checkboxValue).removeAttr("disabled");
            }
        }

        //checkbox 单选
        vm.checkBoxSingle = function ($event, type) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('#xs_table input[selectType=\"' + type + '\"]').each(function () {
                    var id = $(this).attr("id");
                    var value = $(this).attr("value");
                    if (value != checkboxValue) {
                        $(this).removeAttr("checked");
                    }
                });
            }
        }

        //项目关联弹窗
        vm.showAssociate = function(){
            vm.currentAssociateSign = vm.model;
            signSvc.showAssociateSign();
        }

        //start 保存项目关联
        vm.saveAssociateSign = function(associateSignId){
            var signid = vm.model.signid;
            if(signid == associateSignId){
                common.alert({
                    vm:vm,
                    msg:"不能关联自身项目",
                    closeDialog:true,
                    fn:function() {
                    }
                });
                return ;
            }
            signSvc.saveAssociateSign(vm,signid,associateSignId,function(){
                //回调
                $state.reload();
            });
        }
        //end 保存项目关联

        //start 解除项目关联
        vm.disAssociateSign = function(){
            signSvc.saveAssociateSign(vm,vm.model.signid,null,function(){
                 //回调
                 $state.reload();
            });
        }
        //end 解除项目关联

    }
})();
