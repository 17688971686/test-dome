(function () {
    'use strict';

    angular.module('app').controller('workprogramEditCtrl', workprogram);

    workprogram.$inject = ['workprogramSvc', '$state', 'bsWin', 'sysfileSvc', '$scope', 'meetingSvc', 'roomSvc','signSvc'];

    function workprogram(workprogramSvc, $state, bsWin, sysfileSvc, $scope, meetingSvc, roomSvc,signSvc) {
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
            isSelfReview: false,          //是否自评
            isSingleReview: true,         //是否单个评审
            isMainWorkProj: false,         //是否是合并评审主项目
            isLoadMeetRoom: false,         //是否已经加载了会议室
            isReveiwAWP: false,             //是否是合并评审次项目，如果是，则不允许修改，由主项目控制
        }
        vm.expertList =  new Array(15); //用于打印页面的专家列表，控制行数
        //页面初始化
        activate();
        function activate() {
            vm.showAll = true;
            workprogramSvc.initPage(vm);
            $('#wpTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
            })
            //查询会议列表
        }

        //初始化附件上传控件
        vm.initFileUpload = function () {
            if (!vm.work.id) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.work.id", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        vm.initFileUpload();
                    }
                });
            }

            //创建附件对象
            vm.sysFile = {
                businessId: vm.work.id,
                mainId: vm.work.signId,
                mainType: sysfileSvc.mainTypeValue().SIGN,
                sysfileType: sysfileSvc.mainTypeValue().WORKPROGRAM,
                sysBusiType: sysfileSvc.mainTypeValue().WORKPROGRAM,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm
            });
        }

        //评审方式修改
        vm.reviewTypeChange = function () {
            if(vm.work.reviewType =='自评'){
                vm.work.expertCost="";
            }
            //如果已经保存了工作方案，则从数据库查找工作方案内容进行对比
            if(vm.work.id){
                workprogramSvc.findById(vm.work.id,function(data){
                    if(data.expertCost!=undefined && vm.work.expertCost==""){
                        vm.work.expertCost=data.expertCost;
                    }
                    //1、由合并评审改为单个评审
                    if(data.isSigle == '合并评审' && data.isMainProject == "9" && "单个评审" == vm.work.isSigle){
                        bsWin.confirm({
                            title: "询问提示",
                            message: "该项目已经关联其他合并评审会关联，您确定要改为单个评审吗？",
                            onOk: function () {
                                workprogramSvc.deleteAllMerge($state.params.signid,vm.work.id, function (data) {
                                    if (data.flag || data.reCode == 'ok') {
                                        bsWin.alert("操作成功！",function(){
                                            vm.work.isMainProject = "0";
                                        });
                                    } else {
                                        bsWin.error("操作失败！");
                                    }
                                });
                            },
                            onCancel: function () {
                                vm.work.isSigle = "合并评审";
                                vm.work.isMainProject = "9";
                            }
                        });
                    //由专家评审会改成专家函评，并且已经预定了会议室
                    }else if(data.reviewType =='专家评审会' && vm.work.reviewType =='专家函评' && vm.work.roomBookingDtos && vm.work.roomBookingDtos.length > 0){
                        bsWin.confirm({
                            title: "询问提示",
                            message: "改成专家函评，预定会议室将会删除，确定修改么？",
                            onOk: function () {
                                workprogramSvc.updateReviewType($state.params.signid,vm.work.id,vm.work.reviewType, function (data) {
                                    if (data.flag || data.reCode == 'ok') {
                                        vm.work = data.reObj;
                                        vm.work.signId = $state.params.signid;
                                        bsWin.alert("操作成功！");
                                    } else {
                                        bsWin.error("操作失败！");
                                    }
                                });
                            },
                            onCancel: function () {
                                vm.work.reviewType = data.reviewType;
                            }
                        });

                    }else if((data.reviewType =='专家评审会' || data.reviewType =='专家函评') && vm.work.reviewType =='自评'){
                        var isNeedUpdate = false;
                        if(vm.work.roomBookingDtos && vm.work.roomBookingDtos.length > 0){
                            isNeedUpdate = true;
                        }else if(vm.work.expertDtoList &&vm.work.expertDtoList.length > 0){
                            isNeedUpdate = true;
                        }
                        if(isNeedUpdate){
                            bsWin.confirm({
                                title: "询问提示",
                                message: "评审方式改成自评，预定会议室和抽取专家将会删除，确定修改么？",
                                onOk: function () {
                                    workprogramSvc.updateReviewType($state.params.signid,vm.work.id,vm.work.reviewType, function (data) {
                                        if (data.flag || data.reCode == 'ok') {
                                            vm.work = data.reObj;
                                            vm.work.signId = $state.params.signid;
                                            bsWin.alert("操作成功！");
                                        } else {
                                            bsWin.error("操作失败！");
                                        }
                                    });
                                },
                                onCancel: function () {
                                    vm.work.reviewType = data.reviewType;
                                }
                            });
                        }
                    }

                });
            }
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
        vm.initMergeWP = function () {
            if (!vm.work.id) {
                bsWin.alert("请先保存工作方案！");
            } else {
                //1、先判断该工作方案是否已经保存
                workprogramSvc.findById(vm.work.id,function(data){
                    //2、如果已经保存，则弹框
                    if(data.isSigle == '合并评审' && data.isMainProject == "9"){
                        //初始化合并评审信息
                        workprogramSvc.initMergeInfo(vm, vm.work.signId);
                        $("#mergeSign").kendoWindow({
                            width: "75%",
                            height: "700px",
                            title: "合并评审",
                            visible: false,
                            modal: true,
                            closable: true,
                            actions: ["Pin", "Minimize", "Maximize", "Close"]
                        }).data("kendoWindow").center().open();
                    }else{
                        bsWin.alert("请先保存工作方案！");
                    }
                });
            }
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
        vm.addTimeStage = function () {
            if (vm.work.id) {
                workprogramSvc.findById(vm.work.id,function(data){
                    //2、如果已经保存，则弹框
                    if(data.reviewType == '专家评审会'){
                        $state.go('room', {businessId: vm.work.id, businessType: "SIGN_WP"});
                    }else{
                        bsWin.alert("请先保存工作方案！");
                    }
                });
            } else {
                bsWin.alert("请先保存！");
            }
        }
        /*********************  E_会议室模块   *************************/

        //查询评估部门
        vm.findUsersByOrgId = function (type) {
            workprogramSvc.findUsersByOrgId(vm, type);
        }

        vm.create = function () {
            var id = $("#work_program_form").find('div[class="ng-scope"]').attr("id");
            common.initJqValidation($("#"+id));
            var isValid = $("#work_program_form").valid();
            if (isValid) {
                workprogramSvc.createWP(vm.work, false, vm.iscommit, function (data) {
                    if (data.flag || data.reCode == "ok") {
                        vm.work.id = data.reObj.id;
                        bsWin.success("操作成功！");
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            } else {
                bsWin.alert("操作失败，有红色*号的选项为必填项，请按要求填写！");
            }
        };

        //拟聘请专家
        vm.selectExpert = function () {
            if (vm.work.id) {
                //2、如果已经保存，则弹框
                workprogramSvc.findById(vm.work.id,function(data){
                    if(data.reviewType == '专家评审会' || data.reviewType == '专家函评'){
                        //先让用户选择会议时间
                        if(vm.work.reviewType == '专家评审会' && (!vm.work.roomBookingDtos || vm.work.roomBookingDtos.length == 0) ){
                            bsWin.alert("请先预定评审会日期！");
                        }else if(vm.work.reviewType == '专家函评' && !data.letterDate){
                            bsWin.alert("请先选择函评日期并保存！");
                        }else{
                            $state.go('expertReviewEdit', {
                                businessId: vm.work.signId,
                                minBusinessId: vm.work.id,
                                businessType: "SIGN"
                            });
                        }
                    }else{
                        bsWin.alert("请先保存工作方案！");
                    }
                });
            } else {
                bsWin.alert("请先保存当前信息，再继续操作！");
            }
        }

        //签收模板打印
        vm.printpage = function ($event) {
            var id =  $($event.target).attr("id");
            signSvc.workProgramPrint(id);
        }
    }
})();
