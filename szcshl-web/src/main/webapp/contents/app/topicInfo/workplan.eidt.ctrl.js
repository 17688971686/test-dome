(function () {
    'use strict';

    angular.module('app').controller('workPlanEditCtrl', workPlan);

    workPlan.$inject = ['bsWin', 'meetingSvc', 'roomSvc', 'workPlanSvc','$state','$scope','sysfileSvc'];

    function workPlan(bsWin, meetingSvc, roomSvc, workPlanSvc,$state,$scope,sysfileSvc) {
        var vm = this;
        vm.title = '课题研究成果鉴定会工作方案';
        vm.workplan = {};
        if($state.params.topicId){
            vm.workplan.topicId = $state.params.topicId;
        }

        activate();
        function activate() {
            if(vm.workplan.topicId){
                workPlanSvc.findByTopicId(vm.workplan.topicId,function(data){
                    vm.workplan = data;
                    vm.workplan.topicId = $state.params.topicId;
                    vm.initFileUpload();
                })
            }
        }

        //S_保存工作方案
        vm.saveWorkPlan = function(){
            workPlanSvc.save(vm.workplan,function (data) {
                if(data.flag || data.reCode == 'ok'){
                    vm.workplan = data.reObj;
                    bsWin.alert("保存成功！");
                }else{
                    bsWin.error(data.reMsg);
                }
            })
        }//E_saveWorkPlan

        //初始化附件上传控件
        vm.initFileUpload = function(){
            if(!vm.workplan.id){
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.workplan.id",function (newValue, oldValue) {
                    if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                        vm.initFileUpload();
                    }
                });
            }

            //创建附件对象
            vm.sysFile = {
                businessId : vm.workplan.id,
                mainId : vm.workplan.topicId,
                mainType : sysfileSvc.mainTypeValue().TOPIC,
                sysfileType:sysfileSvc.mainTypeValue().TOPIC_WORKPLAN,
                sysBusiType:sysfileSvc.mainTypeValue().TOPIC_WORKPLAN,
            };
            sysfileSvc.initUploadOptions({
                inputId:"sysfileinput",
                vm:vm
            });
        }

        //S_会议室预定
        vm.bookMeeting = function () {
            if(vm.workplan.id){
                $state.go('room',{businessId:vm.workplan.id,businessType:"TOPIC_WP"});
            }else{
                bsWin.alert("请先保存当前信息！");
            }
        }//E_addTimeStage

        //S_拟聘请专家
        vm.selectExpert = function () {
            if (vm.workplan.id) {
                $state.go('expertReviewEdit', {businessId:$state.params.topicId,minBusinessId:vm.workplan.id,businessType:"TOPIC",reviewType: vm.workplan.reviewType});
            } else {
                bsWin.alert("请先保存当前信息，再继续操作！");
            }
        }//E_selectExpert

        //S_编辑预定会议室
        vm.updateBookRoom = function(id){
            if(!vm.meetingList){
                //查找所有会议室地
                meetingSvc.findAllMeeting(function (data) {
                    vm.meetingList = data;
                })
            }
            $.each(vm.workplan.roomDtoList, function(key, val) {
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
        }//E_updateBookRoom

        //S_保存预定会议室信息
        vm.saveRoom = function(){
            roomSvc.saveRoom(vm.roombook,function(data){
                if (data.flag || data.reCode == "ok") {
                    //替换修改的会议信息
                    $.each(vm.workplan.roomDtoList, function(key, val) {
                        if(vm.roombook.id == val.id){
                            val = data.reObj;
                        }
                    } );
                    bsWin.success("操作成功！",function(){
                        vm.onRoomClose();
                    });
                } else {
                    bsWin.error(data.reMsg);
                }
            })
        }//E_saveRoom

        vm.onRoomClose = function(){
            window.parent.$("#roomBookDetailWindow").data("kendoWindow").close();
        }
    }
})();
