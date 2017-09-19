/**
 * Created by ldm on 2017/9/5 0005.
 */
(function () {
    'use strict';

    angular.module('app').controller('flowDealCtrl', flowDeal);

    flowDeal.$inject = ['ideaSvc', '$state', 'bsWin', 'topicSvc', 'flowSvc', 'bookBuyBusinessSvc','expertReviewSvc','assertStorageBusinessSvc'];


    function flowDeal(ideaSvc, $state, bsWin, topicSvc, flowSvc, bookBuyBusinessSvc,expertReviewSvc,assertStorageBusinessSvc) {
        var vm = this;
        vm.title = '待办任务处理';
        vm.businessKey = $state.params.businessKey;            // 业务ID
        vm.processKey = $state.params.processKey;              // 流程定义值
        vm.taskId = $state.params.taskId;                      // 任务ID
        vm.instanceId = $state.params.instanceId;              // 流程实例ID
        vm.currentFlow;//当前流程信息
        vm.showFlag = {
            businessNext: false,                              //是否显示下一环节处理人tr
            businessTr: false                              //是否显示业务处理tr
        }

        activate();
        function activate() {
            $('#myTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
            })
            //共用方法
            //1、显示流程图
            vm.picture = rootPath + "/flow/processInstance/img/" + vm.instanceId;
            //2、历史处理记录
            flowSvc.historyData(vm);
            //3、查询当前环节信息
            flowSvc.getFlowInfo(vm.taskId, vm.instanceId, function (data) {
                vm.flow = data;
                vm.currentFlow = data;
                //如果是结束环节，则不显示下一环节信息
                if (vm.flow.end) {
                    vm.showFlag.nodeNext = false;
                } else {
                    //初始化环节信息
                    switch (vm.processKey) {
                        case flowcommon.getFlowDefinedKey().TOPIC_FLOW:
                            topicSvc.initFlowNode(vm.flow, vm.showFlag, vm);
                            break;
                    }
                }
            });
            //4、各自显示模块
            switch (vm.processKey) {
                case flowcommon.getFlowDefinedKey().TOPIC_FLOW:     //课题研究流程
                    topicSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().ASSERT_STORAGE_FLOW:
                    assertStorageBusinessSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().BOOKS_BUY_FLOW:     //图书采购流程
                    bookBuyBusinessSvc.initFlowDeal(vm);
                    break;
            }
        }

        /***************  S_初始化附件上传控件  ***************/
        vm.initFileUpload = function (mainType, sysfileType, sysBusiType) {
            vm.sysFile = {
                businessId: vm.businessKey,
                mainId: vm.businessKey,
                mainType: mainType,
                sysfileType: sysfileType,
                sysBusiType: sysBusiType,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm
            });
        }
        //附件下载
        vm.commonDownloadSysFile = function (sysFileId) {
            sysfileSvc.downloadFile(sysFileId);
        }
        /***************  E_初始化附件上传控件  ***************/

        /***************  S_个人意见  ***************/
        vm.ideaEdit = function (options) {
            if (!angular.isObject(options)) {
                options = {};
            }
            ideaSvc.initIdeaData(vm, options);
        }
        vm.selectedIdea = function () {
            vm.flow.dealOption = vm.chooseIdea;
        }
        /***************  E_个人意见  ***************/

        /***************  S_流程处理  ***************/
        vm.commitFlow = function () {
            if (vm.flow.isSuspended) {
                bsWin.error("该流程目前为暂停状态，不能进行流转操作！");
                return;
            }
            flowSvc.commit(vm.isCommit, vm.flow, function (data) {
                if (data.flag || data.reCode == "ok") {
                    bsWin.success("操作成功！", function () {
                        $state.go('agendaTasks');
                    })
                } else {
                    bsWin.alert(data.reMsg);
                }
            });
        }

        //S_流程回退
        vm.backFlow = function () {
            common.initJqValidation($("#flow_form"));
            var isValid = $("#flow_form").valid();
            if (isValid) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认回退吗？",
                    onOk: function () {
                        flowSvc.rollBackToLast(vm.flow, vm.isCommit, function (data) {
                            vm.isCommit = false;
                            if (data.flag || data.reCode == "ok") {
                                bsWin.alert("回退成功！", function () {
                                    $state.go('agendaTasks');
                                });
                            } else {
                                bsWin.alert(data.reMsg);
                            }
                        }); // 回退到上一个环节
                    }
                });
            }
        }
        /***************  E_流程处理  ***************/

        /***************  S_专家评分，评审费发放  ***************/
        // 编辑专家评分
        vm.editSelectExpert = function (id) {
            vm.scoreExpert = {};
            $.each(vm.model.expertReviewDto.expertSelectedDtoList,function (i,scopeEP) {
                if(scopeEP.id == id){
                    vm.scoreExpert = scopeEP;
                    return ;
                }
            })

            $("#star").raty({
                score: function () {
                    $(this).attr("data-num", angular.isUndefined(vm.scoreExpert.score)?0:vm.scoreExpert.score);
                    return $(this).attr("data-num");
                },
                starOn: '../contents/libs/raty/lib/images/star-on.png',
                starOff: '../contents/libs/raty/lib/images/star-off.png',
                starHalf: '../contents/libs/raty/lib/images/star-half.png',
                readOnly: false,
                halfShow: true,
                size: 34,
                click: function (score, evt) {
                    vm.scoreExpert.score = score;
                }
            });

            $("#score_win").kendoWindow({
                width: "820px",
                height: "365px",
                title: "编辑-专家星级",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        // 关闭专家评分
        vm.closeEditMark = function () {
            window.parent.$("#score_win").data("kendoWindow").close();
        }

        // 保存专家评分
        vm.saveMark = function () {
            common.initJqValidation($('#expert_score_form'));
            var isValid = $('#expert_score_form').valid();
            if(isValid){
                expertReviewSvc.saveMark(vm.scoreExpert,function(){
                    bsWin.success("保存成功！",function(){
                        vm.closeEditMark();
                    });
                });
            }else{
                bsWin.alert("请填写评分和评分内容！");
            }
        }

        // 计算应纳税额
        vm.countTaxes = function (expertReview) {
            if(expertReview == undefined){
                return ;
            }
            if(expertReview.payDate == undefined){
                bsWin.alert("请选择评审费发放日期");
                return ;
            }
            var reg = /^(\d{4}-\d{1,2}-\d{1,2})$/;
            if(!reg.exec(expertReview.payDate)){
                bsWin.alert("请输入正确的日期格式");
                return ;
            }
            if (expertReview.expertSelectedDtoList == undefined || expertReview.expertSelectedDtoList.length == 0) {
                bsWin.alert("该方案还没评审专家");
                return;
            }
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if(isValid){
                var len = expertReview.expertSelectedDtoList.length, ids = '', month;
                $.each(expertReview.expertSelectedDtoList,function (i,v) {
                    ids += "'" + v.id + "'";
                    if (i != (len - 1)) {
                        ids += ",";
                    }
                })
                var payDate = expertReview.payDate;
                month = payDate.substring(0, payDate.lastIndexOf('-'));
                expertReviewSvc.countTaxes(ids,month,function (data) {
                    var allExpertCost = data;
                    expertReview.reviewCost = 0;
                    expertReview.reviewTaxes = 0;
                    expertReview.totalCost = 0;

                    $.each(expertReview.expertSelectedDtoList,function(i,v){
                        var expertId = v.EXPERTID;
                        var expertSelectedId = v.id;
                        var totalCost = 0;
                        //console.log("计算专家:"+expertDto.name);
                        if (allExpertCost != undefined && allExpertCost.length > 0) {
                            //累加专家改月的评审费用
                            allExpertCost.forEach(function (v, i) {
                                if (v.EXPERTID == expertId && v.ESID != expertSelectedId) {
                                    v.REVIEWCOST = v.REVIEWCOST == undefined ? 0 : v.REVIEWCOST;
                                    v.REVIEWCOST = parseFloat(v.REVIEWCOST);
                                    totalCost = parseFloat(totalCost) + v.REVIEWCOST;
                                }
                            });
                        }
                        //console.log("专家当月累加:" + totalCost);
                        //计算评审费用
                        v.reviewCost = v.reviewCost == undefined ? 0 : v.reviewCost;
                        var reviewTaxesTotal = totalCost + parseFloat(v.reviewCost);
                        //console.log("专家当月累加加上本次:" + reviewTaxesTotal);
                        v.reviewTaxes = countNum(reviewTaxesTotal).toFixed(2);
                        v.totalCost = (parseFloat(v.reviewCost) + parseFloat(v.reviewTaxes)).toFixed(2);
                        expertReview.reviewCost = (parseFloat(expertReview.reviewCost) + parseFloat(v.reviewCost)).toFixed(2);
                        expertReview.reviewTaxes = (parseFloat(expertReview.reviewTaxes) + parseFloat(v.reviewTaxes)).toFixed(2);
                        expertReview.totalCost = (parseFloat(expertReview.reviewCost) + parseFloat(expertReview.reviewTaxes)).toFixed(2);
                    });
                });
            }
        }

        // S_countNum
        function countNum(reviewCost) {
            reviewCost = reviewCost == undefined ? 0 : reviewCost;
            var reviewTaxes = 0;
            if (reviewCost > 800 && reviewCost <= 4000) {
                reviewTaxes = (reviewCost - 800) * 0.2;
            } else if (reviewCost > 4000 && reviewCost <= 20000) {
                reviewTaxes = reviewCost * (1 - 0.2) * 0.2
            } else if (reviewCost > 20000 && reviewCost <= 50000) {
                reviewTaxes = reviewCost * (1 - 0.2) * 0.3 - 2000;
            } else if (reviewCost > 50000) {
                //待确认
            }
            return reviewTaxes;
        }// E_countNum

        // 关闭专家费用
        vm.closeEditPay = function () {
            window.parent.$("#payment").data("kendoWindow").close();
        }

        // 保存专家费用
        vm.savePayment = function (expertReview) {
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if (isValid) {
                expertReviewSvc.savePayment(expertReview,vm.isCommit,function(data){
                    if(data.flag || data.reCode == "ok"){
                        bsWin.alert("操作成功！",function(){
                            vm.isCommit = false;
                        });
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("请正确填写专家评审费信息！");
            }
        }
        /***************  E_专家评分，评审费发放  ***************/
    }
})();

