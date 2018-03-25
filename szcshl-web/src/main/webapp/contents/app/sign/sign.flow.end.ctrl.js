(function () {
    'use strict';

    angular.module('app').controller('signEndCtrl', sign);

    sign.$inject = ['sysfileSvc','signSvc','$state','flowSvc','$scope','templatePrintSvc'];

    function sign(sysfileSvc,signSvc,$state,flowSvc,$scope,templatePrintSvc) {
        var vm = this;
        vm.title = "已办结项目详情";
        vm.model = {};
        vm.flow = {};
        vm.model.signid = $state.params.signid;   //业务ID
        vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID
        vm.flow.hideFlowImg = true;

        //按钮显示控制，全部归为这个对象控制
        vm.showFlag = {
            tabWorkProgram:false,       // 显示工作方案标签tab
            tabBaseWP:false,            // 项目基本信息tab
            tabDispatch:false,          // 发文信息tab
            tabFilerecord:false,        // 归档信息tab
            tabExpert:false,            // 专家信息tab
            tabSysFile:false,           // 附件信息tab
            tabAssociateSigns:false,    // 关联项目tab
        };

        //业务控制对象
        vm.businessFlag = {
            expertReviews : []
        }
        active();
        function active(){
            $('#myTab li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
                vm.model.showDiv = showDiv;
            })

            flowSvc.initFlowData(vm);
            //初始化业务信息
            signSvc.initFlowPageData(vm.model.signid,function(data){
                vm.model = data;
                var deActive = $("#myTab .active");
                var deObj = $("a", deActive);
                vm.model.showDiv = deObj.attr("for-div");
                //发文
                if (vm.model.dispatchDocDto) {
                    vm.showFlag.tabDispatch = true;
                    vm.dispatchDoc = vm.model.dispatchDocDto;
                }
                //归档
                if (vm.model.fileRecordDto) {
                    vm.showFlag.tabFilerecord = true;
                    vm.fileRecord = vm.model.fileRecordDto;
                }

                //初始化专家评分
                if (vm.model.processState > 1) {
                    vm.showFlag.tabWorkProgram=true;        //显示工作方案
                }

                //显示拟补充资料函
                if(vm.model.suppLetterDtoList){
                    vm.showSupperIndex = 0;
                }
                //拟补充资料信息
                if(vm.model.registerFileDtoDtoList!=undefined){
                    vm.supply=[];//拟补充资料
                    vm.registerFile=[];//其他资料
                    vm.drawingFile=[];//图纸资料
                    vm.otherFile=[];//归档的其他资料
                    vm.model.registerFileDtoDtoList.forEach(function(registerFile  , x){
                        if(registerFile.businessType =="3"){
                            vm.supply.push(registerFile);
                        }else if(registerFile.businessType =="2"){
                            vm.drawingFile.push(registerFile);
                        }else if(registerFile.businessType =="1" ||registerFile.businessType =="4"){
                            vm.registerFile.push(registerFile);
                        }else if(registerFile.businessType =="5" ||registerFile.businessType =="6"||registerFile.businessType =="7"){
                            vm.otherFile.push(registerFile);
                        }
                    })
                }
            });

            // 初始化上传附件
            sysfileSvc.findByMianId(vm.model.signid,function(data){
                if(data && data.length > 0){
                    vm.showFlag.tabSysFile = true;
                    vm.sysFileList = data;
                    sysfileSvc.initZtreeClient(vm,$scope);//树形图
                }
            });
        }
        //附件下载
        vm.commonDownloadSysFile = function(sysFileId){
            sysfileSvc.downloadFile(sysFileId);
        }

        //附件在线编辑
        vm.commonEditSysFile = function(sysFileId){
            $("#editSysFileWindow").kendoWindow({
                width: "80%",
                title: "在线编辑",
                content: rootPath + "/file/editFile?sysFileId="+sysFileId,
            });
        }
        //签收模板打印
        vm.printpage = function ($event) {
            templatePrintSvc.templatePrint($event.target,vm.model);
        }
    }
})();
