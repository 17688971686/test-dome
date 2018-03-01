angular.module('signFlowDeal.controller', ['signFlowDeal.service', 'common.service', 'global_variable'])

	.controller('signFlowDealCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','signFlowDealService','Message',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,signFlowDealService,Message) {

	    $scope.signid = $state.params.signid;
		$scope.processInstanceId = $state.params.processInstanceId;
		$scope.taskId = $state.params.taskId;
		
		var parameter={};
		parameter.processInstanceId=$scope.processInstanceId;
		parameter.taskId=$scope.taskId;
			//返回
			$scope.back = function() {
				$state.go('gtasks');
			}
		//流程提交	
		$scope.submitProc=function(){
			//提交时先进行表单检查
			var checkResult = checkBusinessFill($scope);
			if (checkResult.resultTag) {
				signFlowDealService.commit($scope.flow,$rootScope.userInfo.loginName).then(function(response){
					console.log(response);
					if(response.data.flag){
						Message.show(response.data.reMsg);
					}else{
					    Message.show(response.data.reMsg);
					}
   
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});		
         		
         	}else{
         		  Message.show(checkResult.resultMsg);
         	}
			
			
		}
		//检查相应的表单填写
	function checkBusinessFill($scope) {
            if(!$scope.flow.businessMap){
                $scope.flow.businessMap = {};
            }
            //默认通过
            var resultObj = {};
            resultObj.resultTag = true;

            switch ($scope.flow.curNode.activitiId) {
                //综合部拟办
                case  flowcommon.getSignFlowNode().SIGN_ZHB:
                    if ($("#viceDirector").val()) {
                        $scope.flow.businessMap.FGLD = $("#viceDirector").val();
                    } else {
                        resultObj.resultTag = false;
                    }
                    break;
                //分管领导审批，要选择主办部门
                case flowcommon.getSignFlowNode().SIGN_FGLD_FB:
                    resultObj.resultTag = false;
                    $('.seleteTable input[selectType="main"]:checked').each(function () {
                        $scope.flow.businessMap.MAIN_ORG = $(this).val();
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
                            $scope.flow.businessMap.ASSIST_ORG = assistOrgArr.join(',');
                        }
                    }else{
                        resultObj.resultMsg = "主办部门不为空！";
                    }
                    break;
                //部门分办，要选择办理人员
                case flowcommon.getSignFlowNode().SIGN_BMFB1:
                    //如果是协审流程
                    if($scope.model.isassistflow && ($scope.model.isassistflow == 9 || $scope.model.isassistflow == '9')){
                        if(!$scope.businessFlag.principalUsers || $scope.businessFlag.principalUsers.length == 0){
                            resultObj.resultTag = false;
                            resultObj.resultMsg = "请先选择项目负责人！";
                        }else{
                            resultObj.resultTag = false;
                            $.each($scope.businessFlag.principalUsers,function(i,pu){
                                if(pu.isMainUser == '9' || pu.isMainUser == 9){
                                    resultObj.resultTag = true;
                                }
                            })
                            if(!resultObj.resultTag){
                                resultObj.resultMsg = "必须要选择一个第一负责人！";
                            }
                        }
                        if(resultObj.resultTag){
                            $scope.flow.businessMap.PRINCIPAL = $scope.businessFlag.principalUsers;
                        }
                    //如果不是
                    }else{
                        //主办才有第一负责人，协办的全是第二负责人
                        var selUserId = $("#selPrincipalMainUser").val();
                        if (!selUserId) {
                            resultObj.resultTag = false;
                            resultObj.resultMsg = "必须要选择一个第一负责人！";
                            break;
                        }
                        resultObj.resultTag = true;
                        $scope.flow.businessMap.M_USER_ID = selUserId;
                        //判断选择第二负责人
                        var assistIdArr = [];
                        $('#principalAssistUser input[selectType="assistUser"]:checked').each(function () {
                            assistIdArr.push($(this).val());
                        });
                        if (assistIdArr.length > 0) {
                            $scope.flow.businessMap.A_USER_ID = assistIdArr.join(',');
                        }
                    }
                    break;
                case flowcommon.getSignFlowNode().SIGN_BMFB2:
                case flowcommon.getSignFlowNode().SIGN_BMFB3:
                case flowcommon.getSignFlowNode().SIGN_BMFB4:
                    //如果是协审流程
                    if($scope.model.isassistflow && ($scope.model.isassistflow == 9 || $scope.model.isassistflow == '9')){
                        if(!$scope.businessFlag.principalUsers || $scope.businessFlag.principalUsers.length == 0){
                            resultObj.resultTag = false;
                            resultObj.resultMsg = "请先选择项目负责人！";
                        }
                        if(resultObj.resultTag){
                            $scope.flow.businessMap.PRINCIPAL = $scope.businessFlag.principalUsers;
                        }
                    //如果不是
                    }else {
                        var assistIdArr = [];
                        $('#principalAssistUser input[selectType="assistUser"]:checked').each(function () {
                            assistIdArr.push($(this).val());
                        });
                        if (assistIdArr.length > 0) {
                            $scope.flow.businessMap.A_USER_ID = assistIdArr.join(',');
                        }else{
                            resultObj.resultTag = false;
                            resultObj.resultMsg = "必须要选择负责人！";
                        }
                    }
                    break;
                case flowcommon.getSignFlowNode().SIGN_XMFZR1:
                case flowcommon.getSignFlowNode().SIGN_XMFZR2:
                case flowcommon.getSignFlowNode().SIGN_XMFZR3:
                case flowcommon.getSignFlowNode().SIGN_XMFZR4:
                    if($scope.businessFlag.isNeedWP == 9 && $scope.businessFlag.isFinishWP == false){
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "您还没有完成工作方案，不能进行下一步操作！";
                    }
                    $scope.flow.businessMap.IS_NEED_WP = $scope.businessFlag.isNeedWP;
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
                    if($scope.model.processState < 4){
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "您还没完成发文操作，不能进行下一步操作！";
                    }
                    $scope.flow.businessMap.DIS_ID = $scope.dispatchDoc.id;
                    break;
                //项目负责人确认发文
                case flowcommon.getSignFlowNode().SIGN_QRFW:
                case flowcommon.getSignFlowNode().SIGN_BMLD_QRFW_XB:
                case flowcommon.getSignFlowNode().SIGN_FGLD_QRFW_XB:
                    if($scope.businessFlag.passDis == '9' || $scope.businessFlag.passDis == 9){
                        $scope.flow.businessMap.AGREE = '9';
                    }else{
                        $scope.flow.businessMap.AGREE = '0';
                    }
                    $scope.flow.businessMap.DIS_ID = $scope.dispatchDoc.id;
                    break;
                //部长审批发文
                case flowcommon.getSignFlowNode().SIGN_BMLD_QRFW:
                //分管领导审批发文
                case flowcommon.getSignFlowNode().SIGN_FGLD_QRFW:
                //主任审批发文
                case flowcommon.getSignFlowNode().SIGN_ZR_QRFW:
                    $scope.flow.businessMap.DIS_ID = $scope.dispatchDoc.id;
                    break;
                //生成发文编号
                case flowcommon.getSignFlowNode().SIGN_FWBH:
                    $scope.flow.businessMap.DIS_ID = $scope.dispatchDoc.id;
                    break;
                //财务办理
                case flowcommon.getSignFlowNode().SIGN_CWBL:
                    break;
                //归档
                case flowcommon.getSignFlowNode().SIGN_GD:
                    if($scope.model.processState < 7 ){
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "您还没完成归档操作，不能进行下一步操作！";
                    }
                    break;
                //第二负责人确认归档
                case flowcommon.getSignFlowNode().SIGN_DSFZR_QRGD:
                    $scope.flow.businessMap.GD_ID = $scope.fileRecord.fileRecordId;
                    break;
                //最终归档
                case flowcommon.getSignFlowNode().SIGN_QRGD:
                    $scope.flow.businessMap.GD_ID = $scope.fileRecord.fileRecordId;
                    break;
                default:
                    ;
            }

            return resultObj;
        }//E_checkBusinessFill
		
			activate();
		
			function activate(){
				//详细信息
				signFlowDealService.initFlowPageData($scope.signid).then(function(response){
					$scope.sign=response.data
					$scope.fileRecord=$scope.sign.fileRecord;
					$scope.dispatchDoc=$scope.sign.dispatchDocDto;
   
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});
         		
         		//流程信息
         		signFlowDealService.flowNodeInfo(parameter).then(function(response){
         		     $scope.flow=response.data;
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});

			}
	   
	
		}
	])

