(function () {
    'use strict';

    angular.module('app').controller('signDetailsCtrl', sign);

    sign.$inject = ['sysfileSvc', 'signSvc', '$state', 'flowSvc', '$scope', 'templatePrintSvc', 'assistSvc'  , 'expertReviewSvc' , 'expertSvc'];

    function sign(sysfileSvc, signSvc, $state, flowSvc, $scope, templatePrintSvc, assistSvc , expertReviewSvc , expertSvc) {
        var vm = this;
        vm.model = {};							    //创建一个form对象
        vm.flow = {};                               //收文对象
        vm.model.signid = $state.params.signid;	    //收文ID
        vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID
        //按钮显示控制，全部归为这个对象控制
        vm.showFlag = {
            tabWorkProgram: false,       // 显示工作方案标签tab
            tabBaseWP: false,            // 项目基本信息tab
            tabDispatch: false,          // 发文信息tab
            tabFilerecord: false,        // 归档信息tab
            tabExpert: false,            // 专家信息tab
            tabSysFile: false,           // 附件信息tab
            tabAssociateSigns: false,    // 关联项目tab
        };
        //业务控制对象
        vm.businessFlag = {
            expertReviews: [],         // 专家评审方案
        }

        vm.expertList = new Array(15); //用于打印页面的专家列表，控制行数

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

            //流程图和流程处理记录信息
            if ($state.params.processInstanceId) {
                flowSvc.initFlowData(vm);
            }

            //初始化业务信息
            signSvc.initFlowPageData(vm.model.signid, function (data) {
                vm.model = data;
                var deActive = $("#myTab .active");
                var deObj = $("a", deActive);
                vm.model.showDiv = deObj.attr("for-div");
                //发文
                if (vm.model.dispatchDocDto) {
                    vm.showFlag.tabDispatch = true;
                    vm.dispatchDoc = vm.model.dispatchDocDto;
                    assistSvc.findAssistPlanSignById(vm.model.signid, function (data) {
                        vm.assistPlanSign = data;
                    })
                }
                //归档
                if (vm.model.fileRecordDto) {
                    vm.showFlag.tabFilerecord = true;
                    vm.fileRecord = vm.model.fileRecordDto;
                }
                //通过评估部门的个数来控制总投资字段
                if(vm.model.workProgramDtoList && vm.model.workProgramDtoList.length >0) {
                    var orgStr;
                    if (vm.model.workProgramDtoList[0].branchId == 1) {
                        orgStr = vm.model.workProgramDtoList[0].reviewOrgName;
                    } else {
                        orgStr = vm.model.workProgramDtoList[0].mainWorkProgramDto.reviewOrgName;
                    }
                    if (orgStr && orgStr.split(',').length > 1) {
                        vm.showTotalInvestment = true;
                    }
                }

                //初始化专家评分
                if (vm.model.processState > 1) {
                    vm.showFlag.tabWorkProgram = true;        //显示工作方案
                }

                //显示拟补充资料函
                if (vm.model.suppLetterDtoList) {
                    vm.showSupperIndex = 0;
                }
                //拟补充资料信息
                if(vm.model.registerFileDtoDtoList!=undefined){
                    vm.supply=[];//拟补充资料
                    vm.registerFile=[];//审批页面的其他资料
                    vm.drawingFile=[];//图纸资料
                    vm.otherFile=[];//归档的其他资料
                    vm.model.registerFileDtoDtoList.forEach(function(registerFile  , x){
                        if(registerFile.businessType =="3" || registerFile.businessType =="5"
                            ||registerFile.businessType =="6"||registerFile.businessType =="7"){
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
            sysfileSvc.findByMianId(vm.model.signid, function (data) {
                if (data && data.length > 0) {
                    vm.showFlag.tabSysFile = true;
                    vm.sysFileList = data;
                    sysfileSvc.initZtreeClient(vm, $scope);//树形图
                }
            });
        }

        //签收模板打印
        /*vm.printpage = function ($event) {
            var id = $($event.target).attr("id");
            signSvc.workProgramPrint(id);
        }*/

        /**
         * 打印功能 - 分页
         */
        vm.templatePage = function (id) {
            templatePrintSvc.templatePage(id);
        }

        //附件下载
        vm.commonDownloadSysFile = function (sysFileId) {
            sysfileSvc.downloadFile(sysFileId);
        }
        /**
         * 报审登记表导出
         */
        /*vm.printSign = function () {
            var reviewstage = escape(encodeURIComponent(vm.model.reviewstage));
            window.open(rootPath + "/sign/printSign?signId=" + vm.model.signid + "&reviewStage=" + reviewstage);
        }*/

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

        /**
         * 查看专家信息
         * @param expertId
         */
        vm.checkExpertDetail = function(expertId){
            expertSvc.queryExpertDetail(vm , expertId);
        }
    }
})();
