/**
 * Created by ldm on 2017/9/5 0005.
 */
(function () {
    'use strict';

    angular.module('app').controller('flowEndCtrl', flowEnd);

    flowEnd.$inject = ['$state','topicSvc','flowSvc','sysfileSvc','bookBuyBusinessSvc','assertStorageBusinessSvc','pauseProjectSvc','archivesLibrarySvc','reviewProjectAppraiseSvc'];

    function flowEnd($state,topicSvc,flowSvc,sysfileSvc,bookBuyBusinessSvc,assertStorageBusinessSvc,pauseProjectSvc,archivesLibrarySvc,reviewProjectAppraiseSvc) {
        var vm = this;
        vm.title = '待办任务处理';
        vm.businessKey = $state.params.businessKey;            // 业务ID
        vm.processKey = $state.params.processKey;              // 流程定义值
        vm.instanceId = $state.params.instanceId;              // 流程实例ID

        vm.showFlag={
            businessNext : false,                              //是否显示下一环节处理人tr
            businessTr : false,                                //是否显示业务处理tr
            showUploadFile : false,                            //显示附件
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
            //1、历史处理记录
            flowSvc.historyData(vm);

            //2、各自显示模块
            switch (vm.processKey){
                case flowcommon.getFlowDefinedKey().TOPIC_FLOW:     //课题研究流程
                    topicSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().BOOKS_BUY_FLOW:
                    bookBuyBusinessSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().ASSERT_STORAGE_FLOW:
                    assertStorageBusinessSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().PROJECT_STOP_FLOW:      //项目暂停流程
                    pauseProjectSvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().FLOW_ARCHIVES:          //档案借阅流程
                    archivesLibrarySvc.initFlowDeal(vm);
                    break;
                case flowcommon.getFlowDefinedKey().FLOW_APPRAISE_REPORT:   //优秀申请报告流程
                    reviewProjectAppraiseSvc.initFlowDeal(vm);
                    break;
            }
            //5、附件
            sysfileSvc.findByBusinessId(vm.businessKey,function(data){
                if(data && data.length > 0){
                    vm.sysFilelists = data;
                    vm.showFlag.showUploadFile = true;
                }
            })
        }

    }
})();

