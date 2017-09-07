/**
 * Created by Administrator on 2017/9/5 0005.
 */
(function () {
    'use strict';

    angular.module('app').controller('flowDealCtrl', flowDeal);

    flowDeal.$inject = ['ideaSvc','$state','bsWin','topicSvc','flowSvc'];

    function flowDeal(ideaSvc,$state, bsWin,topicSvc,flowSvc) {
        var vm = this;
        vm.title = '待办任务处理';
        vm.businessKey = $state.params.businessKey;            // 业务ID
        vm.processKey = $state.params.processKey;              // 流程定义值
        vm.taskId = $state.params.taskId;                      // 任务ID
        vm.instanceId = $state.params.instanceId;              // 流程实例ID

        vm.showFlag={
            businessNext : false,                              //是否显示下一环节处理人tr
            businessTr : false,                                //是否显示业务处理tr
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
            //2、历史处理记录
            flowSvc.historyData(vm);
            //3、查询当前环节信息
            flowSvc.getFlowInfo(vm.taskId,vm.instanceId,function(data){
                vm.flow = data;
                //如果是结束环节，则不显示下一环节信息
                if (vm.flow.end) {
                    vm.showFlag.nodeNext = false;
                }
            });
            //4、各自显示模块
            switch (vm.processKey){
                case flowcommon.getFlowDefinedKey().TOPIC_FLOW:     //课题研究流程
                    topicSvc.initFlowDeal(vm);
                    break;
            }
        }

        /***************  S_个人意见  ***************/
        vm.ideaEdit = function (options) {
            if(!angular.isObject(options)){
                options = {};
            }
            ideaSvc.initIdeaData(vm,options);
        }
        vm.selectedIdea = function(){
            vm.flow.dealOption = vm.chooseIdea;
        }
        /***************  E_个人意见  ***************/

        /***************  S_流程处理  ***************/
        vm.commitFlow = function () {
            if(vm.flow.isSuspended){
                bsWin.error("该流程目前为暂停状态，不能进行流转操作！");
                return ;
            }
            flowSvc.commit(vm.isCommit,vm.flow,function(data){
                if (data.flag || data.reCode == "ok") {
                    bsWin.success("操作成功！",function(){
                        $state.go('agendaTasks');
                    })
                }else{
                    bsWin.alert(data.reMsg);
                }
            });
        }

        //S_流程回退
        vm.backFlow = function () {
            common.initJqValidation($("#flow_form"));
            var isValid = $("#flow_form").valid();
            if(isValid){
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认回退吗？",
                    onOk: function () {
                        flowSvc.rollBackToLast(vm.flow,vm.isCommit,function(data){
                            vm.isCommit = false;
                            if (data.flag || data.reCode == "ok") {
                                bsWin.alert("回退成功！",function(){
                                    $state.go('agendaTasks');
                                });
                            } else {
                                bsWin.alert(data.reMsg);
                            }
                        }); // 回退到上一个环节
                    }
                });
            }
        }
        /***************  E_流程处理  ***************/
    }
})();

