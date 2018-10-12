(function () {
    'use strict';

    angular.module('app').controller('maintWorkprogramEditCtrl', mainworkprogram);

    mainworkprogram.$inject = ['workprogramSvc', '$state', 'bsWin', 'sysfileSvc', '$scope', '$window', '$rootScope', 'signSvc' , 'expertSvc'];

    function mainworkprogram(workprogramSvc, $state, bsWin, sysfileSvc, $scope, $window, $rootScope, signSvc , expertSvc) {
        var vm = this;
        vm.work = {};						//创建一个form对象
        vm.model = {};                      //项目对象
        vm.title = '评审方案编辑';        	//标题
        vm.startDateTime = new Date("2006/6/1 08:00");
        vm.endDateTime = new Date("2030/6/1 21:00");
        vm.work.signId = $state.params.signid;		//收文ID
        vm.work.id = "";

        vm.sign = {};						//创建收文对象
        vm.unSeledWork = {};                //未选择的工作方案
        vm.searchSign = {};                 //用于过滤

        vm.businessFlag = {
            isSelfReview: false,            //是否自评
            isSingleReview: true,           //是否单个评审
            isMainWorkProj: false,          //是否是合并评审主项目
            isLoadMeetRoom: false,          //是否已经加载了会议室
            isReveiwAWP: false,             //是否是合并评审次项目，如果是，则不允许修改，由主项目控制
        }
        vm.expertList = new Array(15);     //用于打印页面的专家列表，控制行数
        //页面初始化
        activate();
        function activate() {
            vm.showAll = true;
            workprogramSvc.workMaintainList(vm, function (data) {
                vm.workProgramDtoList = data.WPList;
                //如果存在多个分支的情况，则显示项目总投资
                if (data.showTotalInvestment == '9' || data.showTotalInvestment == 9) {
                    vm.showTotalInvestment = true;
                } else {
                    vm.showTotalInvestment = false;
                }
                if (vm.workProgramDtoList && vm.workProgramDtoList.length > 0) {
                    angular.forEach(vm.workProgramDtoList, function (wp, index) {
                        wp.projectTypeDicts = $rootScope.topSelectChange(wp.projectType, $rootScope.DICT.PROJECTTYPE.dicts)
                    })
                }
            });
        }

        vm.goBackMain = function () {
            $state.go('MaintainProjectEdit', {signid: $state.params.signid, processInstanceId: null});
        }

        //评审方式修改
        vm.reviewTypeChange = function (wp) {
            workprogramSvc.findById(wp.id, function (data) {
                if (data.expertCost) {
                    wp.expertCost = data.expertCost;
                }
                //1、由合并评审改为单个评审
                if (data.isSigle == '合并评审' && data.isMainProject == "9" && "单个评审" == wp.isSigle) {
                    bsWin.confirm({
                        title: "询问提示",
                        message: "该项目已关联其他项目，您确定要改为单个评审吗？",
                        onOk: function () {
                            workprogramSvc.deleteAllMerge($state.params.signid, wp.id, function (data) {
                                if (data.flag || data.reCode == 'ok') {
                                    bsWin.alert("操作成功！", function () {
                                        $state.reload('maintWorkprogramEdit',{signid:$state.params.signid});
                                    });
                                } else {
                                    bsWin.error("操作失败！");
                                }
                            });
                        },
                        onCancel: function () {
                            $state.reload('maintWorkprogramEdit',{signid:$state.params.signid});
                        }
                    });
                    //由专家评审会改成专家函评，并且已经预定了会议室
                } else if (data.reviewType == '专家评审会' && wp.reviewType == '专家函评' && wp.roomBookingDtos && wp.roomBookingDtos.length > 0) {
                    bsWin.confirm({
                        title: "询问提示",
                        message: "改成专家函评，预定会议室将会删除，确定修改么？",
                        onOk: function () {
                            workprogramSvc.updateReviewType($state.params.signid, wp.id, wp.reviewType, function (data) {
                                if (data.flag || data.reCode == 'ok') {
                                    bsWin.alert("操作成功！",function(){
                                        $state.reload('maintWorkprogramEdit',{signid:$state.params.signid});
                                    });
                                } else {
                                    bsWin.error("操作失败！");
                                }
                            });
                        },
                        onCancel: function () {
                            $state.reload('maintWorkprogramEdit',{signid:$state.params.signid});
                        }
                    });

                } else if ((data.reviewType == '专家评审会' || data.reviewType == '专家函评') && wp.reviewType == '自评') {
                    var isNeedUpdate = false;
                    if (wp.roomBookingDtos && wp.roomBookingDtos.length > 0) {
                        isNeedUpdate = true;
                    }
                    if (wp.expertSelectedDtoList && wp.expertSelectedDtoList.length > 0) {
                        isNeedUpdate = true;
                    }
                    if (isNeedUpdate) {
                        bsWin.confirm({
                            title: "询问提示",
                            message: "评审方式改成自评，预定会议室和抽取专家将会删除，确定修改么？",
                            onOk: function () {
                                workprogramSvc.updateReviewType($state.params.signid, wp.id, wp.reviewType, function (data) {
                                    if (data.flag || data.reCode == 'ok') {
                                        bsWin.alert("操作成功！",function(){
                                            $state.reload('maintWorkprogramEdit',{signid:$state.params.signid});
                                        });
                                    } else {
                                        bsWin.error("操作失败！");
                                    }
                                });
                            },
                            onCancel: function () {
                                $state.reload('maintWorkprogramEdit',{signid:$state.params.signid});
                            }
                        });
                    }
                }
            });
        }

        //关闭窗口
        vm.onWorkClose = function () {
            window.parent.$(".workPro").data("kendoWindow").close();
        }

        //重置合并发文
        vm.formReset = function () {
            vm.searchSign = {};
        }

        //过滤器
        vm.filterSign = function (item) {
            var isMatch = true;
            if (vm.searchSign.projectname && (item.projectname).indexOf(vm.searchSign.projectname) == -1) {
                isMatch = false;
            }
            if (isMatch && vm.searchSign.reviewstage && (item.reviewstage).indexOf(vm.searchSign.reviewstage) == -1) {
                isMatch = false;
            }
            if (isMatch) {
                return item;
            }
        }

        //初始化合并评审弹框
        vm.initMergeWP = function (wp) {
            //1、先判断该工作方案是否已经保存
            workprogramSvc.findById(wp.id, function (data) {
                //2、如果已经保存，则弹框
                if (data.isSigle == '合并评审' && data.isMainProject == "9") {
                    //初始化合并评审信息
                    workprogramSvc.initMergeInfo(vm, wp.signId);
                    $("#mergeSign").kendoWindow({
                        width: "75%",
                        height: "700px",
                        title: "合并评审",
                        visible: false,
                        modal: true,
                        closable: true,
                        actions: ["Pin", "Minimize", "Maximize", "Close"]
                    }).data("kendoWindow").center().open();
                } else {
                    bsWin.alert("请先保存工作方案！");
                }
            });
        }

        //选择项目
        vm.chooseSign = function () {
            var selIds = $("input[name='mergeSign']:checked");
            if (selIds.length == 0) {
                bsWin.alert("请选择要合并评审的项目！");
            } else {
                var signIdArr = [];
                $.each(selIds, function (i, obj) {
                    signIdArr.push(obj.value);
                });
                workprogramSvc.chooseSign(vm.work.signId, signIdArr.join(","), function (data) {
                    if (data.flag || data.reCode == "ok") {
                        workprogramSvc.initMergeInfo(vm, vm.work.signId);
                    }
                    bsWin.alert(data.reMsg);
                });
            }
        }

        //取消项目
        vm.cancelSign = function () {
            var selIds = $("input[name='cancelMergeSignid']:checked");
            if (selIds.length == 0) {
                bsWin.alert("请选择要取消合并评审的项目！");
            } else {
                var selSignIdArr = [];
                $.each(selIds, function (i, obj) {
                    selSignIdArr.push(obj.value);
                });
            }
            workprogramSvc.cancelMergeSign(vm.work.signId, selSignIdArr.join(","), function (data) {
                if (data.flag || data.reCode == "ok") {
                    workprogramSvc.initMergeInfo(vm, vm.work.signId);
                }
                bsWin.alert(data.reMsg);
            });
        }

        /*********************  S_会议室模块   *************************/
        //会议预定添加弹窗
        vm.addTimeStage = function (id) {
            if (id) {
                workprogramSvc.findById(id, function (data) {
                    //2、如果已经保存，则弹框
                    if (data.reviewType == '专家评审会') {
                        $state.go('room', {businessId: id, businessType: "SIGN_WP"});
                    } else {
                        bsWin.alert("请先保存工作方案！");
                    }
                });
            } else {
                bsWin.alert("请先保存！");
            }
        }
        //会议预定添加弹窗维维护管理模块
        /*        vm.addTimeStageMain = function () {
         if (vm.work.id) {
         workprogramSvc.findById(vm.work.id,function(data){
         //2、如果已经保存，则弹框
         if(data.reviewType == '专家评审会'){
         $state.go('room', {businessId: vm.work.id, businessType: "SIGN_WP",mainFlag:"1"});
         }else{
         bsWin.alert("请先保存工作方案！");
         }
         });
         } else {
         bsWin.alert("请先保存！");
         }
         }*/
        /*********************  E_会议室模块   *************************/

        //查询评估部门
        vm.findUsersByOrgId = function (type) {
            workprogramSvc.findUsersByOrgId(vm, type);
        }

        //判断调研时间的结束时间是否小于开始时间
        vm.compare = function (studyBeginTimeStr1, studyEndTimeStr1) {
            var studyBeginTimeStr = parseInt(studyBeginTimeStr1.split(":")[0]);
            var studyEndTimeStr = parseInt(studyEndTimeStr1.split(":")[0]);
            if (studyBeginTimeStr == studyEndTimeStr) {//当“时”想等时，判断“分”
                var beginTime = parseInt(studyBeginTimeStr1.split(":")[1]);
                var endTime = parseInt(studyEndTimeStr1.split(":")[1]);
                if (beginTime > endTime) { //判断“分”
                    vm.isTime = true;
                } else {
                    vm.isTime = false;
                }

            } else {
                if (studyBeginTimeStr > studyEndTimeStr) { //判断"时"
                    vm.isTime = true;
                } else {
                    vm.isTime = false;
                }
            }

        }

        //拟聘请专家
        vm.selectExpert = function (id, reviewType, roomBookingDtos) {
            if (id) {
                //2、如果已经保存，则弹框
                workprogramSvc.findById(id, function (data) {
                    if (data.reviewType == '专家评审会' || data.reviewType == '专家函评') {
                        //先让用户选择会议时间
                        if (reviewType == '专家评审会' && (!roomBookingDtos || roomBookingDtos.length == 0)) {
                            bsWin.alert("请先预定评审会日期！");
                        } else if (reviewType == '专家函评' && !data.letterDate) {
                            bsWin.alert("请先选择函评日期并保存！");
                        } else {
                            $state.go('expertReviewEdit', {
                                businessId: vm.work.signId,
                                minBusinessId: id,
                                businessType: "SIGN",
                                reviewType: reviewType,
                                isback: true
                            });
                        }
                    } else {
                        bsWin.alert("请先保存工作方案！");
                    }
                });
            } else {
                bsWin.alert("请先保存当前信息，再继续操作！");
            }
        }

        //签收模板打印
        vm.printpage = function ($event) {
            var id = $($event.target).attr("id");
            signSvc.workProgramPrint(id);
        }

        //维护项目时的工作方案的保存
        vm.createMaintain = function (wp,formId) {
            common.initJqValidation($("#"+formId));
           var isValid = $($("#"+formId)).valid();
            if (isValid) {
                if(!vm.isTime){
                    if(wp.studyQuantum=="全天") {
                        wp.studyBeginTime="";
                        wp.studyEndTime="";
                    }else{
                        if($("#studyAllDay").val() && wp.studyBeginTimeStr){
                            wp.studyBeginTime = $("#studyAllDay").val() + " " + wp.studyBeginTimeStr + ":00";

                        }
                        if ($("#studyAllDay").val() && wp.studyEndTimeStr) {
                            wp.studyEndTime = $("#studyAllDay").val() + " " + wp.studyEndTimeStr + ":00";
                        }
                    }
                   workprogramSvc.createWP(wp, false, vm.iscommit, function (data) {
                        bsWin.alert("操作成功");
                    });
                }else{
                    bsWin.alert("结束时间必须大于开始时间！");
                }

            } else {
                bsWin.alert("操作失败，有红色*号的选项为必填项，请按要求填写！");
            }
        }

        /**
         * 查看专家信息
         * @param expertId
         */
        vm.checkExpertDetail = function(expertId){

            console.log(4);
            vm.expert = [];
            vm.id = expertId;
            expertSvc.getExpertById(vm.id, function (data) {
                vm.expert = data;
                $("#queryExportDetailsMWP").kendoWindow({
                    width: "80%",
                    height: "620px",
                    title: "专家详细信息",
                    visible: false,
                    modal: true,
                    open:function(){
                        $("#expertPhotoSrc").attr("src", rootPath + "/expert/transportImg?expertId=" + vm.expert.expertID + "&t=" + Math.random());
                        //tab标签
                        $("#busi_baseinfoMWP").addClass("active").addClass("in").show(500);
                        $('#myTabExpertMWP li').click(function (e) {
                            $("#busi_baseinfoMWP").removeClass("active").removeClass("in");
                            var aObj = $("a", this);
                            e.preventDefault();
                            aObj.tab('show');
                            var showDiv = aObj.attr("for-div");
                            // $("#" + showDiv).removeClass("active").removeClass("in");
                            $("#" + showDiv).addClass("active").addClass("in").show(500);
                        })
                        //评审过项目
                        vm.reviewProjectList2 = [];
                        expertSvc.reviewProjectGrid(vm.id,function(data){
                            vm.isLoading = false;
                            if(data && data.length > 0){
                                vm.reviewProjectList2 = data;
                                vm.noData = false;
                            }else{
                                vm.noData = true;
                            }

                        });
                    },
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            });
        }
    }
})();
