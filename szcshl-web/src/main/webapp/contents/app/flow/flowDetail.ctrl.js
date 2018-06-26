/**
 * Created by ldm on 2017/9/5 0005.
 */
(function () {
    'use strict';

    angular.module('app').controller('flowDetailCtrl', flowDetail);

    flowDetail.$inject = ['$scope','$state','topicSvc','flowSvc','sysfileSvc','bookBuyBusinessSvc','assertStorageBusinessSvc','pauseProjectSvc','archivesLibrarySvc','reviewProjectAppraiseSvc','addSuppLetterSvc','monthlyMultiyearSvc','annountmentSvc'];

    function flowDetail($scope,$state,topicSvc,flowSvc,sysfileSvc,bookBuyBusinessSvc,assertStorageBusinessSvc,pauseProjectSvc,archivesLibrarySvc,reviewProjectAppraiseSvc,addSuppLetterSvc,monthlyMultiyearSvc,annountmentSvc) {
        var vm = this;
        vm.title = '在办任务详情页';
        vm.businessKey = $state.params.businessKey;            // 业务ID
        vm.processKey = $state.params.processKey;              // 流程定义值
        vm.taskId = $state.params.taskId;                      // 任务ID
        vm.instanceId = $state.params.instanceId;              // 流程实例ID

        vm.showFlag={
            businessNext : false,                              //是否显示下一环节处理人tr
            businessTr : false,                                //是否显示业务处理tr
            tabSysFile : false,                                //显示附件
            buttBack : false,                                  //显示回退按钮
        }
        activate();
        function activate() {
            $('#myTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
            })
            //共用方法
            //1、显示流程图
            if(vm.taskId){
                debugger;
                vm.picture = rootPath + "/flow/processInstance/img/"+ vm.instanceId;
            }
            //2、历史处理记录
            flowSvc.historyData(vm);

            //2、各自显示模块
            switch (vm.processKey){
                case flowcommon.getFlowDefinedKey().TOPIC_FLOW:     //课题研究流程
                    //3、查询当前环节信息
                    flowSvc.getFlowInfo(vm.taskId, vm.instanceId, function (data) {
                        vm.flow = data;
                        //有任务ID，说明任务还没办理
                        if(vm.flow.taskId){
                            vm.currentFlow = data;
                            //如果是结束环节，则不显示下一环节信息
                            if (vm.flow.end) {
                                vm.showFlag.nodeNext = false;
                            } else {
                                //初始化环节信息
                                switch (vm.processKey) {
                                    case flowcommon.getFlowDefinedKey().TOPIC_FLOW:
                                        topicSvc.initFlowNode(vm.flow, vm.showFlag, vm);
                                        break;
                                }
                            }
                        }
                    });
                    topicSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().BOOKS_BUY_FLOW:     //图书采购流程
                    bookBuyBusinessSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().ASSERT_STORAGE_FLOW:
                    assertStorageBusinessSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().PROJECT_STOP_FLOW:      //项目暂停流程
                    debugger;
                    pauseProjectSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().FLOW_ARCHIVES:          //档案借阅流程
                    archivesLibrarySvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().FLOW_APPRAISE_REPORT:   //优秀申请报告流程
                    reviewProjectAppraiseSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().FLOW_SUPP_LETTER:       //拟补充资料函流程
                    addSuppLetterSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().MONTHLY_BULLETIN_FLOW:       //月报简报流程
                    monthlyMultiyearSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().ANNOUNT_MENT_FLOW:       //通知公告
                    annountmentSvc.initFlowDeal(vm);
                    vm.parentLink = "#annountment";
                    vm.parentName="通知公告管理";
                    vm.title = "通知公告审批信息";
                    break;
            }
            // 初始化上传附件
            sysfileSvc.findByMianId(vm.businessKey,function(data){
                if(data && data.length > 0){
                    vm.showFlag.tabSysFile = true;
                    vm.sysFileList = data;
                    vm.urlType="flowDetail";//附件右边的列表显示
                    sysfileSvc.initZtreeClient(vm,$scope);//树形图
                }
            });
        }

    }
})();

