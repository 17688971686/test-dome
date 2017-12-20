(function () {
    'use strict';

    angular.module('app').controller('signFlowDetailCtrl', sign);

    sign.$inject = ['sysfileSvc','signSvc','$state','flowSvc','signFlowSvc','$scope','templatePrintSvc'];

    function sign(sysfileSvc,signSvc,$state,flowSvc,signFlowSvc,$scope,templatePrintSvc) {
        var vm = this;
        vm.title = "项目流程信息";
        vm.model = {};
        vm.flow = {};					
        vm.work = {};
        vm.dispatchDoc = {};
        vm.fileRecord = {};
        
        vm.model.signid = $state.params.signid;	
        vm.flow.taskId = $state.params.taskId;			//流程任务ID
        vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID

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
            expertReviews : [],         // 专家评审方案
        }
        vm.expertList =  new Array(10); //用于打印页面的专家列表，控制行数
        //用于打印发文，项目概况控制
        vm.workProgramXmjys ={};//项目建议书
        vm.workProgramKxxyj = {};//可行性研究
        vm.workProgramXmgs = {};//项目概算
        vm.workProgramTg = {}; //调概
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

            //初始化流程信息
            flowSvc.initFlowData(vm);
            // 初始化业务信息
            signSvc.initFlowPageData(vm.model.signid,function(data){
                vm.model = data;
                var deActive = $("#myTab .active");
                var deObj = $("a", deActive);
                vm.model.showDiv = deObj.attr("for-div");
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

                //判断是否有多个分支，用于控制是否显示总投资字段 和 分开获取关联的项目信息（主要用于项目概算阶段）
                if(vm.model.workProgramDtoList && vm.model.workProgramDtoList.length >0){
                    vm.showTotalInvestment = true;
                    for( var i=0 ; i< vm.model.workProgramDtoList.length ; i++ ){
                        var reviewStage = vm.model.workProgramDtoList[i].reviewstage;
                        if(reviewStage && reviewStage == '项目建议书'){
                            vm.workProgramXmjys =vm.model.workProgramDtoList[i];
                        }
                        if(reviewStage && reviewStage == '可行性研究报告'){
                            vm.workProgramKxxyj = vm.model.workProgramDtoList[i];
                        }
                        if(reviewStage && reviewStage == '项目概算' &&
                            (!vm.model.ischangeEstimate || vm.model.ischangeEstimate != 9 || vm.model.ischangeEstimate != '9')){
                            vm.workProgramXmgs =vm.model.workProgramDtoList[i];
                        }
                        if(reviewStage && reviewStage == '项目概算' &&
                            vm.model.ischangeEstimate && (vm.model.ischangeEstimate == 9 || vm.model.ischangeEstimate == '9')){
                            vm.workProgramTg =vm.model.workProgramDtoList[i];
                        }
                    }
                }

                //初始化专家评分
                if (vm.model.processState > 1) {
                    vm.showFlag.tabWorkProgram=true;        //显示工作方案
                }
                //显示拟补充资料函
                if(vm.model.suppLetterDtoList){
                    vm.showSupperIndex = 0;
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

        //签收模板打印
        vm.printpage = function ($event) {
            var id =  $($event.target).attr("id");
            signSvc.workProgramPrint(id);
        }
        /**
         * 打印功能 -分页
         */
        vm.templatePage = function(id){
            templatePrintSvc.templatePage(id);
        }

    }
})();
