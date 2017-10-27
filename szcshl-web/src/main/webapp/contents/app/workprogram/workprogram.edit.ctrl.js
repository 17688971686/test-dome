(function () {
    'use strict';

    angular.module('app').controller('workprogramEditCtrl', workprogram);

    workprogram.$inject = ['workprogramSvc','$state','bsWin','sysfileSvc','$scope','meetingSvc','roomSvc'];

    function workprogram(workprogramSvc,$state,bsWin,sysfileSvc,$scope,meetingSvc,roomSvc) {
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

        vm.businessFlag={
            isSelfReview : false,          //是否自评
            isSingleReview : true,         //是否单个评审
            isMainWorkProj: false,         //是否是合并评审主项目
            isLoadMeetRoom: false,         //是否已经加载了会议室
            isReveiwAWP:false,             //是否是合并评审次项目，如果是，则不允许修改，由主项目控制
        }

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
        vm.initFileUpload = function(){
            if(!vm.work.id){
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.work.id",function (newValue, oldValue) {
                    if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                        vm.initFileUpload();
                    }
                });
            }

            //创建附件对象
            vm.sysFile = {
                businessId : vm.work.id,
                mainId : vm.work.signId,
                mainType : sysfileSvc.mainTypeValue().SIGN,
                sysfileType:sysfileSvc.mainTypeValue().WORKPROGRAM,
                sysBusiType:sysfileSvc.mainTypeValue().WORKPROGRAM,
            };
            sysfileSvc.initUploadOptions({
                inputId:"sysfileinput",
                vm:vm
            });
        }
        //评审方式修改
        vm.reviewTypeChange = function(){

            if("专家评审会" == vm.work.reviewType){
                if("合并评审" == vm.work.isSigle){
                    vm.work.isMainProject = 9;
                }
            }
            //自评的话，不需要会议和专家
           else if("自评" == vm.work.reviewType){

                if("合并评审" == vm.work.isSigle){
                    vm.work.isSigle = "单个评审";

                    common.alert({
                        vm: vm,
                        msg: "自评方案不能进行合并评审！",
                        colseDialog:true,
                    })
                }
            //专家函评不需要会议室
            }else {
                //合并评审改为单个评审
                if( vm.businessFlag.isMainWorkProj && !vm.businessFlag.isSingleReview && "单个评审" == vm.work.isSigle){
                    bsWin.confirm({
                        title: "询问提示",
                        message: "该项目已经关联其他合并评审会关联，您确定要改为单个评审吗？",
                        onOk: function () {
                            $('.confirmDialog').modal('hide');
                            vm.work.isMainProject = "0";
                            workprogramSvc.deleteAllMerge($state.params.signid,function(data){
                                if(data.flag || data.reCode =='ok'){
                                    bsWin.alert("操作成功！");
                                }else{
                                    bsWin.error("操作失败！");
                                }
                            });
                        },
                        onCancel:function(){
                            vm.work.isSigle = "合并评审"
                            $('.confirmDialog').modal('hide');
                        }
                    });
                }
            }
        }
        
        //关闭窗口
        vm.onWorkClose=function(){
        	window.parent.$(".workPro").data("kendoWindow").close();
        }

        //重置合并发文
        vm.formReset = function () {
            vm.searchSign = {};
        }
        //过滤器
        vm.filterSign = function(item){
            var isMatch = true;
            if(vm.searchSign.projectname && (item.projectname).indexOf(vm.searchSign.projectname) == -1){
                isMatch = false;
            }
            if(isMatch && vm.searchSign.reviewstage && (item.reviewstage).indexOf(vm.searchSign.reviewstage) == -1){
                isMatch = false;
            }
            if(isMatch){
                return item;
            }
        }

        //初始化合并评审弹框
        vm.initMergeWP = function(){
            if (!vm.work.id) {
                bsWin.alert("请先保存工作方案！");
            }else{
                //初始化合并评审信息
                workprogramSvc.initMergeInfo(vm,vm.work.signId);
                $("#mergeSign").kendoWindow({
                    width: "75%",
                    height: "700px",
                    title: "合并评审",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            }
        }

        //选择项目
        vm.chooseSign = function(){
            var selIds = $("input[name='mergeSign']:checked");
            if(selIds.length == 0){
                bsWin.alert("请选择要合并评审的项目！");
            }else{
                var signIdArr = [];
                $.each(selIds, function (i, obj) {
                    signIdArr.push(obj.value);
                });
                workprogramSvc.chooseSign(vm.work.signId,signIdArr.join(","),function (data) {
                    if (data.flag || data.reCode == "ok") {
                        workprogramSvc.initMergeInfo(vm,vm.work.signId);
                    }
                    bsWin.alert(data.reMsg);
                });
            }
        }

        //取消项目
        vm.cancelSign = function(){
            var selIds = $("input[name='cancelMergeSignid']:checked");
            if(selIds.length == 0){
                bsWin.alert("请选择要取消合并评审的项目！");
            }else{
                var selSignIdArr = [];
                $.each(selIds, function (i, obj) {
                    selSignIdArr.push(obj.value);
                });
            }
        	workprogramSvc.cancelMergeSign(vm.work.signId,selSignIdArr.join(","),function (data) {
                if (data.flag || data.reCode == "ok") {
                    workprogramSvc.initMergeInfo(vm,vm.work.signId);
                }
                bsWin.alert(data.reMsg);
            });
        }
     
        /*********************  S_会议室模块   *************************/
        //会议预定添加弹窗
        vm.addTimeStage = function(){
            if(vm.work.id){
                $state.go('room',{businessId:vm.work.id,businessType:"SIGN_WP"});
            }else{
                bsWin.alert("请先保存！");
            }
        }

        //修改会议预定信息
        vm.updateBookRoom = function(id){
            if(!vm.meetingList){
                //查找所有会议室地
                meetingSvc.findAllMeeting(function (data) {
                    vm.meetingList = data;
                })
            }
            $.each(vm.work.roomBookingDtos, function(key, val) {
                if(id == val.id){
                    vm.roombook = val;
                }
            } );

            $("#roomBookDetailWindow").kendoWindow({
                width : "50%",
                height : "620px",
                title : "会议预定信息",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
        }
        
        //保存预定会议室信息
        vm.saveRoom = function(){
            roomSvc.saveRoom(vm.roombook,function(data){
                if (data.flag || data.reCode == "ok") {
                    //替换修改的会议信息
                    $.each( vm.work.roomBookingDtos, function(key, val) {
                        if(vm.roombook.id == val.id){
                            val = data.reObj;
                        }
                    } );
                    bsWin.success("操作成功！",function () {
                        vm.onRoomClose();
                    });
                } else {
                    bsWin.error(data.reMsg);
                }

            })
        }

        vm.onRoomClose = function(){
            window.parent.$("#roomBookDetailWindow").data("kendoWindow").close();
        }

        //删除会议室预定情况(预定的会议室不能删除)
        /*vm.deleteBookRoom = function(id){
            bsWin.confirm({
                title: "询问提示",
                message: "是否进行该操作！",
                onOk: function () {
                    workprogramSvc.deleteBookRoom(id,function(){
                        bsWin.success("操作成功！");
                        $.each( vm.work.roomBookingDtos, function(key, val) {
                            if(id == val.id){
                                vm.work.roomBookingDtos.splice(key, 1);
                            }
                        } );
                    })
                },
            });
        }*/
        /*********************  E_会议室模块   *************************/

        //查询评估部门
        vm.findUsersByOrgId = function(type){
        	workprogramSvc.findUsersByOrgId(vm,type);
        }
        
        vm.create = function () {
            common.initJqValidation($("#work_program_form"));
            var isValid = $("#work_program_form").valid();
            if(isValid){
                workprogramSvc.createWP(vm.work,false,vm.iscommit,function(data){
                    if (data.flag || data.reCode == "ok") {
                        vm.work.id = data.reObj.id;
                        //初始化数值
                        if(data.reObj.reviewType == "自评"){
                            vm.businessFlag.isSelfReview = true;           //是否自评
                        }
                        if(data.reObj.isSigle == "合并评审"){
                            vm.businessFlag.isSingleReview = false;         //是否单个评审
                        }
                        if(data.reObj.isMainProject == "9"){
                            vm.businessFlag.isMainWorkProj = true;           //合并评审主项目
                        }
                        bsWin.success("操作成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("表格填写不正确，请检查相应的必填项信息！");
            }
        };

        //拟聘请专家
        vm.selectExpert = function(){
            if (vm.work.id) {
                $state.go('expertReviewEdit', {businessId:vm.work.signId,minBusinessId: vm.work.id,businessType:"SIGN"});
            } else {
                bsWin.alert("请先保存当前信息，再继续操作！");
            }
        }
    }
})();
