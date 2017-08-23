(function () {
    'use strict';

    angular.module('app').factory('signFlowSvc', signFlow);

    signFlow.$inject = ['$http', '$state', 'bsWin'];

    function signFlow($http, $state, bsWin) {
        var service = {
            startFlow: startFlow,			            //启动流程
            initBusinessParams: initBusinessParams,	    //初始化业务参数
            checkBusinessFill: checkBusinessFill,	    //检查相应的表单填写
            getChargeDispatch: getChargeDispatch,		//获取发文
            getChargeFilerecord: getChargeFilerecord,	//获取归档信息
            endSignDetail: endSignDetail,                 //已办结的签收信息
        };
        return service;

        //S_startFlow
        function startFlow(signid,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/startNewFlow",
                params: {
                    signid: signid
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_startFlow

        //S_initBusinessParams
        function initBusinessParams(vm) {
            switch (vm.flow.curNode.activitiId) {
                //项目签收环节
                case flowcommon.getSignFlowNode().SIGN_QS:
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeSign = true;
                    break;
                //综合部办理
                case flowcommon.getSignFlowNode().SIGN_ZHB:
                    vm.showFlag.buttBack = true;
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelViceMgr = true;
                    if (vm.flow.businessMap) {
                        vm.viceDirectors = vm.flow.businessMap.viceDirectors;
                    }
                    break;
                //分管领导审批工作方案
                case flowcommon.getSignFlowNode().SIGN_FGLD_FB:
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.businessNext = true;
                   
                    vm.showFlag.nodeSelOrgs = true;
                    if (vm.flow.businessMap) {
                        vm.orgs= vm.flow.businessMap.orgs;
                    }
                    break;
                //部门分办
                case flowcommon.getSignFlowNode().SIGN_BMFB1:
                    vm.businessFlag.isMainBranch = true;
                case flowcommon.getSignFlowNode().SIGN_BMFB2:
                case flowcommon.getSignFlowNode().SIGN_BMFB3:
                case flowcommon.getSignFlowNode().SIGN_BMFB4:
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelPrincipal = true;
                    if (vm.flow.businessMap) {
                        vm.users = vm.flow.businessMap.users;
                    }
                    break;
                //项目负责人办理
                case flowcommon.getSignFlowNode().SIGN_XMFZR1:
                    vm.businessFlag.isMainBranch = true;
                    vm.businessFlag.curBranchId = "1";
                case flowcommon.getSignFlowNode().SIGN_XMFZR2:
                    if(!vm.businessFlag.curBranchId){
                        vm.businessFlag.curBranchId = "2";
                    }
                case flowcommon.getSignFlowNode().SIGN_XMFZR3:
                    if(!vm.businessFlag.curBranchId){
                        vm.businessFlag.curBranchId = "3";
                    }
                case flowcommon.getSignFlowNode().SIGN_XMFZR4:
                    if(!vm.businessFlag.curBranchId){
                        vm.businessFlag.curBranchId = "4";          //计算当前分支，主要是为了控制评审方案的编辑
                    }
                    $.each(vm.model.workProgramDtoList,function(i,wp){
                        if(wp.branchId ==  vm.businessFlag.curBranchId){
                            vm.businessFlag.editEPReviewId = wp.expertReviewId;
                        }
                    })
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeWorkProgram = true;
                    vm.businessFlag.isFinishWP = vm.flow.businessMap.isFinishWP;
                    //已经在做工作方案，则显示
                    if(vm.model.processState >= 2){
                        vm.businessFlag.editExpertSC = true;        //显示专家评分按钮
                        vm.showFlag.tabWorkProgram = true;
                        $("#show_workprogram_a").click();
                    }
                    break;
                //部长审核工作方案
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW1:
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW2:
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW3:
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW4:
                //分管领导审批工作方案
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW1:
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW2:
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW3:
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW4:
                    vm.showFlag.buttBack = true;
                    vm.showFlag.tabWorkProgram = true;
                    $("#show_workprogram_a").click();
                    break;
                //发文
                case flowcommon.getSignFlowNode().SIGN_FW:
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeDispatch = true;
                    if(vm.flow.businessMap.prilUserList){
                        vm.businessFlag.principalUsers = vm.flow.businessMap.prilUserList;
                        vm.showFlag.businessNext = true;
                    }
                    //已经填报的发文信息，则显示
                    if(vm.model.processState >= 4){
                        vm.showFlag.tabDispatch = true;
                        $("#show_dispatch_a").click();
                    }
                    break;
                //项目负责人确认发文
                case flowcommon.getSignFlowNode().SIGN_QRFW:
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeConfirmDis = true;
                    vm.businessFlag.passDis = '9';  //默认通过
                    vm.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                //部长审批发文
                case flowcommon.getSignFlowNode().SIGN_BMLD_QRFW:
                //分管领导审批发文
                case flowcommon.getSignFlowNode().SIGN_FGLD_QRFW:
                //主任审批发文
                case flowcommon.getSignFlowNode().SIGN_ZR_QRFW:
                    vm.showFlag.buttBack = true;
                    vm.showFlag.tabWorkProgram = true;
                    vm.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                //生成发文编号
                case flowcommon.getSignFlowNode().SIGN_FWBH:
                    vm.showFlag.tabWorkProgram = true;
                    vm.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeCreateDisNum = true;
                    break;
                //财务办理
                case flowcommon.getSignFlowNode().SIGN_CWBL:
                    vm.showFlag.tabWorkProgram = true;
                    vm.showFlag.tabDispatch = true;
                	vm.showFlag.businessTr = true;
                	vm.showFlag.financialCode = true;
                    break;
                //归档
                case flowcommon.getSignFlowNode().SIGN_GD:
                    vm.showFlag.tabWorkProgram = true;
                    vm.showFlag.tabDispatch = true;
                    //有第二负责人确认
                    if(vm.flow.businessMap.checkFileUser){
                        vm.showFlag.businessNext = true;
                    }
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeFileRecord = true;
                    if(vm.model.processState > 6){
                        vm.showFlag.tabFilerecord = true;
                        $("#show_filerecord_a").click();
                    }
                    break;
                //第二负责人确认归档
                case flowcommon.getSignFlowNode().SIGN_DSFZR_QRGD:
                    vm.showFlag.tabWorkProgram = true;
                    vm.showFlag.tabDispatch = true;
                    vm.showFlag.tabFilerecord = true;
                    $("#show_filerecord_a").click();
                    vm.showFlag.buttBack = true;
                    break;
                //最终归档
                case flowcommon.getSignFlowNode().SIGN_QRGD:
                    vm.showFlag.tabWorkProgram = true;
                    vm.showFlag.tabDispatch = true;
                    vm.showFlag.tabFilerecord = true;
                    $("#show_filerecord_a").click();
                    vm.showFlag.buttBack = true;
                    vm.showFlag.nodeNext = false;
                    break;
                default:
                    ;
            }
        }//E_initBusinessParams

        //S_checkBusinessFill
        function checkBusinessFill(vm) {
            if(!vm.flow.businessMap){
                vm.flow.businessMap = {};
            }

            //默认通过
            var resultObj = {};
            resultObj.resultTag = true;

            switch (vm.flow.curNode.activitiId) {
                //综合部拟办
                case  flowcommon.getSignFlowNode().SIGN_ZHB:
                    if ($("#viceDirector").val()) {
                        vm.flow.businessMap.FGLD = $("#viceDirector").val();
                    } else {
                        resultObj.resultTag = false;
                    }
                    break;
                //分管领导审批，要选择主办部门
                case flowcommon.getSignFlowNode().SIGN_FGLD_FB:
                    resultObj.resultTag = false;
                    $('.seleteTable input[selectType="main"]:checked').each(function () {
                        vm.flow.businessMap.MAIN_ORG = $(this).val();
                        resultObj.resultTag = true;
                    });
                    if(resultObj.resultTag){
                        var assistOrgArr = [];
                        $('.seleteTable input[selectType="assist"]:checked').each(function () {
                            assistOrgArr.push($(this).val())
                        });
                        if(assistOrgArr.length > 3){
                            resultObj.resultTag = false;
                            resultObj.resultMsg = "协办部门最多只能选择3个！";
                        }else{
                            vm.flow.businessMap.ASSIST_ORG = assistOrgArr.join(',');
                        }
                    }else{
                        resultObj.resultMsg = "主办部门不为空！";
                    }
                    break;
                //部门分办，要选择办理人员
                case flowcommon.getSignFlowNode().SIGN_BMFB1:
                    //主办才有第一负责人，协办的全是第二负责人
                    var selUserId = $("#selPrincipalMainUser").val();
                    if (!selUserId) {
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "必须要选择一个第一负责人！";
                        break;
                    }
                    resultObj.resultTag = true;
                    vm.flow.businessMap.M_USER_ID = selUserId;
                    //判断选择第二负责人
                    var assistIdArr = [];
                    $('#principalAssistUser input[selectType="assistUser"]:checked').each(function () {
                        assistIdArr.push($(this).val());
                    });
                    if (assistIdArr.length > 0) {
                        vm.flow.businessMap.A_USER_ID = assistIdArr.join(',');
                    }
                    break;
                case flowcommon.getSignFlowNode().SIGN_BMFB2:
                case flowcommon.getSignFlowNode().SIGN_BMFB3:
                case flowcommon.getSignFlowNode().SIGN_BMFB4:
                    var assistIdArr = [];
                    $('#principalAssistUser input[selectType="assistUser"]:checked').each(function () {
                        assistIdArr.push($(this).val());
                    });
                    if (assistIdArr.length > 0) {
                        vm.flow.businessMap.A_USER_ID = assistIdArr.join(',');
                    }else{
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "必须要选择负责人！";
                    }
                    break;
                case flowcommon.getSignFlowNode().SIGN_XMFZR1:
                case flowcommon.getSignFlowNode().SIGN_XMFZR2:
                case flowcommon.getSignFlowNode().SIGN_XMFZR3:
                case flowcommon.getSignFlowNode().SIGN_XMFZR4:
                    if(vm.businessFlag.isNeedWP == 9 && vm.businessFlag.isFinishWP == false){
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "您还没有完成工作方案，不能进行下一步操作！";
                    }
                    vm.flow.businessMap.IS_NEED_WP = vm.businessFlag.isNeedWP;
                    break;
                //部长审核工作方案
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW1:
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW2:
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW3:
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW4:
                   break;
                //分管领导审核工作方案
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW1:
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW2:
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW3:
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW4:
                    break;
                //发文申请
                case flowcommon.getSignFlowNode().SIGN_FW:
                    if(vm.model.processState < 4){
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "您还没完成发文操作，不能进行下一步操作！";
                    }
                    break;
                //项目负责人确认发文
                case flowcommon.getSignFlowNode().SIGN_QRFW:
                    if(vm.businessFlag.passDis == '9' || vm.businessFlag.passDis == 9){
                        vm.flow.businessMap.AGREE = '9';
                    }else{
                        vm.flow.businessMap.AGREE = '0';
                    }
                    break;
                //部长审批发文
                case flowcommon.getSignFlowNode().SIGN_BMLD_QRFW:
                //分管领导审批发文
                case flowcommon.getSignFlowNode().SIGN_FGLD_QRFW:
                //主任审批发文
                case flowcommon.getSignFlowNode().SIGN_ZR_QRFW:
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id;
                    break;
                //生成发文编号
                case flowcommon.getSignFlowNode().SIGN_FWBH:
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id;
                    break;
                //财务办理
                case flowcommon.getSignFlowNode().SIGN_CWBL:
                    break;
                //归档
                case flowcommon.getSignFlowNode().SIGN_GD:
                    if(vm.model.processState < 7 ){
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "您还没完成归档操作，不能进行下一步操作！";
                    }
                    break;
                //第二负责人确认归档
                case flowcommon.getSignFlowNode().SIGN_DSFZR_QRGD:
                    break;
                //最终归档
                case flowcommon.getSignFlowNode().SIGN_QRGD:
                    vm.flow.businessMap.GD_ID = vm.fileRecord.fileRecordId;
                    break;
                default:
                    ;
            }

            return resultObj;
        }//E_checkBusinessFill

        //S_getChargeDispatch
        function getChargeDispatch(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/dispatch/html/initDispatchBySignId",
                params: {signId: vm.model.signid}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.dispatchDoc = response.data;
                        $("#show_dispatch_a").click();
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_getChargeDispatch

        //S_getChargeFilerecord
        function getChargeFilerecord(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/fileRecord/html/initBySignId",
                params: {signId: vm.model.signid}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.fileRecord = response.data;
                        $("#show_filerecord_a").click();
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_getChargeFilerecord

        //S_endSignDetail
        function endSignDetail(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/html/initDetailPageData",
                params: {signid: vm.model.signid, queryAll: true}
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.model = response.data;
                        if (vm.model.workProgramDtoList && vm.model.workProgramDtoList.length > 0) {
                            vm.show_workprogram = true;
                            vm.model.workProgramDtoList.forEach(function (w, index) {
                                if (w.isMain == 9) {
                                    vm.showMainwork = true;
                                    vm.mainwork = {};
                                    vm.mainwork = w;
                                } else if (w.isMain == 0) {
                                    vm.showAssistwork = true;
                                    vm.assistwork = {};
                                    vm.assistwork = w;
                                }
                            });
                        }
                        if (vm.model.dispatchDocDto) {
                            vm.show_dispatch = true;
                            vm.dispatchDoc = vm.model.dispatchDocDto;
                        }
                        if (vm.model.fileRecordDto) {
                            vm.show_filerecord = true;
                            vm.fileRecord = vm.model.fileRecordDto;
                        }
                    }
                })
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_endSignDetail

    }//E_signFlow
})();