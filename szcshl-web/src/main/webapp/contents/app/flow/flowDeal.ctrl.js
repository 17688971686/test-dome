/**
 * Created by ldm on 2017/9/5 0005.
 */
(function () {
    'use strict';

    angular.module('app').controller('flowDealCtrl', flowDeal);

    flowDeal.$inject = ['$scope','ideaSvc','sysfileSvc', '$state', 'bsWin', 'topicSvc', 'flowSvc', 'bookBuyBusinessSvc','expertReviewSvc','assertStorageBusinessSvc','pauseProjectSvc','archivesLibrarySvc','reviewProjectAppraiseSvc','addSuppLetterSvc','monthlyMultiyearSvc','annountmentSvc'];

    function flowDeal($scope,ideaSvc,sysfileSvc, $state, bsWin, topicSvc, flowSvc, bookBuyBusinessSvc,expertReviewSvc,assertStorageBusinessSvc,pauseProjectSvc,archivesLibrarySvc,reviewProjectAppraiseSvc,addSuppLetterSvc,monthlyMultiyearSvc,annountmentSvc) {
        var vm = this;
        vm.title = '待办任务处理';
        vm.businessKey = $state.params.businessKey;            // 业务ID
        vm.processKey = $state.params.processKey;              // 流程定义值
        vm.taskId = $state.params.taskId;                      // 任务ID
        vm.instanceId = $state.params.instanceId;              // 流程实例ID
        vm.currentFlow;//当前流程信息
        vm.showFlag = {
            businessNext: false,                              //是否显示下一环节处理人tr
            businessTr: false,                                //是否显示业务处理tr
            tabSysFile:false,                                 //是否显示附件tab
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
                //有任务ID，说明任务还没办理
                if(vm.flow.taskId){
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
                }else{
                    bsWin.alert("该任务已处理！",function(){
                        $state.go('gtasks');
                    });
                }
            });
            //附件加载类型，0表示不加载，1表示根据mainId加载，2表示根据businessId加载
            var sysFileLoadType = 0;
            //4、各自显示模块
            switch (vm.processKey) {
                case flowcommon.getFlowDefinedKey().TOPIC_FLOW:     //课题研究流程
                    topicSvc.initFlowDeal(vm);
                    sysFileLoadType = 1;
                    break;
                case flowcommon.getFlowDefinedKey().ASSERT_STORAGE_FLOW:    //资产入库流程
                    assertStorageBusinessSvc.initFlowDeal(vm);
                    sysFileLoadType = 1;
                    break;
                case flowcommon.getFlowDefinedKey().BOOKS_BUY_FLOW:         //图书采购流程
                    bookBuyBusinessSvc.initFlowDeal(vm);
                    sysFileLoadType = 1;
                    break;
                case flowcommon.getFlowDefinedKey().PROJECT_STOP_FLOW:      //项目暂停流程
                    pauseProjectSvc.initFlowDeal(vm);
                    sysFileLoadType = 2;
                    break;
                case flowcommon.getFlowDefinedKey().FLOW_ARCHIVES:          //档案借阅流程
                    archivesLibrarySvc.initFlowDeal(vm);
                    sysFileLoadType = 1;
                    break;
                case flowcommon.getFlowDefinedKey().FLOW_APPRAISE_REPORT:   //优秀申请报告流程
                    reviewProjectAppraiseSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().FLOW_SUPP_LETTER:       //拟补充资料函流程
                    addSuppLetterSvc.initFlowDeal(vm);
                    sysFileLoadType = 2;
                    break;
                case flowcommon.getFlowDefinedKey().MONTHLY_BULLETIN_FLOW:       //月报简报流程
                    monthlyMultiyearSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().ANNOUNT_MENT_FLOW:       //通知公告
                    annountmentSvc.initFlowDeal(vm);
                    sysFileLoadType = 1;
                    break;
            }
            // 初始化上传附件
            if(sysFileLoadType == 1){
                sysfileSvc.findByMianId(vm.businessKey,function(data){
                    if(data && data.length > 0){
                        vm.showFlag.tabSysFile = true;
                        vm.sysFileList = data;
                        sysfileSvc.initZtreeClient(vm,$scope);//树形图
                    }
                });
            }else if(sysFileLoadType == 2){
                sysfileSvc.findByBusinessId(vm.businessKey,function(data){
                    if(data && data.length > 0){
                        vm.showFlag.tabSysFile = true;
                        vm.sysFileList = data;
                        sysfileSvc.initZtreeClient(vm,$scope);//树形图
                    }
                });
            }
            //5、初始化个人常用意见
            ideaSvc.initIdea(vm);
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
                    vm.scoreExpert = angular.copy(scopeEP);
                    return ;
                }
            })

            $("#star_"+vm.scoreExpert.id).raty({
                number:5,
                score: function () {
                    $(this).attr("data-num", angular.isUndefined(vm.scoreExpert.score)?0:vm.scoreExpert.score);
                    return $(this).attr("data-num");
                },
                starOn: '../contents/libs/raty/lib/images/star-on.png',
                starOff: '../contents/libs/raty/lib/images/star-off.png',
                starHalf: '../contents/libs/raty/lib/images/star-half.png',
                readOnly: false,
                halfShow: true,
                hints   : ['不合格','合格','中等','良好','优秀'],
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
                actions: ["Close"]
            }).data("kendoWindow").center().open();
        }

        // 关闭专家评分
        vm.closeEditMark = function () {
            window.parent.$("#score_win").data("kendoWindow").close();
        }

        // 保存专家评分
        vm.saveMark = function () {
            if(!vm.scoreExpert.score || vm.scoreExpert.score == 0){
                bsWin.alert("请对专家进行评分！");
            }else{
                expertReviewSvc.saveMark(vm.scoreExpert,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        angular.forEach(vm.model.expertReviewDto.expertSelectedDtoList,function (scopeEP,index) {
                            if(scopeEP.id == vm.scoreExpert.id){
                                scopeEP.score = vm.scoreExpert.score;
                                scopeEP.describes = vm.scoreExpert.describes;
                            }
                        })
                        bsWin.success("保存成功！",function(){
                            vm.closeEditMark();
                        });
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }
        }

        //确定实际参加会议的专家
        vm.affirmJoinExpert = function () {
            if(vm.model.expertReviewDto && vm.model.expertReviewDto.expertSelectedDtoList){
                vm.confirmEPList = vm.model.expertReviewDto.expertSelectedDtoList;
                $("#confirmJoinExpert").kendoWindow({
                    width: "75%",
                    height: "600px",
                    title: "参加评审会专家确认",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            }else{
                bsWin.alert("该项目没有评审专家！");
            }
        }

        //未参加改为参加
        vm.updateToJoin = function () {
            var isCheck = $("#notJoinExpertTable input[name='notJoinExpert']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要改为参加会议的专家");
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState(vm.expertReviewDto.id,"","", ids.join(','), '9',vm.isCommit,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        //1、更改专家评分和评审费发放的专家
                        vm.reFleshJoinState(ids,'9');
                        bsWin.success("操作成功！");
                    }else{
                        bsWin.success(data.reMsg);
                    }
                });
            }
        }

        //参加改为未参加
        vm.updateToNotJoin = function () {
            var isCheck = $("#joinExpertTable input[name='joinExpert']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择未参加会议的专家");
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState(vm.expertReviewDto.id,"","", ids.join(','), '0',vm.isCommit,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        vm.reFleshJoinState(ids,'0');
                        bsWin.success("操作成功！");
                    }else{
                        bsWin.success(data.reMsg);
                    }
                });
            }
        }

        //更新参加未参加状态
        vm.reFleshJoinState = function(ids,state){
            $.each(ids,function(i, obj){
                //1、删除已确认的专家
                $.each(vm.confirmEPList,function(index, epObj){
                    if(obj == epObj.id){
                        epObj.isJoin = state;
                    }
                })
            })
            expertReviewSvc.refleshBusinessEP(vm.model.workPlanDto.id,function(data){
                vm.model.workPlanDto.expertDtoList = data;
            });
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
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认提交么？提交之后不可修改，请核对正确。",
                    onOk: function () {
                        //自动保存
                        expertReviewSvc.savePayment(expertReview, vm.isCommit,true, function (data) {
                            if (data.flag || data.reCode == "ok") {
                                var resultMap = data.reObj, totalCost = 0, totaoTaxes = 0;
                                for (var k = 0, lk = expertReview.expertSelectedDtoList.length; k < lk; k++) {
                                    var v = expertReview.expertSelectedDtoList[k];
                                    if ((v.isConfrim == '9' || v.isConfrim == 9) && (v.isJoin == '9' || v.isJoin == 9)) {
                                        for (var i = 0, l = resultMap.length; i < l; i++) {
                                            var epId = resultMap[i].EXPERTID;
                                            if (v.expertDto.expertID == epId) {
                                                v.reviewTaxes = parseFloat(resultMap[i].MONTAXES == undefined ? 0 : resultMap[i].MONTAXES).toFixed(2);
                                                v.totalCost = (parseFloat(v.reviewCost) + parseFloat(v.reviewTaxes)).toFixed(2);
                                                totalCost = parseFloat(totalCost) + parseFloat(v.reviewCost);
                                                totaoTaxes = parseFloat(totaoTaxes) + parseFloat(v.reviewTaxes);
                                            }
                                        }
                                    }
                                }

                                expertReview.reviewCost = parseFloat(totalCost).toFixed(2);
                                expertReview.reviewTaxes = parseFloat(totaoTaxes).toFixed(2);
                                expertReview.totalCost = (parseFloat(totalCost) + parseFloat(totaoTaxes)).toFixed(2);
                                bsWin.alert("操作成功！", function () {
                                    vm.isCommit = false;
                                    expertReview.state = '9';
                                });
                            } else {
                                bsWin.alert(data.reMsg);
                            }
                        });
                    }
                });
            }else{
                bsWin.alert("请正确填写专家评审费信息！");
            }

        }

        /**
         * 保存评审费，不包括计税
         * @param expertReview
         */
        vm.savePayment = function(expertReview){
            if (expertReview == undefined) {
                return;
            }
            if (expertReview.expertSelectedDtoList == undefined || expertReview.expertSelectedDtoList.length == 0) {
                bsWin.alert("该方案还没有选择评审专家，请先选取评审专家！");
                return;
            }
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if (isValid) {
                //自动保存
                expertReviewSvc.savePayment(expertReview, vm.isCommit,false, function (data) {
                    if (data.flag || data.reCode == "ok") {
                        var resultMap = data.reObj, totalCost = 0, totaoTaxes = 0;
                        for (var k = 0, lk = expertReview.expertSelectedDtoList.length; k < lk; k++) {
                            var v = expertReview.expertSelectedDtoList[k];
                            if ((v.isConfrim == '9' || v.isConfrim == 9) && (v.isJoin == '9' || v.isJoin == 9)) {
                                for (var i = 0, l = resultMap.length; i < l; i++) {
                                    var epId = resultMap[i].EXPERTID;
                                    if (v.expertDto.expertID == epId) {
                                        v.reviewTaxes = parseFloat(resultMap[i].MONTAXES == undefined ? 0 : resultMap[i].MONTAXES).toFixed(2);
                                        v.totalCost = (parseFloat(v.reviewCost) + parseFloat(v.reviewTaxes)).toFixed(2);
                                        totalCost = parseFloat(totalCost) + parseFloat(v.reviewCost);
                                        totaoTaxes = parseFloat(totaoTaxes) + parseFloat(v.reviewTaxes);
                                    }
                                }
                            }
                        }

                        expertReview.reviewCost = parseFloat(totalCost).toFixed(2);
                        expertReview.reviewTaxes = parseFloat(totaoTaxes).toFixed(2);
                        expertReview.totalCost = (parseFloat(totalCost) + parseFloat(totaoTaxes)).toFixed(2);
                        bsWin.alert("操作成功！", function () {
                            vm.isCommit = false;
                        });
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            } else {
                bsWin.alert("请正确填写专家评审费信息！");
            }
        }

        // 关闭专家费用
        vm.closeEditPay = function () {
            window.parent.$("#payment").data("kendoWindow").close();
        }

        /*****************S_单位评分******************/
        vm.editUnitScore = function (id) {
            $("#star").raty({
                number: 5,
                score: function () {
                    $(this).attr("data-num", angular.isUndefined(vm.model.unitScoreDto.score) ? 0 : vm.model.unitScoreDto.score);
                    return $(this).attr("data-num");
                },
                starOn: '../contents/libs/raty/lib/images/star-on.png',
                starOff: '../contents/libs/raty/lib/images/star-off.png',
                starHalf: '../contents/libs/raty/lib/images/star-half.png',
                readOnly: false,
                halfShow: true,
                hints: ['不合格', '合格', '中等', '良好', '优秀'],
                size: 34,
                click: function (score, evt) {
                    vm.model.unitScoreDto.score = score;
                }
            });

            $("#unitscore_win").kendoWindow({
                width: "820px",
                height: "365px",
                title: "编辑-单位星级",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Close"]
            }).data("kendoWindow").center().open();

        }
        //保存单位评分
        vm.saveUnit=function () {
            if (!vm.model.unitScoreDto.score || vm.model.unitScoreDto.score == 0) {
                bsWin.alert("请对单位进行评分！");
            } else {
                companySvc.saveUnit(vm.model.unitScoreDto, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        bsWin.success("保存成功！", function () {
                            vm.closeEditUnit();
                        });
                    } else {
                        bsWin.alert(data.reMsg);
                    }

                });
            }

        }
        // 关闭单位评分
        vm.closeEditUnit = function () {
            window.parent.$("#unitscore_win").data("kendoWindow").close();
        }

        /*****************E_单位评分******************/



        /***************  s_月报简报  ***************/
        vm.updateFlow=function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if(isValid){
                monthlyMultiyearSvc.updatemonthlyMultiyear(vm.suppletter,function(data){
                    if (data.flag || data.reCode == "ok") {
                        vm.commitFlow();
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("缺少部分没有填写，请仔细检查");
            }

        }

        /***************  E_月报简报  ***************/

        /***************  s_档案借阅  ***************/
        vm.isTime=function(){
            if(vm.model.readDate!="" && vm.model.readDate!=undefined ){
                if(vm.flow.businessMap.RETURNDATE<vm.model.readDate){
                    bsWin.alert("归还时间不能小于查阅时间",function () {
                        vm.flow.businessMap.RETURNDATE=vm.model.readDate;
                        $scope.$apply();
                    })
                }
            }
        }
        /***************  E_档案借阅  ***************/

    }
})();

