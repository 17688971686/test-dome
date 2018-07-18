(function () {
    'use strict';

    angular.module('app').controller('signFlowDealCtrl', sign);

    sign.$inject = ['sysfileSvc', 'signSvc', 'dispatchSvc', '$state', 'flowSvc', 'signFlowSvc', 'ideaSvc',
        'workprogramSvc', 'expertReviewSvc', '$scope', 'bsWin', 'financialManagerSvc', 'addSuppLetterQuerySvc',
        'addCostSvc', 'templatePrintSvc', 'companySvc'];

    function sign(sysfileSvc, signSvc, dispatchSvc, $state, flowSvc, signFlowSvc, ideaSvc, workprogramSvc,
                  expertReviewSvc, $scope, bsWin, financialManagerSvc, addSuppLetterQuerySvc, addCostSvc, templatePrintSvc, companySvc) {

        var vm = this;
        vm.title = "项目流程处理";
        vm.model = {};          //收文对象
        vm.flow = {};           //流程对象
        vm.mainwork = {};       //主工作方案
        vm.assistwork = {};     //协工作方案
        vm.dispatchDoc = {};    //发文
        vm.fileRecord = {};     //归档
        vm.expertReview = {};   //评审方案
        vm.queryParams = {};  //返回时。列表数据不变
        vm.work = {};
        vm.isDisplay = true;   //附件显示删除按钮
        vm.reworkWorkPlanObject = {};   //发文:重写工作方案
        vm.expertList = new Array(15); //用于打印页面的专家列表，控制行数
        //按钮显示控制，全部归为这个对象控制
        vm.showFlag = {
            businessTr: false,          //显示业务办理tr
            businessDis: false,         //显示直接发文复选框
            businessNext: false,        //显示下一环节处理人或者部门

            nodeNext: true,           //下一环节名称
            nodeSelViceMgr: false,      // 选择分管副主任环节
            nodeSelOrgs: false,         // 选择分管部门
            nodeSelPrincipal: false,    // 选择项目负责人
            isMainBranch: false,        // 选择第一负责人
            nodeSign: false,            // 项目签收
            nodeWorkProgram: false,     // 工作方案
            nodeDispatch: false,        // 发文
            nodeConfirmDis: false,      // 确认发文
            nodeCreateDisNum: false,    // 生成发文编号
            nodeFileRecord: false,      // 归档
            nodeXSWorkProgram: false,   // 协审工作方案

            tabWorkProgram: false,       // 显示工作方案标签tab
            tabBaseWP: false,            // 项目基本信息tab
            tabDispatch: false,          // 发文信息tab
            tabFilerecord: false,        // 归档信息tab
            tabExpert: false,            // 专家信息tab
            tabSysFile: false,           // 附件信息tab

            buttBack: false,             // 回退按钮
            expertRemark: false,         // 专家评分弹窗内容显示
            expertpayment: false,        // 专家费用弹窗内容显示
            expertEdit: false,            // 专家评分费用编辑权限
            isMainPrinUser: false,        // 是否是第一负责人
            showFilecodeBt : false ,         //显示归档按钮
            showReworkWorkPlanBt : false          //显示重写工作方案按钮
        };

        //业务控制对象
        vm.businessFlag = {
            isLoadSign: false,         // 是否加载收文信息
            isLoadFlow: false,         // 是否加载流程信息
            isGotoDis: false,          // 是否直接发文
            isMakeDisNum: false,       // 是否生成发文编号
            principalUsers: [],         // 负责人列表
            isSelMainPriUser: false,     // 是否已经设置主要负责人
            editExpertSC: false,       // 编辑专家评审费和评分,只有专家评审方案环节才能编辑
            expertScore: {},              // 专家评分对象
            isNeedWP: 9,                // 是否需要工作方案
            isMainBranch: false,       // 是否是主分支流程
            isFinishWP: false,         // 是否完成了工作方案
            passDis: false,              // 发文是否通过
            curBranchId: "",              // 当前流程分支
            editEPReviewId: "",           // 可以编辑的评审方案ID
            isReworkWorkPlan: false,              // 是否重写工作方案对象
        }
        vm.model.signid = $state.params.signid;
        vm.work.id = $state.params.id;
        vm.flow.taskId = $state.params.taskId; // 流程任务ID
        vm.flow.processInstanceId = $state.params.processInstanceId; // 流程实例ID

        vm.signId = vm.model.signid;
        vm.expertList = new Array(10); //用于打印页面的专家列表，控制行数
        vm.curDate = "";  //当前日期
        active();
        function active() {
            // debugger;
            $('#myTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
                vm.model.showDiv = showDiv;
            })

            //初始化附件控件
            vm.sysFile = {
                businessId: $state.params.signid,
                mainId: $state.params.signid,
                mainType: sysfileSvc.mainTypeValue().SIGN,
                sysfileType: sysfileSvc.mainTypeValue().FILLSIGN,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm,
                uploadSuccess: function () {
                    sysfileSvc.findByMianId(vm.model.signid, function (data) {
                        if (data && data.length > 0) {
                            vm.showFlag.tabSysFile = true;
                            vm.sysFileList = data;
                            sysfileSvc.initZtreeClient(vm, $scope);//树形图
                        }
                    });
                }
            });
            // 初始化业务信息
            signSvc.initFlowPageData(vm.model.signid, function (data) {
                // debugger;
                vm.model = data;
                vm.curDate = data.curDate;
                var deActive = $("#myTab .active");
                var deObj = $("a", deActive);
                vm.model.showDiv = deObj.attr("for-div");

                //判断是否有工作方案,并且是正在发文环节的，有工作方案则显示会前材料按钮
                if (vm.model.processState == 3 || vm.model.processState == 4) {
                    vm.showMeterial = true;
                }
                //发文
                if (vm.model.dispatchDocDto) {
                    vm.showFlag.tabDispatch = true;

                    vm.dispatchDoc = vm.model.dispatchDocDto;
                    //如果是合并发文次项目，则不用生成发文编号
                    if ((vm.dispatchDoc.dispatchWay == 2 && vm.dispatchDoc.isMainProject == 0)|| vm.dispatchDoc.fileNum) {
                        vm.businessFlag.isCreateDisFileNum = true;
                    } else {
                        vm.showFlag.buttDisFileNum = true;
                    }
                    //如果是合并发文主项目，要获取合并项目信息
                    if(vm.dispatchDoc.dispatchWay == 2 && vm.dispatchDoc.isMainProject == 9){
                        dispatchSvc.findMergeDis(vm.model.signid,function(data){
                            if(data){
                                vm.mergeDisDtoList = data;
                            }
                        })
                    }

                }

                //归档
                if (vm.model.fileRecordDto) {
                    vm.showFlag.tabFilerecord = true;
                    vm.fileRecord = vm.model.fileRecordDto;
                }
                // debugger;
                //判断是否有多个分支，用于控制是否显示总投资字段 和 分开获取关联的项目信息（主要用于项目概算阶段）（旧版本）
                //通过评估部门的个数来控制总投资字段  修改于（2018-01-16）
                if (vm.model.workProgramDtoList && vm.model.workProgramDtoList.length > 0) {
                    var orgStr;
                    if (vm.model.workProgramDtoList[0].branchId == '1') {
                        orgStr = vm.model.workProgramDtoList[0].reviewOrgName;
                    } else {
                        orgStr = vm.model.workProgramDtoList[0].mainWorkProgramDto.reviewOrgName;
                    }

                    if (orgStr && orgStr.split(',').length > 1) {
                        vm.showTotalInvestment = true;
                    }

                    //进行专家的专业类别拼接
                    for (var i = 0; i < vm.model.workProgramDtoList.length; i++) {
                        var workProgramDtoList = vm.model.workProgramDtoList;//进行存值
                        //判断下是否有拟请的专家
                        if (workProgramDtoList[i].expertDtoList) {
                            var expertDtoList = workProgramDtoList[i].expertDtoList;//进行存值
                            for (var j = 0; j < expertDtoList.length; j++) {
                                //判断下专家是否有专业类别
                                if (expertDtoList[j].expertTypeDtoList) {
                                    var expertTypeList = expertDtoList[j].expertTypeDtoList;//进行存值
                                    var major = "";//专业
                                    var expertCategory = ""//专业类别
                                    for (var k = 0; k < expertTypeList.length; k++) {
                                        if (expertCategory.indexOf(expertTypeList[k].expertType) < 0) {
                                            if (k > 0) {
                                                expertCategory += "、"
                                            }
                                            expertCategory += expertTypeList[k].expertType;
                                        }
                                        if (k > 0) {
                                            major += "、"
                                        }
                                        major += expertTypeList[k].maJorBig + "、" + expertTypeList[k].maJorSmall;

                                    }
                                    expertDtoList[j].expertCategory = expertCategory;
                                    expertDtoList[j].major = major;
                                }
                            }
                        }
                    }
                }

                //更改状态,并初始化业务参数
                vm.businessFlag.isLoadSign = true;
                if (vm.businessFlag.isLoadSign && vm.businessFlag.isLoadFlow) {
                    signFlowSvc.initBusinessParams(vm);
                }

                //显示拟补充资料函
                if (vm.model.suppLetterDtoList) {
                    vm.showSupperIndex = 0;
                }
                //拟补充资料信息
                if (vm.model.registerFileDtoDtoList != undefined) {
                    vm.supply = [];//拟补充资料
                    vm.registerFile = [];//其他资料
                    vm.drawingFile = [];//图纸资料
                    vm.otherFile = [];//归档的其他资料
                    vm.model.registerFileDtoDtoList.forEach(function (registerFile, x) {
                        if (registerFile.businessType == 3 || registerFile.businessType =="5"
                            ||registerFile.businessType =="6"||registerFile.businessType =="7") {
                            vm.supply.push(registerFile);
                        } else if (registerFile.businessType == 2) {
                            vm.drawingFile.push(registerFile);
                        } else if (registerFile.businessType == 1 || registerFile.businessType == 4) {
                            vm.registerFile.push(registerFile);
                        } else if (registerFile.businessType == 5 || registerFile.businessType == 6 || registerFile.businessType == 7) {
                            vm.otherFile.push(registerFile);
                        }
                    })
                }
            });

            // 初始化流程数据
            flowSvc.getFlowInfo(vm.flow.taskId, vm.flow.processInstanceId, function (data) {
                vm.flow = data;
                //如果任务ID为空，说明任务已经被处理
                if (vm.flow.taskId) {
                    //如果是结束环节，则不显示下一环节信息
                    if (vm.flow.end) {
                        vm.showFlag.nodeNext = false;
                    }
                    //更改状态,并初始化业务参数
                    vm.businessFlag.isLoadFlow = true;
                    if (vm.businessFlag.isLoadSign && vm.businessFlag.isLoadFlow) {
                        signFlowSvc.initBusinessParams(vm);
                    }
                } else {
                    bsWin.alert("该任务已处理！", function () {
                        $state.go('gtasks');
                    });
                }
            });
            // 初始化办理信息
            flowSvc.initFlowData(vm);

            // 初始化上传附件
            sysfileSvc.findByMianId(vm.model.signid, function (data) {
                if (data && data.length > 0) {
                    vm.showFlag.tabSysFile = true;
                    vm.sysFileList = data;
                    sysfileSvc.initZtreeClient(vm, $scope);//树形图
                }
            });
            /**
             * 删除附件
             * @param fileId
             */
            vm.delFile = function (fileId) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认删除么？",
                    onOk: function () {
                        sysfileSvc.delSysFile(fileId, function () {
                            bsWin.alert("删除成功", function () {
                                sysfileSvc.findByMianId(vm.model.signid, function (data) {
                                    if (data && data.length > 0) {
                                        vm.showFlag.tabSysFile = true;
                                        vm.sysFileList = data;
                                        sysfileSvc.initZtreeClient(vm, $scope);//树形图
                                    } else {
                                        vm.showFlag.tabSysFile = false;
                                        $('#myTab a:first').tab('show');// 选取第一个标签页
                                        //打开标签页
                                        $(".tab-pane").removeClass("active").removeClass("in");
                                        $("#sign_detail").addClass("active").addClass("in").show(500);
                                    }
                                });
                            })
                        });
                    }
                });

            }
            //初始化个人常用意见
            ideaSvc.initIdea(vm);
        }

        /***************  S_评审意见管理  ***************/
        // begin 管理个人意见
        vm.ideaEdit = function (options) {
            // debugger;
            if (!angular.isObject(options)) {
                options = {};
            }
            ideaSvc.initIdeaData(vm, options);
        }

        //选择个人常用意见
        vm.selectedIdea = function () {
            // debugger;
            vm.flow.dealOption = vm.chooseIdea;
        }
        /***************  E_评审意见管理  ***************/

        /***************  S_专家评分，评审费发放  ***************/
        // 编辑专家评分
        vm.editSelectExpert = function (id) {
            // debugger;
            vm.scoreExpert = {};
            $.each(vm.model.expertReviewDto.expertSelectedDtoList, function (i, scopeEP) {
                if (scopeEP.id == id) {
                    vm.scoreExpert = angular.copy(scopeEP);
                    return;
                }
            })

            $("#star_" + vm.scoreExpert.id).raty({
                number: 5,
                score: function () {
                    $(this).attr("data-num", angular.isUndefined(vm.scoreExpert.score) ? 0 : vm.scoreExpert.score);
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
            if (!vm.scoreExpert.score || vm.scoreExpert.score == 0) {
                bsWin.alert("请对专家进行评分！");
            } else {
                expertReviewSvc.saveMark(vm.scoreExpert, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        angular.forEach(vm.model.expertReviewDto.expertSelectedDtoList, function (scopeEP, index) {
                            if (scopeEP.id == vm.scoreExpert.id) {
                                scopeEP.score = vm.scoreExpert.score;
                                scopeEP.describes = vm.scoreExpert.describes;
                            }
                        })
                        bsWin.success("保存成功！", function () {
                            vm.closeEditMark();
                        });
                    } else {
                        bsWin.alert(data.reMsg);
                    }

                });
            }
        }

        /**
         * 计算应纳税额(日期以【函评日期/评审会日期】为准)
         * @param expertReview
         */
        vm.countTaxes = function (expertReview) {
            if (expertReview == undefined) {
                return;
            }
            if (expertReview.expertSelectedDtoList == undefined || expertReview.expertSelectedDtoList.length == 0) {
                bsWin.alert("该方案还没有选择评审专家，请先选取评审专家！");
                return;
            }
            if (expertReview.reviewDate == undefined) {
                bsWin.alert("(函评/评审会)日期为空，无法进行专家纳税计算，请联系系统管理员处理！");
                return;
            }
            var reg = /^(\d{4}-\d{1,2}-\d{1,2})$/;
            if (!reg.exec(expertReview.reviewDate)) {
                bsWin.alert("请输入正确的日期格式");
                return;
            }
            //设置一个评审费发放日期默认值
            if (!expertReview.payDate) {
                expertReview.payDate = vm.curDate;
            }
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if (isValid) {
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
            } else {
                bsWin.alert("请正确填写专家评审费信息！");
            }
        }

        // 关闭专家费用
        vm.closeEditPay = function () {
            window.parent.$("#payment").data("kendoWindow").close();
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
        /***************  E_专家评分，评审费发放  ***************/

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
        vm.saveUnit = function () {
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

        /***************  S_流程处理 ***************/
        //流程提交
        vm.commitNextStep = function () {
            if (vm.flow.isSuspended) {
                bsWin.error("该流程目前为暂停状态，不能进行流转操作！");
                return;
            } else {
                var checkResult = signFlowSvc.checkBusinessFill(vm);
                if (checkResult.resultTag) {
                    flowSvc.commit(vm.isCommit, vm.flow, function (data) {
                        if (data.flag || data.reCode == "ok") {
                            bsWin.success("操作成功！", function () {
                                $state.go('gtasks');
                            })
                        } else {
                            bsWin.alert(data.reMsg);
                        }
                    });
                } else {
                    bsWin.alert(checkResult.resultMsg);
                }
            }
        }

        //S_流程回退
        vm.commitBack = function () {
            common.initJqValidation($("#flow_form"));
            var isValid = $("#flow_form").valid();
            if (isValid) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认回退吗？",
                    onOk: function () {
                        flowSvc.rollBackToLast(vm.flow, vm.isCommit, function (data) {
                            if (data.flag || data.reCode == "ok") {
                                vm.isCommit = false;
                                bsWin.alert("回退成功！", function () {
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
        vm.editSign = function () {
            $state.go('fillSign', {signid: vm.model.signid});
        }
        // S_跳转到 工作方案 编辑页面
        vm.addWorkProgram = function () {
            $state.go('flowWPEdit', {signid: vm.model.signid,taskid:vm.flow.taskId});
        }// E_跳转到 工作方案 编辑页面

        // S_跳转到 项目基本信息 编辑页面
        vm.addBaseInfo = function(){
            $state.go('initProjBase', {signid: vm.model.signid,isadmin:null});
        }// E_跳转到 项目基本信息 编辑页面

        //S_链接到拟补充资料函
        vm.addSuppLetter = function () {
            addSuppLetterQuerySvc.checkIsApprove(vm.model.signid,"1",function(data){
                if(data.flag || data.reCode == 'ok'){
                    $state.go('addSupp', {businessId: vm.model.signid, businessType: "SIGN"});
                }else{
                    bsWin.confirm({
                        title: "询问提示",
                        message: "该项目还有拟补充资料函未审批完成，确定要新增拟补充资料函么？如果要修改拟补充资料函，请到“查询统计”->“拟补充资料函查询”菜单进行修改即可！",
                        onOk: function () {
                            $state.go('addSupp', {businessId: vm.model.signid, businessType: "SIGN"});
                        }
                    });
                }
            });


        }// E_跳转到 拟补充资料函 编辑页面

        //S 拟补充资料函列表
        vm.addSuppLetterList = function () {
            $state.go('addSuppletterList', {businessId: vm.model.signid});
        }
        //E 拟补充资料函列表

        //S_工作方案  --链接到  登记表补充资料
        vm.addRegisterFile = function () {
            // debugger;
            /*$("#associateWindow").kendoWindow({
             width: "80%",
             height: "800px",
             title: "项目关联",
             visible: false,
             modal: true,
             closable: true,
             actions: ["Pin", "Minimize", "Maximize", "close"],
             }).data("kendoWindow").center().open();*/
            $state.go('registerFile', {businessId: vm.model.signid});
        }// E_工作方案  --链接到  登记表补充资料

        //S_跳转到 工作方案 基本信息
        vm.addBaseWP = function () {
            $state.go('workprogramBaseEdit', {signid: vm.model.signid});
        }


        // S_跳转到 发文 编辑页面
        vm.addDisPatch = function () {
            //如果是未关联，并且是可研或者概算阶段，提醒是否要关联
            if ((!vm.model.isAssociate || vm.model.isAssociate == 0) &&
                (signcommon.getReviewStage().STAGE_STUDY == vm.model.reviewstage
                || signcommon.getReviewStage().STAGE_BUDGET == vm.model.reviewstage)) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "该项目还没进行项目关联，是否需要进行关联设置？",
                    onOk: function () {
                        if (!vm.ss) {
                            vm.page = lgx.page.init({
                                id: "demo5", get: function (o) {
                                    //根据项目名称，查询要关联阶段的项目
                                    if (!vm.price) {
                                        vm.price = {
                                            signid: vm.model.signid,
                                            mUserName: vm.model.mUserName,
                                        };
                                    }
                                    vm.price.reviewstage = vm.model.reviewstage; //设置评审阶段
                                    var skip;
                                    //oracle的分页不一样。
                                    if (o.skip != 0) {
                                        skip = o.skip + 1
                                    } else {
                                        skip = o.skip
                                    }
                                    vm.price.skip = skip;//页码
                                    vm.price.size = o.size + o.skip;//页数
                                    signSvc.getAssociateSignGrid(vm, function (data) {
                                        vm.associateSignList = [];
                                        if (data) {
                                            vm.noassociateSign = false;
                                            vm.associateSignList = data.value;
                                            vm.page.callback(data.count);//请求回调时传入总记录数
                                        }else{
                                            vm.noassociateSign = true;
                                        }
                                    });
                                }
                            });
                            vm.ss = true;
                        } else {
                            vm.page.selPage(1);
                        }
                        //选中要关联的项目
                        $("#associateWindow").kendoWindow({
                            width: "80%",
                            height: "800px",
                            title: "项目关联",
                            visible: false,
                            modal: true,
                            closable: true,
                            actions: ["Pin", "Minimize", "Maximize", "close"],
                        }).data("kendoWindow").center().open();

                    },
                    onCancel: function () {
                        $state.go('dispatchEdit', {signid: vm.model.signid});
                    }
                });
            } else {
                $state.go('dispatchEdit', {signid: vm.model.signid});
            }
        }// E_跳转到 发文 编辑页面

        // S_跳转到 发文 重写工作方案
        vm.reworkWorkPlanViem = function () {
            workprogramSvc.getProjBranchInfo($state.params.signid,function(data){
                vm.signBranchData = data;
                $("#reworkWorkPlanWindow").kendoWindow({
                    width: "50%",
                    height: "400px",
                    title: "重写工作方案",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "close"],
                }).data("kendoWindow").center().open();
            });

        }// E_跳转到 发文 编辑页面

        //关联项目条件查询
        vm.associateQuerySign = function () {
            signSvc.getAssociateSignGrid(vm, function (data) {
                vm.associateSignList = [];
                if (data) {
                    vm.noassociateSign = false;
                    vm.associateSignList = data.value;
                    vm.page.callback(data.count);//请求回调时传入总记录数
                }else{
                    vm.noassociateSign = true;
                }
            });
        }

        //start 保存项目关联
        vm.saveAssociateSign = function (associateSignId) {
            if (vm.model.signid == associateSignId) {
                bsWin.alert("不能关联自身项目");
                return;
            }
            signSvc.saveAssociateSign(vm.model.signid, associateSignId, function () {
                if (associateSignId) {
                    vm.model.isAssociate = 1;
                }
                bsWin.alert(associateSignId != undefined ? "项目关联成功" : "项目解除关联成功");
                window.parent.$("#associateWindow").data("kendoWindow").close();
            });
        }
        //end 保存项目关联

        // S_财务办理(目前全部是评审费)
        vm.addFinancialApply = function (id) {
            vm.costType = "REVIEW";
            /*if ("9" == vm.model.isassistflow || 9 == vm.model.isassistflow) {
             vm.costType = "ASSIST";
             } else {
             vm.costType = "REVIEW";
             }*/
            /**
             * 初始化费用录入
             */
            addCostSvc.initAddCost(vm, vm.costType, vm.model, id);
        }
        //E_财务办理

        vm.addDoFile = function () {
            $state.go('fileRecordEdit', {
                signid: vm.model.signid
            });
        }

        //S_归档登记表补充资料
        vm.addRegisterFileRecord = function () {
            if (!vm.fileRecord.fileRecordId) {
                bsWin.alert("请保存归档再操作！");
            } else {
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
                        if (value != checkboxValue) {
                            $(this).removeAttr("checked");
                            $("#assist_" + value).removeAttr("disabled");
                        } else {
                            $("#assist_" + checkboxValue).removeAttr("checked");
                            $("#assist_" + checkboxValue).attr("disabled", "disabled");
                        }
                    });

            } else {
                $("#assist_" + checkboxValue).removeAttr("disabled");
            }
            vm.initOption($event);
        }
        var selOrg = [];
        vm.initOption = function ($event) {
            var checkbox = $event.target;
            var obj = $(checkbox);
            var selectType = obj.attr("selectType");
            var checked = checkbox.checked;
            if (checked && selectType == 'main') {
                //先删除第一个
                selOrg.splice(0, 1);
                //添加（替换不了。变为了添加）
                selOrg.splice(0, 0, obj.attr("tit"));
            } else if (!checked && selectType == 'main') {
                selOrg[0] = "";
            }

            if (checked && selectType == 'assist') {
                selOrg.push(obj.attr("tit"));
            } else if (!checked && selectType == 'assist') {
                var title = obj.attr("tit");
                angular.forEach(selOrg, function (tit, index) {
                    if (tit == title) {
                        selOrg.splice(index, 1);
                    }
                });
            }
            if (selOrg.length > 0) {
                for (var i = 0; i < selOrg.length; i++) {
                    if (selOrg[0] == selOrg[i] && 0 != i) {//判断跟主办是否有重复
                        selOrg.splice(i, 1);
                    }

                }
                vm.flow.dealOption = "请（" + selOrg.join('，') + "）组织评审";
            }

        }

        //检查项目负责人
        vm.checkPrincipal = function () {
            var selUserId = $("#selPrincipalMainUser").val();
            if (selUserId) {
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

        var selUser = []
        vm.initUserOption = function (displayName) {
            var selUserId = $("#selPrincipalMainUser").val();
            var isSelMainUser = false;
            var defaultOption = "请（"
            if (selUserId) {
                $.each(vm.users, function (i, u) {
                    if (u.id == selUserId) {
                        defaultOption += u.displayName;
                        vm.selUserName = u.displayName;//用于判断是否要删除。
                        isSelMainUser = true;
                    }
                })
            } else {
                vm.selUserName = displayName;
            }
            //根据勾选的来加
            if ($("input[name='" + displayName + "']").is(':checked')) {//勾中的
                selUser.push(displayName);
            } else {//不勾中的
                angular.forEach(selUser, function (su, index) {
                    if (su == vm.selUserName || su == displayName) {
                        //判断。如果第一负责人跟其他负责人相同时。进行删减。只保留一个意见
                        selUser.splice(index, 1);
                    }
                });
            }
            if (selUser.length > 0) {
                if (isSelMainUser) {
                    defaultOption += ', ';
                }
                defaultOption += selUser.join(', ');
            }
            defaultOption += " )组织评审。";

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
            } else {
                if (vm.businessFlag.isMainBranch) {
                    if (vm.isMainPriUser == 9 && isCheck.length > 1) {
                        bsWin.alert("总负责人只能选一个");
                        return;
                    }
                    if (vm.businessFlag.isSelMainPriUser == false && (angular.isUndefined(vm.isMainPriUser) || vm.isMainPriUser == 0)) {
                        bsWin.alert("请先选择总负责人");
                        return;
                    }
                    if (vm.businessFlag.isSelMainPriUser == true && vm.isMainPriUser == 9) {
                        bsWin.alert("你已经选择了一个总负责人！");
                        return;
                    }
                }
                /* if(vm.businessFlag.principalUsers && (vm.businessFlag.principalUsers.length + isCheck.length) > 3){
                 bsWin.alert("最多只能选择3个负责人，请重新选择！");
                 return ;
                 }*/

                for (var i = 0; i < isCheck.length; i++) {
                    var priUser = {};
                    priUser.userId = isCheck[i].value;
                    priUser.userType = $("#userType").val();
                    if (vm.isMainPriUser == 9) {
                        vm.businessFlag.isSelMainPriUser = true;
                        priUser.isMainUser = 9;
                        vm.isMainPriUser = 0;
                    } else {
                        priUser.isMainUser = 0;
                    }
                    vm.users.forEach(function (u, index) {
                        if (u.id == isCheck[i].value) {
                            u.isSelected = true;
                            priUser.userId = u.id;
                            priUser.userName = u.displayName;
                        }
                    });
                    vm.businessFlag.principalUsers.push(priUser);
                    //进行排序。主负责人第一
                    vm.businessFlag.principalUsers.sort(by("isMainUser"));
                    //初始化处理人
                    vm.initDealUserName(vm.businessFlag.principalUsers);
                }
            }
        }
        //排序的方法
        var by = function (name) {
            return function (o, p) {
                var a, b;
                if (typeof o === "object" && typeof p === "object" && o && p) {
                    a = o[name];
                    b = p[name];
                    console.log(a, b);
                    if (a === b) {
                        return 0;
                    }
                    if (typeof a === typeof b) {
                        return a > b ? -1 : 1;
                    }
                    return typeof a > typeof b ? -1 : 1;
                }
                else {
                    throw ("error");
                }
            }
        }

        //删除负责人
        vm.delPriUser = function () {
            var isCheck = $("#xs_bmfb input[name='selPriUser']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择取消的负责人");
            } else {
                for (var i = 0; i < isCheck.length; i++) {
                    vm.users.forEach(function (u, index) {
                        if (u.id == isCheck[i].value) {
                            u.isSelected = false;
                        }
                    });
                    vm.businessFlag.principalUsers.forEach(function (pu, index) {
                        if (pu.userId == isCheck[i].value) {
                            if (pu.isMainUser == 9) {
                                vm.businessFlag.isSelMainPriUser = false;
                            }
                            vm.businessFlag.principalUsers.splice(index, 1);
                        }
                    });
                }
                //初始化处理人
                vm.initDealUserName(vm.businessFlag.principalUsers);
            }
        }//E_删除负责人

        vm.initDealUserName = function (userList) {
            if (userList && userList.length > 0) {
                var defaultOption = "请（";
                angular.forEach(userList, function (u, i) {
                    if (i > 0) {
                        defaultOption += ","
                    }
                    defaultOption += u.userName;


                })
                defaultOption += " )组织评审。";
                vm.flow.dealOption = defaultOption;
            } else {
                vm.flow.dealOption = "";
            }
        }


        //S_判断是否需要工作方案
        vm.checkNeedWP = function ($event) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            if (checked) {
                vm.businessFlag.isNeedWP = 9;
            } else {
                //如果已经完成了工作方案，则询问是否要删除
                if (vm.businessFlag.isFinishWP) {
                    bsWin.confirm({
                        title: "询问提示",
                        message: "不做工作方案系统将会删除工作方案数据，确认不做工作方案么？",
                        onOk: function () {
                            $('.confirmDialog').modal('hide');
                            vm.businessFlag.isNeedWP = 0;
                            signSvc.removeWP(vm);
                        },
                        onClose: function () {
                            checkbox.checked = !checked;
                            vm.businessFlag.isNeedWP = 9;
                        }
                    });
                } else {
                    vm.businessFlag.isNeedWP = 0;
                }
            }
        }//E_判断是否需要工作方案

        //生产会前准备材料
        vm.meetingDoc = function () {
            bsWin.confirm({
                title: "",
                message: "如果之前已经生成会前准备材料，则本次生成的文档会覆盖之前产生的文档，确定执行操作么？",
                onOk: function () {
                    signSvc.meetingDoc(vm, function (data) {
                        if (data.flag || data.reCode == 'ok') {
                            bsWin.success(data.reMsg);
                            sysfileSvc.findByMianId(vm.model.signid, function (data) {
                                if (data || data.length > 0) {
                                    vm.showFlag.tabSysFile = true;
                                    vm.sysFileList = data;
                                    sysfileSvc.initZtreeClient(vm, $scope);//树形图
                                }
                            });
                        } else {
                            bsWin.alert(data.reMsg);
                        }
                    });
                    // });
                }
            })
        }

        //附件下载
        vm.commonDownloadSysFile = function (sysFileId) {
            sysfileSvc.downloadFile(sysFileId);
        }

        //生成发文字号
        vm.createDispatchFileNum = function () {
            signSvc.createDispatchFileNum(vm.model.signid, vm.dispatchDoc.id, function (data) {
                if (data.flag || data.reCode == "ok") {
                    vm.dispatchDoc.fileNum = data.reObj;
                }
                bsWin.alert(data.reMsg);
            });
        }

        //生成发文模板
        vm.dispatchTemplate = function () {
            signSvc.createDispatchTemplate(vm , function(data){
                if (data.flag || data.reCode == 'ok') {
                    bsWin.success(data.reMsg);
                    sysfileSvc.findByMianId(vm.model.signid, function (data) {
                        if (data || data.length > 0) {
                            vm.showFlag.tabSysFile = true;
                            vm.sysFileList = data;
                            sysfileSvc.initZtreeClient(vm, $scope);//树形图
                        }
                    });
                } else {
                    bsWin.alert(data.reMsg);
                }
            });
        }

        //监听是否通过
        vm.watchPassDis = function () {
            //监听是否关联事件
            $scope.$watch("vm.businessFlag.passDis", function (newValue, oldValue) {
                if (newValue == 9) {
                    vm.flow.dealOption = "核稿无误";
                } else {
                    vm.flow.dealOption = "核稿有误";
                }
            });
        }

        //确定实际参加会议的专家
        vm.affirmJoinExpert = function () {
            if (vm.model.expertReviewDto && vm.model.expertReviewDto.expertSelectedDtoList) {
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
            } else {
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
                console.log(ids.join(','));
                expertReviewSvc.updateJoinState(vm.expertReview.id,"", "", ids.join(','), '9', vm.isCommit, function (data) {
                    if(data.flag || data.reCode == 'ok'){
                        vm.reFleshJoinState(ids, '9');
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
                expertReviewSvc.updateJoinState(vm.expertReview.id,"", "", ids.join(','), '0', vm.isCommit, function (data) {
                    if(data.flag || data.reCode == 'ok'){
                        vm.reFleshJoinState(ids, '0');
                        bsWin.success("操作成功！");
                    }else{
                        bsWin.success(data.reMsg);
                    }
                });
            }
        }

        //更新参加未参加状态
        vm.reFleshJoinState = function (ids, state) {
            $.each(ids, function (i, obj) {
                //1、删除已确认的专家
                $.each(vm.confirmEPList, function (index, epObj) {
                    if (obj == epObj.id) {
                        epObj.isJoin = state;
                    }
                })
            })
            //刷新工作方案的专家信息
            $.each(vm.model.workProgramDtoList, function (i, wpObj) {
                expertReviewSvc.refleshBusinessEP(wpObj.id, function (data) {
                    wpObj.expertDtoList = data;
                });
            })
        }

        //工作方案模板打印
        vm.printpage = function ($event) {
            var id = $($event.target).attr("id");
            signSvc.workProgramPrint(id);
        }

        /**
         * 生成评审报告
         */
        vm.reviewReportDoc = function () {
            bsWin.confirm({
                title: "",
                message: "如果之前已经生成评审报告，则本次操作会覆盖之前生成的文档，确定执行操作么？",
                onOk: function () {
                    signSvc.createDispatchTemplate(vm, function (data) {
                        if (data.flag || data.reCode == 'ok') {
                            bsWin.success("操作成功！", function () {
                                sysfileSvc.findByMianId(vm.model.signid, function (data) {
                                    if (data || data.length > 0) {
                                        vm.showFlag.tabSysFile = true;
                                        vm.sysFileList = data;
                                        sysfileSvc.initZtreeClient(vm, $scope);//树形图
                                    }
                                })
                            });
                        } else {
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            })
        }

        /**
         * 打印功能 -分页
         */
        vm.templatePage = function (id) {
            templatePrintSvc.templatePage(id);
        }

        /**
         * 专家评审费大于1000的可以点击进行拆分打印
         * @param expertId
         */
        vm.splitPayment = function(expertSelectId , expert , reviewCost){
            vm.expertSelect = {};
            vm.expertSelect.id = expertSelectId;
            vm.expertSelect.isSplit = 9;
            vm.expertSelect.oneCost = "1000";

            vm.expertName = expert.name;
            vm.reviewCost = reviewCost
            $("#splitPayment").kendoWindow({
                width: "50%",
                height: "300px",
                title: "专家评审费打印方案",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();

            $scope.$watch("vm.expertSelect.isSplit",function (newValue, oldValue) {
                //由关联改成未关联
                if(newValue != oldValue ){
                    if(vm.expertSelect.isSplit == 9){
                        vm.expertSelect.oneCost = "1000";
                    }
                    if(vm.expertSelect.isSplit == 0){
                        vm.expertSelect.oneCost = "0";
                    }
                }
            });
        }

        /**
         * 保存打印方案
         */
        vm.saveSplit = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                expertReviewSvc.saveSplit(vm);
            }
        }

    }
})();
