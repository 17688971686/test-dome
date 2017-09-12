/**
 * Created by ldm on 2017/9/5 0005.
 */
(function () {
    'use strict';

    angular.module('app').controller('flowDetailCtrl', flowDetail);

    flowDetail.$inject = ['$state','topicSvc','flowSvc','sysfileSvc'];

    function flowDetail($state,topicSvc,flowSvc,sysfileSvc) {
        var vm = this;
        vm.title = '在办任务详情页';
        vm.businessKey = $state.params.businessKey;            // 业务ID
        vm.processKey = $state.params.processKey;              // 流程定义值
        vm.taskId = $state.params.taskId;                      // 任务ID
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
            //1、显示流程图
            vm.picture = rootPath + "/flow/processInstance/img/"+ vm.instanceId;
            //1、历史处理记录
            flowSvc.historyData(vm);

            //2、各自显示模块
            switch (vm.processKey){
                case flowcommon.getFlowDefinedKey().TOPIC_FLOW:     //课题研究流程
                    topicSvc.initFlowDeal(vm);
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

