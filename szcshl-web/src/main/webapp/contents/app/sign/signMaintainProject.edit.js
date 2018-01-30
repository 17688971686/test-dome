(function () {
    'use strict';

    angular.module('app').controller('MaintainProjectEditCtrl', MaintainProjectEdit);

    MaintainProjectEdit.$inject = ['pauseProjectSvc','signSvc','$state','flowSvc','$scope','sysfileSvc','addRegisterFileSvc','bsWin'];

    function MaintainProjectEdit(pauseProjectSvc,signSvc,$state,flowSvc,$scope,sysfileSvc,addRegisterFileSvc,bsWin) {
        var vm = this;
        vm.title = "维护项目修改";
        vm.model = {};
        vm.model.signid = $state.params.signid;   //业务ID
        vm.model.processInstanceId = $state.params.processInstanceId;	//流程实例ID

        //初始化附件上传控件
        vm.initFileUpload = function () {
            //创建附件对象
            vm.sysFile = {
                businessId: vm.model.signid,
                mainId: vm.model.signid,
                mainType: sysfileSvc.mainTypeValue().SIGN,
                sysfileType: sysfileSvc.mainTypeValue().WORKPROGRAM,
                sysBusiType: sysfileSvc.mainTypeValue().WORKPROGRAM,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm,
                uploadSuccess: function () {
                    sysfileSvc.findByBusinessId(vm.annountment.anId, function (data) {
                        vm.sysFilelists = data;
                    });
                }
            });
        }
        active();
        function active(){
            // 初始化业务信息
            signSvc.initFlowPageData(vm.model.signid, function (data) {
                vm.model = data;
                vm.dispatchDoc=vm.model.dispatchDocDto;
                vm.fileRecordDto=vm.model.fileRecordDto;
                if(vm.model.workProgramDtoList){
                    for(var i=0;i<vm.model.workProgramDtoList.length;i++){
                        if(vm.model.workProgramDtoList[i].branchId=="1"){
                            vm.work=vm.model.workProgramDtoList[i];//主的工作方案
                            //初始化第二负责人
                            if(vm.work.secondChargeUserName){
                                vm.secondChargeUserName=vm.work.secondChargeUserName.split(",")
                            }
                        }
                    }
                }

            });
            vm.initFileUpload();
        }
        //  登记表补充资料
        vm.addRegisterFile = function () {
            $state.go('registerFile', {businessId: vm.model.signid});
        }// --链接到  登记表补充资料
        // S_跳转到 工作方案 编辑页面
        vm.addWorkProgram = function () {
            if( vm.model.processInstanceId){
                if(vm.model.workProgramDtoList){
                    $state.go('workprogramEdit', {signid: vm.model.signid,isControl:true});
                }else{
                    bsWin.alert("该项目还没有填写工作方案");
                }

            }else{
                bsWin.alert("该项目还没有发起流程");
            }
        }// E_跳转到 工作方案 编辑页面

        // S_跳转到 发文 编辑页面
        vm.addDisPatch = function () {
         if( vm.model.processInstanceId){
          if(vm.model.dispatchDocDto){
            //如果是未关联，并且是可研或者概算阶段，提醒是否要关联
            if ((!vm.model.isAssociate || vm.model.isAssociate == 0) &&
                (signcommon.getReviewStage().STAGE_STUDY == vm.model.reviewstage || signcommon.getReviewStage().STAGE_BUDGET == vm.model.reviewstage)) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "该项目还没进行项目关联，是否需要进行关联设置？",
                    onOk: function () {
                        //根据项目名称，查询要关联阶段的项目
                        if (!vm.searchAssociateSign) {
                            vm.searchAssociateSign = {
                                signid: vm.model.signid,
                                projectname: vm.model.projectname,
                            };
                        }
                        signSvc.getAssociateSign(vm.searchAssociateSign, function (data) {
                            vm.associateSignList = [];
                            if (data) {
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
                    onCancel: function () {
                        $state.go('dispatchEdit', {signid: vm.model.signid,isControl:true});
                    }
                });
            } else {
                $state.go('dispatchEdit', {signid: vm.model.signid,isControl:true});
            }
           }else{
              bsWin.alert("该项目还没有填写发文");
             }
         }else{
                bsWin.alert("该项目还没有发起流程");
            }
        }// E_跳转到 发文 编辑页面
// E_跳转到 发文 编辑页面
        vm.addDoFile = function () {
            if( vm.model.processInstanceId) {
                if(vm.modle.fileRecordDto){
                    $state.go('fileRecordEdit', {signid: vm.model.signid,isControl:true});
                }else{
                    bsWin.alert("该项目还没有填写归档");
                }

            }else{
                bsWin.alert("该项目还没有发起流程");
            }
        }

        //start  作废项目
        vm.scrap = function (signid) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认作废该条项目吗？",
                onOk: function () {
                    signSvc.deleteSign(signid,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            bsWin.alert("操作成功！",function(){
                                $state.go('MaintainProjectList');
                            })
                        }else{
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            });
        }//end 作废项目

        /**
         * 项目暂停
         * @param signid
         */
        vm.pauseProject = function () {
            pauseProjectSvc.findPausingProject(vm,vm.model.signid);
        }
        //S_链接到拟补充资料函
        vm.addSuppLetter = function () {
            $state.go('addSupp', {businessId: vm.model.signid, businessType: "SIGN",isControl:true});
        }// E_跳转到 拟补充资料函 编辑页面

        //关联其他项目阶段
        vm.relation=function () {
            //根据项目名称，查询要关联阶段的项目
            if (!vm.searchAssociateSign) {
                vm.searchAssociateSign = {
                    signid: vm.model.signid,
                    projectname: vm.model.projectname,
                };
            }
            signSvc.getAssociateSign(vm.searchAssociateSign, function (data) {
                vm.associateSignList = [];
                if (data) {
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
        }


    }
})();
