(function () {
    'use strict';

    angular.module('app').controller('signFlowDealCtrl', sign);

    sign.$inject = ['sysfileSvc', 'signSvc','workprogramSvc', '$state', 'flowSvc', 'signFlowSvc','ideaSvc',
      'addRegisterFileSvc','expertReviewSvc', '$scope','bsWin' , 'financialManagerSvc' , 'assistCostCountSvc' , 'addCostSvc','templatePrintSvc'];

    function sign(sysfileSvc, signSvc,workprogramSvc, $state, flowSvc, signFlowSvc,ideaSvc,addRegisterFileSvc,
                  expertReviewSvc, $scope,bsWin , financialManagerSvc , assistCostCountSvc , addCostSvc,templatePrintSvc) {
        var vm = this;
        vm.title = "项目流程处理";
        vm.model = {};          //收文对象
        vm.flow = {};           //流程对象
        vm.mainwork = {};       //主工作方案
        vm.assistwork = {};     //协工作方案
        vm.dispatchDoc = {};    //发文
        vm.fileRecord = {};     //归档
        vm.expertReview = {};   //评审方案
        vm.work = {};

        //按钮显示控制，全部归为这个对象控制
        vm.showFlag = {
            businessTr:false,          //显示业务办理tr
            businessDis:false,         //显示直接发文复选框
            businessNext:false,        //显示下一环节处理人或者部门

            nodeNext : true,           //下一环节名称
            nodeSelViceMgr:false,      // 选择分管副主任环节
            nodeSelOrgs:false,         // 选择分管部门
            nodeSelPrincipal:false,    // 选择项目负责人
            isMainBranch:false,        // 选择第一负责人
            nodeSign:false,            // 项目签收
            nodeWorkProgram:false,     // 工作方案
            nodeDispatch:false,        // 发文
            nodeConfirmDis:false,      // 确认发文
            nodeCreateDisNum:false,    // 生成发文编号
            nodeFileRecord:false,      // 归档
            nodeXSWorkProgram:false,   // 协审工作方案

            tabWorkProgram:false,       // 显示工作方案标签tab
            tabBaseWP:false,            // 项目基本信息tab
            tabDispatch:false,          // 发文信息tab
            tabFilerecord:false,        // 归档信息tab
            tabExpert:false,            // 专家信息tab
            tabSysFile:false,           // 附件信息tab

            buttBack:false,             // 回退按钮
            expertRemark:false,         // 专家评分弹窗内容显示
            expertpayment:false,        // 专家费用弹窗内容显示
            expertEdit:false,            // 专家评分费用编辑权限
            isMainPrinUser:false        // 是否是第一负责人
        };

        //业务控制对象
        vm.businessFlag = {
            isLoadSign : false,         // 是否加载收文信息
            isLoadFlow : false,         // 是否加载流程信息
            isGotoDis : false,          // 是否直接发文
            isMakeDisNum : false,       // 是否生成发文编号
            principalUsers : [],         // 负责人列表
            isSelMainPriUser:false,     // 是否已经设置主要负责人
            editExpertSC : false,       // 编辑专家评审费和评分,只有专家评审方案环节才能编辑
            expertScore:{},              // 专家评分对象
            isNeedWP : 9,                // 是否需要工作方案
            isMainBranch : false,       // 是否是主分支流程
            isFinishWP : false,         // 是否完成了工作方案
            passDis:false,              // 发文是否通过
            curBranchId:"",              // 当前流程分支
            editEPReviewId:"",           // 可以编辑的评审方案ID
        }

        vm.model.signid = $state.params.signid;
        vm.work.id = $state.params.id;
        vm.flow.taskId = $state.params.taskId; // 流程任务ID
        vm.flow.processInstanceId = $state.params.processInstanceId; // 流程实例ID

        vm.signId = vm.model.signid;

        active();
        function active() {
            $('#myTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
                vm.model.showDiv = showDiv;
            })
            // 初始化业务信息
            signSvc.initFlowPageData(vm.model.signid,function(data){
                vm.model = data;
                //发文
                if (vm.model.dispatchDocDto) {
                    vm.showFlag.tabDispatch = true;
                    vm.dispatchDoc = vm.model.dispatchDocDto;
                    //如果是合并发文次项目，则不用生成发文编号
                    if((vm.dispatchDoc.dispatchWay == 2 && vm.dispatchDoc.isMainProject == 0)
                        || vm.dispatchDoc.fileNum){
                        vm.businessFlag.isCreateDisFileNum = true;
                    }else{
                        vm.showFlag.buttDisFileNum = true;
                    }
                }
                //归档
                if (vm.model.fileRecordDto) {
                    vm.showFlag.tabFilerecord = true;
                    vm.fileRecord = vm.model.fileRecordDto;
                }

                //更改状态,并初始化业务参数
                vm.businessFlag.isLoadSign = true;
                if(vm.businessFlag.isLoadSign && vm.businessFlag.isLoadFlow){
                    signFlowSvc.initBusinessParams(vm);
                }

                //显示拟补充资料函
                if(vm.model.suppLetterDtoList){
                    vm.showSupperIndex = 0;
                }
            });
            // 初始化流程数据
            flowSvc.getFlowInfo(vm.flow.taskId,vm.flow.processInstanceId,function(data){
                vm.flow = data;
                //如果是结束环节，则不显示下一环节信息
                if (vm.flow.end) {
                    vm.showFlag.nodeNext = false;
                }
                //更改状态,并初始化业务参数
                vm.businessFlag.isLoadFlow = true;
                if(vm.businessFlag.isLoadSign && vm.businessFlag.isLoadFlow){
                    signFlowSvc.initBusinessParams(vm);
                }
            });
            // 初始化办理信息
            flowSvc.initFlowData(vm);

            // 初始化上传附件
            sysfileSvc.findByMianId(vm.model.signid,function(data){
                if(data && data.length > 0){
                    vm.showFlag.tabSysFile = true;
                    vm.sysFileList = data;
                    sysfileSvc.initZtreeClient(vm,$scope);//树形图
                }
            });
            //初始化个人常用意见
            ideaSvc.initIdea(vm);
        }

        /***************  S_评审意见管理  ***************/
        // begin 管理个人意见
        vm.ideaEdit = function (options) {
            if(!angular.isObject(options)){
                options = {};
            }
            ideaSvc.initIdeaData(vm,options);
        }

        //选择个人常用意见
        vm.selectedIdea = function(){
            vm.flow.dealOption = vm.chooseIdea;
        }
        /***************  E_评审意见管理  ***************/

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
                        //已经确认并确定参加的，才计算
                        if((v.isConfrim=='9' || v.isConfrim == 9) && (v.isJoin=='9' || v.isJoin==9)){
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

                            //计算评审费用
                            v.reviewCost = v.reviewCost == undefined ? 0 : v.reviewCost;
                            var reviewTaxesTotal = totalCost + parseFloat(v.reviewCost);
                            //console.log("专家当月累加加上本次:" + reviewTaxesTotal);
                            v.reviewTaxes = countNum(reviewTaxesTotal).toFixed(2);
                            v.totalCost = (parseFloat(v.reviewCost) + parseFloat(v.reviewTaxes)).toFixed(2);
                            expertReview.reviewCost = (parseFloat(expertReview.reviewCost) + parseFloat(v.reviewCost)).toFixed(2);
                            expertReview.reviewTaxes = (parseFloat(expertReview.reviewTaxes) + parseFloat(v.reviewTaxes)).toFixed(2);
                            expertReview.totalCost = (parseFloat(expertReview.reviewCost) + parseFloat(expertReview.reviewTaxes)).toFixed(2);
                        }
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
                if(expertReview.reviewCost){
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
                    bsWin.alert("请计算税率，再保存！");
                }
            }else{
                bsWin.alert("请正确填写专家评审费信息！");
            }
        }
        /***************  E_专家评分，评审费发放  ***************/

        /***************  S_流程处理 ***************/
        //流程提交
        vm.commitNextStep = function () {
            if(vm.flow.isSuspended){
                bsWin.error("该流程目前为暂停状态，不能进行流转操作！");
                return ;
            }
            var checkResult = signFlowSvc.checkBusinessFill(vm);
            if (checkResult.resultTag) {
                flowSvc.commit(vm.isCommit,vm.flow,function(data){
                    if (data.flag || data.reCode == "ok") {
                        bsWin.success("操作成功！",function(){
                            $state.go('gtasks');
                        })
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            } else {
                bsWin.alert(checkResult.resultMsg);
            }
        }

        //S_流程回退
        vm.commitBack = function () {
            common.initJqValidation($("#flow_form"));
            var isValid = $("#flow_form").valid();
            if(isValid){
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认回退吗？",
                    onOk: function () {
                        flowSvc.rollBackToLast(vm.flow,vm.isCommit,function(data){
                            if (data.flag || data.reCode == "ok") {
                                vm.isCommit = false;
                                bsWin.alert("回退成功！",function(){
                                    $state.go('gtasks');
                                });
                            } else {
                                bsWin.alert(data.reMsg);
                            }
                        }); // 回退到上一个环节
                    }
                });
            }
        }

        vm.deleteFlow = function () {
            bsWin.confirm({
                title: "询问提示",
                message: "终止流程将无法恢复，确认删除么？？",
                onOk: function () {
                    $('.confirmDialog').modal('hide');
                    flowSvc.deleteFlow(vm);
                }
            });
        }
        /***************  E_流程处理 ***************/

        //编辑审批登记表
        vm.editSign = function(){
            $state.go('fillSign', {signid: vm.model.signid });
        }
        // S_跳转到 工作方案 编辑页面
        vm.addWorkProgram = function () {  
            $state.go('workprogramEdit', {signid: vm.model.signid});
        }// E_跳转到 工作方案 编辑页面
        
        //S_链接到拟补充资料函
        vm.addSuppLetter = function () {
            $state.go('addSupp', {businessId: vm.model.signid,businessType:"SIGN"});
        }// E_跳转到 拟补充资料函 编辑页面
        
        //S 拟补充资料函列表
        vm.addSuppLetterList = function(){
        	$state.go('addSuppletterList',{businessId: vm.model.signid});
        }
      //E 拟补充资料函列表
        
        //S_工作方案  --链接到  登记表补充资料 
        vm.addRegisterFile = function () {
            $state.go('registerFile', {businessId: vm.model.signid});
        }// E_工作方案  --链接到  登记表补充资料
        
        //S_跳转到 工作方案 基本信息
        vm.addBaseWP = function(){
            $state.go('workprogramBaseEdit', {signid: vm.model.signid });
        }

        // S_跳转到 发文 编辑页面
        vm.addDisPatch = function () {
            //如果是未关联，并且是可研或者概算阶段，提醒是否要关联
            if((!vm.model.isAssociate || vm.model.isAssociate == 0) &&
                (signcommon.getReviewStage().STAGE_STUDY == vm.model.reviewstage || signcommon.getReviewStage().STAGE_BUDGET == vm.model.reviewstage)){
                bsWin.confirm({
                    title: "询问提示",
                    message: "该项目还没进行项目关联，是否需要进行关联设置？",
                    onOk: function () {
                        //根据项目名称，查询要关联阶段的项目
                        if(!vm.searchAssociateSign){
                            vm.searchAssociateSign = {
                                signid : vm.model.signid,
                                projectname : vm.model.projectname,
                            };
                        }
                        signSvc.getAssociateSign(vm.searchAssociateSign,function(data){
                            vm.associateSignList = [];
                            if(data){
                                vm.associateSignList = data;
                            }
                            //选中要关联的项目
                            $("#associateWindow").kendoWindow({
                                width: "75%",
                                height: "650px",
                                title: "项目关联",
                                visible: false,
                                modal: true,
                                closable: true,
                                actions: ["Pin", "Minimize", "Maximize", "close"]
                            }).data("kendoWindow").center().open();
                        });
                    },
                    onCancel : function(){
                        $state.go('dispatchEdit', {signid: vm.model.signid});
                    }
                });
            }else{
                $state.go('dispatchEdit', {signid: vm.model.signid});
            }
        }// E_跳转到 发文 编辑页面

        //关联项目条件查询
        vm.associateQuerySign = function(){
            signSvc.getAssociateSign(vm.searchAssociateSign,function(data){
                vm.associateSignList = [];
                if(data){
                    vm.associateSignList = data;
                }
            });
        }

        //start 保存项目关联
        vm.saveAssociateSign = function(associateSignId){
            if(vm.model.signid == associateSignId){
                bsWin.alert("不能关联自身项目");
                return ;
            }
            signSvc.saveAssociateSign(vm.model.signid,associateSignId,function(){
                if(associateSignId){
                    vm.model.isAssociate = 1;
                }
                bsWin.alert(associateSignId != undefined ? "项目关联成功" : "项目解除关联成功");
                window.parent.$("#associateWindow").data("kendoWindow").close();
            });
        }
        //end 保存项目关联

        // S_财务办理
        vm.addFinancialApply = function(id){
            if("9"==vm.model.isassistflow || 9==vm.model.isassistflow){
                vm.costType = "ASSIST";
            }else{
                vm.costType = "REVIEW";
            }
            /**
             * 初始化费用录入
             */
            addCostSvc.initAddCost(vm,vm.costType,vm.model,id);
        }
        //E_财务办理

        vm.addDoFile = function () {
            $state.go('fileRecordEdit', {
                signid: vm.model.signid
            });
        }
        
        //S_归档登记表补充资料
        vm.addRegisterFileRecord = function () {
        	if(!vm.fileRecord.fileRecordId){
        		bsWin.alert("请保存归档再操作！");
        	}else{
        		$state.go('registerFile', {signid: vm.fileRecord.fileRecordId});
        	}
        }// E_归档到登记表补充资料
        

        // 业务判断
        vm.mainOrg = function ($event) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('.seleteTable input[selectType="main"]').each(
                    function () {
                        var value = $(this).attr("value");
                        if(value != checkboxValue){
                            $(this).removeAttr("checked");
                            $("#assist_" + value).removeAttr("disabled");
                        }else{
                            $("#assist_" + checkboxValue).removeAttr("checked");
                            $("#assist_" + checkboxValue).attr("disabled", "disabled");
                        }
                    });

            } else {
                $("#assist_" + checkboxValue).removeAttr("disabled");
            }
            vm.initOption();
        }

        vm.initOption = function(){
            var selOrg = [];
            $('.seleteTable input[selectType="main"]:checked').each(function () {
                selOrg.push($(this).attr("tit"));
            });
            $('.seleteTable input[selectType="assist"]:checked').each(function () {
                selOrg.push($(this).attr("tit"));
            });
            if(selOrg.length > 0){
                vm.flow.dealOption = "请（"+selOrg.join('，')+"）组织评审";
            }
        }

        //检查项目负责人
        vm.checkPrincipal = function(){
            var selUserId = $("#selPrincipalMainUser").val();
            if(selUserId){
                $('#principalAssistUser input[selectType="assistUser"]').each(
                    function () {
                        var value = $(this).attr("value");
                        if (value == selUserId) {
                            $(this).removeAttr("checked");
                            $(this).attr("disabled", "disabled");
                        } else {
                            $(this).removeAttr("disabled");
                        }
                    }
                );
            }
            vm.initUserOption();
        }
        //部门领导分办，选择用户的默认处理意见
        vm.initUserOption = function(){
            var selUserId = $("#selPrincipalMainUser").val();
            var isSelMainUser = false;
            var defaultOption = "请（"
            if(selUserId){
                $.each(vm.users,function(i,u){
                    if(u.id == selUserId){
                        defaultOption += u.displayName;
                        isSelMainUser = true;
                    }
                })
            }
            var selUser = []
            $('#p_assistUser input[selectType="assistUser"]:checked').each(function () {
                selUser.push($(this).attr("tit"));
            });

            if(selUser.length > 0){
                if(isSelMainUser){
                    defaultOption += ', ';
                }
                defaultOption += selUser.join(', ');
            }
            defaultOption += " )组织办理。";

            vm.flow.dealOption = defaultOption;
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
        
        //选择负责人
        vm.addPriUser = function () {
            var isCheck = $("#xs_bmfb input[name='unSelPriUser']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择负责人");
            }else{
                if(vm.businessFlag.isMainBranch){
                    if(vm.isMainPriUser == 9 && isCheck.length > 1){
                        bsWin.alert("总负责人只能选一个");
                        return ;
                    }
                    if(vm.businessFlag.isSelMainPriUser == false && (angular.isUndefined(vm.isMainPriUser) || vm.isMainPriUser == 0)){
                        bsWin.alert("请先选择总负责人");
                        return ;
                    }
                    if(vm.businessFlag.isSelMainPriUser == true && vm.isMainPriUser == 9){
                        bsWin.alert("你已经选择了一个总负责人，不能再次选择负责人！");
                        return ;
                    }
                }
                if(vm.businessFlag.principalUsers && (vm.businessFlag.principalUsers.length + isCheck.length) > 3){
                    bsWin.alert("最多只能选择3个负责人，请重新选择！");
                    return ;
                }

                for (var i = 0; i < isCheck.length; i++) {
                    var priUser = {};
                    priUser.userId = isCheck[i].value;
                    priUser.userType = $("#userType").val();
                    if(vm.isMainPriUser == 9){
                        vm.businessFlag.isSelMainPriUser = true;
                        priUser.isMainUser = 9;
                        vm.isMainPriUser == 0;
                    }else{
                        priUser.isMainUser = 0;
                    }
                    vm.users.forEach(function(u,index){
                       if(u.id == isCheck[i].value){
                           u.isSelected = true;
                           priUser.userId = u.id;
                           priUser.userName = u.displayName;
                       }
                    });
                    vm.businessFlag.principalUsers.push(priUser);
                    //初始化处理人
                    vm.initDealUserName(vm.businessFlag.principalUsers);
                }
            }
        }

        //删除负责人
        vm.delPriUser = function () {
            var isCheck = $("#xs_bmfb input[name='selPriUser']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择取消的负责人");
            }else{
                for (var i = 0; i < isCheck.length; i++) {
                    vm.users.forEach(function(u,index){
                        if(u.id == isCheck[i].value){
                            u.isSelected = false;
                        }
                    });
                    vm.businessFlag.principalUsers.forEach(function(pu,index){
                        if(pu.userId == isCheck[i].value){
                            if(pu.isMainUser == 9){
                                vm.businessFlag.isSelMainPriUser = false;
                            }
                            vm.businessFlag.principalUsers.splice(index,1);
                        }
                    });
                }
                //初始化处理人
                vm.initDealUserName(vm.businessFlag.principalUsers);
            }
        }//E_删除负责人

        vm.initDealUserName = function(userList){
            if(userList && userList.length > 0){
                var defaultOption="请（";
                angular.forEach(userList,function(u,i){
                    if(i > 0){
                        defaultOption += ","
                    }
                    defaultOption += u.userName;
                })
                defaultOption += " )组织办理。";
                vm.flow.dealOption = defaultOption;
            }else{
                vm.flow.dealOption = "";
            }
        }




        //S_判断是否需要工作方案
        vm.checkNeedWP = function($event){
            var checkbox = $event.target;
            var checked = checkbox.checked;
            if(checked){
                vm.businessFlag.isNeedWP = 9;
            }else{
                //如果已经完成了工作方案，则询问是否要删除
                if(vm.businessFlag.isFinishWP){
                    bsWin.confirm({
                        title: "询问提示",
                        message: "取消工作方案操作将会对您之前的做的工作方案进行清除，数据清除不可恢复，确定不做工作么？",
                        onOk: function () {
                            $('.confirmDialog').modal('hide');
                            vm.businessFlag.isNeedWP = 0;
                            signSvc.removeWP(vm);
                        },
                        onClose:function(){
                            checkbox.checked = !checked;
                            vm.businessFlag.isNeedWP = 9;
                        }
                    });
                }else{
                    vm.businessFlag.isNeedWP = 0;
                }
            }
        }//E_判断是否需要工作方案

        //生产会前准备材料
        vm.meetingDoc = function(){
            common.confirm({
                vm:vm,
                title:"",
                msg:"如果之前已经生成会前准备材料，则本次生成的文档会覆盖之前产生的文档，确定执行操作么？",
                fn:function () {
                    $('.confirmDialog').modal('hide');
                    // signSvc.findWorkProgramBySignId(vm,function(){
                        signSvc.meetingDoc(vm,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                bsWin.success("操作成功");
                                sysfileSvc.findByMianId(vm.model.signid,function(data){
                                    if(data || data.length > 0){
                                        vm.showFlag.tabSysFile = true;
                                        vm.sysFileList = data;
                                        sysfileSvc.initZtreeClient(vm,$scope);//树形图

                                    }
                                });
                            }else{
                                bsWin.error(data.reMsg);
                            }
                        });
                    // });
                }
            })
        }

        //附件下载
        vm.commonDownloadSysFile = function(sysFileId){
            sysfileSvc.downloadFile(sysFileId);
        }

        //生成发文字号
        vm.createDispatchFileNum = function(){
            signSvc.createDispatchFileNum(vm.model.signid,vm.dispatchDoc.id,function(data){
                if (data.flag || data.reCode == "ok") {
                    vm.dispatchDoc.fileNum = data.reObj;
                }
                bsWin.alert(data.reMsg);
            });
        }

        //生成发文模板
        vm.dispatchTemplate = function(){
            signSvc.createDispatchTemplate(vm);
        }

        //监听是否通过
        vm.watchPassDis = function(){
            //监听是否关联事件
            $scope.$watch("vm.businessFlag.passDis",function (newValue, oldValue) {
                if(newValue == 9){
                    vm.flow.dealOption = "通过";
                }else{
                    vm.flow.dealOption = "不通过";
                }

            });
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
                expertReviewSvc.updateJoinState("","", ids.join(','), '9',vm.isCommit,function(data){
                    bsWin.success("操作成功！");
                    vm.reFleshJoinState(ids,'9');
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
                expertReviewSvc.updateJoinState("","", ids.join(','), '0',vm.isCommit,function(data){
                    bsWin.success("操作成功！");
                    vm.reFleshJoinState(ids,'0');
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
        }

        //签收模板打印
        vm.printpage = function ($event) {
                console.log($event.target);
                console.log(vm.model);
            templatePrintSvc.templatePrint($event.target,vm.model);
        }

    }
})();
