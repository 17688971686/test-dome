(function () {
    'use strict';

    angular.module('app').controller('workprogramEditCtrl', workprogram);

    workprogram.$inject = ['workprogramSvc','$state'];

    function workprogram(workprogramSvc,$state) {
        var vm = this;
    	vm.work = {};						//创建一个form对象
        vm.title = '评审方案编辑';        	//标题
        vm.startDateTime = new Date("2006/6/1 08:00");
        vm.endDateTime = new Date("2030/6/1 21:00"); 
        vm.work.signId = $state.params.signid;		//收文ID
        if($state.params.workProgramId){
            vm.work.id  = $state.params.workProgramId;  //工作方案ID
        }
        
        vm.sign = {};						//创建收文对象
        vm.unSeledWork = {};                //未选择的工作方案
        vm.serchWork = {};                  //用于过滤

        vm.businessFlag={
            isSelfReview : false,           //是否自评
            isSingleReview : true,         //是否单个评审
            isMainWorkProj: false,          //是否是合并评审主项目

            isRoomBook : false,             //是否已经预定了会议时间
            isHavePre : false,              //预定多个会议室的时候，查看上一个
            isHaveNext : false,             //预定多个会议室 的时候，查看下一个
        }

        //页面初始化
        activate();
        function activate() {
        	workprogramSvc.initPage(vm);
            workprogramSvc.findCompanys(vm);	//查找主管部门
            workprogramSvc.findAllMeeting(vm);	//查找所有会议室地
            workprogramSvc.initReview(vm);		//初始化参数值
        }

        //评审方式修改
        vm.reviewTypeChange = function(){
            //自评的话，不需要会议和专家
            if("自评" == vm.work.reviewType){
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
                    common.confirm({
                        vm:vm,
                        title:"",
                        msg:"该项目已经关联其他合并评审会关联，您确定要改为单个评审吗？",
                        fn: function () {
                            $('.confirmDialog').modal('hide');
                            vm.work.isMainProject = "0";
                            workprogramSvc.deleteAllMerge(vm);
                        },
                        cancel:function(){
                            vm.work.isSigle = "合并评审"
                            $('.confirmDialog').modal('hide');
                        }
                    })
                }
            }
        }
        
        //关闭窗口
        vm.onWorkClose=function(){
        	window.parent.$(".workPro").data("kendoWindow").close();
        }

        //初始化合并评审弹框
        vm.initMergeWP = function(){
            if (!vm.work.id) {
                common.alert({
                    vm: vm,
                    msg: "请先保存再进行关联！",
                    colseDialog:true,
                })
            }else{
                workprogramSvc.initMergeWP(vm);
            }
        }

        //已选工作方案过滤器,排除自己本身
        vm.filterWP = function(item){
            if(vm.work.id != item.id){
                return item;
            }
        }
        //待选工作方案过滤器
        vm.filterSelWP = function(item){
            var isMatch = true;
            if(vm.serchWork.projectname){
                if(item.projectname  != vm.serchWork.projectname){
                    isMatch = false;
                }
            }
            if(isMatch){
                if(vm.serchWork.mianChargeUserName && (item.mianChargeUserName).indexOf(vm.serchWork.mianChargeUserName) == -1){
                    isMatch = false;
                }
            }
            if(isMatch){
                if(vm.serchWork.secondChargeUserName && (item.secondChargeUserName).indexOf(vm.serchWork.secondChargeUserName) == -1){
                    isMatch = false;
                }
            }
            if(isMatch){
                return item;
            }
        }

        //刷新待选工作方案
        vm.fleshWaitSelWp = function(){
            vm.serchWork = {};
            workprogramSvc.waitSeleWP(vm);
        }

        //选择项目
        vm.chooseWP = function(){
            workprogramSvc.chooseWP(vm);
        }

        //取消项目
        vm.cancelWP = function(){
        	workprogramSvc.cancelWP(vm);
        }
     
        //会议预定添加弹窗
        vm.addTimeStage = function(){
            //如果已经预定了会议室，则显示
            if(vm.businessFlag.isRoomBook){
                $("#stageWindow").kendoWindow({
                    width : "660px",
                    height : "550px",
                    title : "会议预定添加",
                    visible : false,
                    modal : true,
                    closable : true,
                    actions : [ "Pin", "Minimize", "Maximize", "Close" ]
                }).data("kendoWindow").center().open();
            //否则，跳转到选择会议室页面
            }else{
                if(vm.work.id){
                    $state.go('room', {workProgramId:vm.work.id});
                }else{
                    common.alert({
                        vm:vm,
                        msg:"请先保存，再选择评审会日期！"
                    })
                }
            }

        }
        
        //会议预定添加
        vm.saveRoom = function(){
        	workprogramSvc.saveRoom(vm);
        }

        //调整到会议室预定页面
        vm.gotoRoom = function(){
            window.parent.$("#stageWindow").data("kendoWindow").close();
            if(vm.work.id){
                $state.go('room', {workProgramId:vm.work.id});
            }else{
                common.alert({
                    vm:vm,
                    msg:"请先保存！"
                })
            }
        }

        //下一个会议预定信息
        vm.nextBookRoom = function(){
            var curIndex = 0;
            vm.RoomBookings.forEach(function (u, number) {
                if(u.id == vm.roombook.id){
                    curIndex = number;
                }
            });
            vm.businessFlag.isHavePre = true;
            vm.businessFlag.isHaveNext = (curIndex == (vm.RoomBookings.length-2))?false:true;
            vm.roombook = vm.RoomBookings[curIndex+1];
        }

        //上一次会议预定信息
        vm.preBookRoom = function(){
            var curIndex = 0;
            vm.RoomBookings.forEach(function (u, number) {
                if(u.id == vm.roombook.id){
                    curIndex = number;
                }
            });
            vm.businessFlag.isHaveNext = true;
            vm.businessFlag.isHavePre = (curIndex == 1)? false:true;
            vm.roombook = vm.RoomBookings[curIndex-1];
        }
        
        vm.onRoomClose = function(){
        	window.parent.$("#stageWindow").data("kendoWindow").close();
        }
        
        vm.queryRoom = function(){
        	workprogramSvc.queryRoom(vm);
        }
        //查询评估部门
        vm.findUsersByOrgId = function(type){
        	workprogramSvc.findUsersByOrgId(vm,type);
        }
        
        vm.create = function () {  
        	workprogramSvc.createWP(vm);
        };  
        
        vm.selectExpert = function(){
        	workprogramSvc.selectExpert(vm);
        }
            
        vm.findReviewDept = function(){
        
        }
    }
})();
