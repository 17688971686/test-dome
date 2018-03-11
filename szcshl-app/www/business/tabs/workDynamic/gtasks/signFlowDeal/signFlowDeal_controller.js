angular.module('signFlowDeal.controller', ['signFlowDeal.service', 'common.service', 'global_variable'])

	.controller('signFlowDealCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','signFlowDealService','Message','$ionicTabsDelegate',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,signFlowDealService,Message,$ionicTabsDelegate) {

	    $scope.signid = $state.params.signid;
		$scope.processInstanceId = $state.params.processInstanceId;
		$scope.taskId = $state.params.taskId;
		var parameter={};
		parameter.processInstanceId=$scope.processInstanceId;
		parameter.taskId=$scope.taskId;
		parameter.userid=$rootScope.userInfo.id;
	/*parameter.userid="1c41d130-32b4-4230-9d8a-1277727d3c60";*/
	/*	parameter.userid="9746a99b-7629-472b-a233-cb3cf94a9da1";*/
		
	    $scope.isReveal=true;//提交按钮的显示
			//返回
			$scope.back = function() {
				$state.go('gtasks');
			}
			//切换页面
			$scope.switchPageM = function(index){
				if(index!=0){
                    $scope.isReveal=false;					
				}else{
					$scope.isReveal=true;
				}

				$ionicTabsDelegate.select(index);
			}
		//流程提交	
	$scope.submitProc=function(){
          	//提交时先进行表单检查
			var checkResult = checkBusinessFill($scope);
			if (checkResult.resultTag) {
				signFlowDealService.commit($scope.flow,$rootScope.userInfo.loginName).then(function(response){
					
					if(response.data.flag){
						Message.show(response.data.reMsg,function(){
						$state.go('gtasks');		
			        });
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
                    $('.frame input[selectType="main"]:checked').each(function () {
                        $scope.flow.businessMap.MAIN_ORG = $(this).val();
                        resultObj.resultTag = true;
                    });
                    if(resultObj.resultTag){
                        var assistOrgArr = [];
                        $('.frame input[selectType="assist"]:checked').each(function () {
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
                    if($scope.sign.isassistflow && ($scope.sign.isassistflow == 9 || $scope.sign.isassistflow == '9')){
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
                    if($scope.sign.isassistflow && ($scope.sign.isassistflow == 9 || $scope.sign.isassistflow == '9')){
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
                    if($scope.sign.processState < 4){
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
                    if($scope.sign.processState < 7 ){
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
	 //业务数据初始化
    function initBusinessParams($scope) {
    	 $scope.showFlag={};
    	 $scope.businessFlag={};
    	
            switch ($scope.flow.curNode.activitiId) {
                //项目签收环节
                case flowcommon.getSignFlowNode().SIGN_QS:
                    $scope.showFlag.businessTr = true;
                    $scope.showFlag.nodeSign = true;
                    break;
                //综合部办理
                case flowcommon.getSignFlowNode().SIGN_ZHB:
                    $scope.showFlag.buttBack = true;
                    $scope.showFlag.businessNext = true;
                    $scope.showFlag.nodeSelViceMgr = true;
                    if ($scope.flow.businessMap) {
                        $scope.viceDirectors = $scope.flow.businessMap.viceDirectors;
                    }
                    break;
                //分管领导审批工作方案
                case flowcommon.getSignFlowNode().SIGN_FGLD_FB:
                    $scope.showFlag.buttBack = true;    //可回退
                    $scope.showFlag.businessNext = true;
                    $scope.showFlag.nodeSelOrgs = true;
                    if ($scope.flow.businessMap) {
                        $scope.orgs= $scope.flow.businessMap.orgs;
                    }
                    break;
                //部门分办
                case flowcommon.getSignFlowNode().SIGN_BMFB1:
                    $scope.businessFlag.isMainBranch = true;
                case flowcommon.getSignFlowNode().SIGN_BMFB2:
                case flowcommon.getSignFlowNode().SIGN_BMFB3:
                case flowcommon.getSignFlowNode().SIGN_BMFB4:
                    $scope.showFlag.businessNext = true;
                    $scope.showFlag.nodeSelPrincipal = true;
                    if ($scope.flow.businessMap && $scope.flow.businessMap.users) {
                        $scope.users = $scope.flow.businessMap.users;
                        $scope.users.forEach(function(u,index){
                            u.isSelected = false;
                        });
                    }
                    break;
                //项目负责人办理
                case flowcommon.getSignFlowNode().SIGN_XMFZR1:
                    $scope.businessFlag.isMainBranch = true;
                    $scope.businessFlag.curBranchId = "1";

                case flowcommon.getSignFlowNode().SIGN_XMFZR2:
                    if(!$scope.businessFlag.curBranchId){
                        $scope.businessFlag.curBranchId = "2";
                    }
                case flowcommon.getSignFlowNode().SIGN_XMFZR3:
                    if(!$scope.businessFlag.curBranchId){
                        $scope.businessFlag.curBranchId = "3";
                    }
                case flowcommon.getSignFlowNode().SIGN_XMFZR4:
                    if(!$scope.businessFlag.curBranchId){
                        $scope.businessFlag.curBranchId = "4";          //计算当前分支，主要是为了控制评审方案的编辑
                    }
                    $scope.showFlag.businessTr = true;
                    $scope.showFlag.nodeWorkProgram = true;
                    $scope.businessFlag.isFinishWP = $scope.flow.businessMap.isFinishWP;
                    //已经在做工作方案，则显示
                    if($scope.sign.processState >= 2 && $scope.sign.workProgramDtoList){
                        $.each($scope.sign.workProgramDtoList,function(i,wp){
                            if(wp.branchId ==  $scope.businessFlag.curBranchId){
                                $scope.businessFlag.editEPReviewId = wp.expertReviewId;
                            }
                        })
                        $scope.businessFlag.editExpertSC = true;        //显示专家评分按钮
                        $scope.showFlag.tabWorkProgram = true;
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
                    $scope.showFlag.buttBack = true;
                    $scope.showFlag.tabWorkProgram = true;
                    $("#show_workprogram_a").click();
                    break;
                //发文
                case flowcommon.getSignFlowNode().SIGN_FW:
                    $scope.showFlag.businessTr = true;
                    $scope.showFlag.nodeDispatch = true;
                    $scope.showFlag.isMainPrinUser = true;      //可以进行专家评分
                    $scope.showFlag.expertEdit = true;          //评审费编辑
                    if($scope.flow.businessMap.prilUserList){
                        $scope.businessFlag.principalUsers = $scope.flow.businessMap.prilUserList;
                        $scope.showFlag.businessNext = true;
                    }
                    //已经填报的发文信息，则显示
                    if($scope.sign.processState >= 4){
                        $scope.showFlag.tabDispatch = true;
                        $("#show_dispatch_a").click();
                    }
                    break;
                //项目负责人确认发文
                case flowcommon.getSignFlowNode().SIGN_QRFW:
                case flowcommon.getSignFlowNode().SIGN_BMLD_QRFW_XB:
                case flowcommon.getSignFlowNode().SIGN_FGLD_QRFW_XB:
                    $scope.watchPassDis();
                    $scope.showFlag.buttBack = false;
                    $scope.showFlag.businessTr = true;
                    $scope.showFlag.nodeConfirmDis = true;
                    $scope.businessFlag.passDis = '9';  //默认通过
                    console.log( $scope.businessFlag.passDis);
                    $scope.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                //部长审批发文
                case flowcommon.getSignFlowNode().SIGN_BMLD_QRFW:
                //分管领导审批发文
                case flowcommon.getSignFlowNode().SIGN_FGLD_QRFW:
                //主任审批发文
                case flowcommon.getSignFlowNode().SIGN_ZR_QRFW:
                    $scope.showFlag.buttBack = true;
                    $scope.showFlag.tabWorkProgram = true;
                    $scope.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                //生成发文编号
                case flowcommon.getSignFlowNode().SIGN_FWBH:
                    $scope.showFlag.tabWorkProgram = true;
                    $scope.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    $scope.showFlag.businessTr = true;
                    $scope.showFlag.nodeCreateDisNum = true;
                    $scope.showFlag.isMainPrinUser = true;      //可以进行专家评分
                    break;
                //财务办理
                case flowcommon.getSignFlowNode().SIGN_CWBL:
                    $scope.showFlag.tabWorkProgram = true;
                    $scope.showFlag.tabDispatch = true;
                	$scope.showFlag.businessTr = true;
                	$scope.showFlag.financialCode = true;
                    break;
                //归档
                case flowcommon.getSignFlowNode().SIGN_GD:
                    $scope.showFlag.tabWorkProgram = true;
                    $scope.showFlag.tabDispatch = true;
                    $scope.showFlag.isMainPrinUser = true;      //可以进行专家评分
                    //有第二负责人确认
                    if($scope.flow.businessMap.checkFileUser){
                        $scope.showFlag.businessNext = true;
                    }
                    $scope.showFlag.businessTr = true;
                    $scope.showFlag.nodeFileRecord = true;
                    if($scope.sign.processState > 6){
                        $scope.showFlag.tabFilerecord = true;
                        $("#show_filerecord_a").click();
                    }
                    break;
                //第二负责人确认归档
                case flowcommon.getSignFlowNode().SIGN_DSFZR_QRGD:
                    $scope.showFlag.tabWorkProgram = true;
                    $scope.showFlag.tabDispatch = true;
                    $scope.showFlag.tabFilerecord = true;
                    $("#show_filerecord_a").click();
                    $scope.showFlag.buttBack = true;
                    break;
                //最终归档
                case flowcommon.getSignFlowNode().SIGN_QRGD:
                    $scope.showFlag.tabWorkProgram = true;
                    $scope.showFlag.tabDispatch = true;
                    $scope.showFlag.tabFilerecord = true;
                    $("#show_filerecord_a").click();
                    $scope.showFlag.buttBack = true;
                    $scope.showFlag.nodeNext = false;
                    break;
                default:
                    ;
            }
        }//E_initBusinessParams
			
    // 勾选主办部门判断
    $scope.mainOrg = function ($event) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
            	
                $('.frame input[selectType="main"]').each(
                    function () {
                        var value = $(this).attr("value");
                        if (value != checkboxValue) {
                            $(this).removeAttr("checked");
                            $("#assist_" + value).removeAttr("disabled");
                        } else {
                            $("#assist_" + checkboxValue).removeAttr("checked");
                            $("#assist_" + checkboxValue).attr("disabled", "disabled");
                        }
                    } );

            } else {
                $("#assist_" + checkboxValue).removeAttr("disabled");
            }
           $scope.initOption();
        }
    // 勾选协办部门判断
    $scope.initOption = function () {
            var selOrg = [];
            $('.frame input[selectType="main"]:checked').each(function () {
                selOrg.push($(this).attr("tit"));
            });
            $('.frame input[selectType="assist"]:checked').each(function () {
                selOrg.push($(this).attr("tit"));
            });
     
            if (selOrg.length > 0) {
                $scope.flow.dealOption = "请（" + selOrg.join('，') + "）组织评审";
            }
       
        }
	//检查项目负责人
    $scope.checkPrincipal = function () {
            var selUserId = $("#selPrincipalMainUser").val();
            if (selUserId) {
                $('#principalAssistUser input[selectType="assistUser"]').each(
                    function () {
                        var value = $(this).attr("value");
                        if (value == selUserId) {
                            $(this).removeAttr("checked");
                            $(this).attr("disabled", "disabled");
                        } else {
                            $(this).removeAttr("disabled");
                        }
                    }
                );
            }
            $scope.initUserOption();
        }
    //部门领导分办，选择用户的默认处理意见
    $scope.initUserOption = function () {
            var selUserId = $("#selPrincipalMainUser").val();
            var isSelMainUser = false;
            var defaultOption = "请（"
            if (selUserId) {
                $.each($scope.users, function (i, u) {
                    if (u.id == selUserId) {
                        defaultOption += u.displayName;
                        isSelMainUser = true;
                    }
                })
            }
            var selUser = []
            $('#principalAssistUser input[selectType="assistUser"]:checked').each(function () {
                selUser.push($(this).attr("tit"));
            });

            if (selUser.length > 0) {
                if (isSelMainUser) {
                    defaultOption += ', ';
                }
                defaultOption += selUser.join(', ');
            }
            defaultOption += " )组织办理。";

            $scope.flow.dealOption = defaultOption;
        }		
	//协审部门分办选择负责人
    $scope.addPriUser = function () {
            var isCheck = $("#xs_bmfb input[name='unSelPriUser']:checked");
            if (isCheck.length < 1) {
                Message.show("请选择负责人");
            } else {
                if ($scope.businessFlag.isMainBranch) {
                    if ($scope.businessFlag.isMainPriUser == 9 && isCheck.length > 1) {
                        Message.show("总负责人只能选一个");
                        return;
                    }
                    if ($scope.businessFlag.isSelMainPriUser == false && (angular.isUndefined($scope.businessFlag.isMainPriUser) || $scope.businessFlag.isMainPriUser == 0)) {
                        Message.show("请先选择总负责人");
                        return;
                    }
                    if ($scope.businessFlag.isSelMainPriUser == true && $scope.businessFlag.isMainPriUser == 9) {
                        Message.show("你已经选择了一个总负责人！");
                        return;
                    }
                }
                /* if($scope.businessFlag.principalUsers && ($scope.businessFlag.principalUsers.length + isCheck.length) > 3){
                 Message.show("最多只能选择3个负责人，请重新选择！");
                 return ;
                 }*/

                for (var i = 0; i < isCheck.length; i++) {
                    var priUser = {};
                    priUser.userId = isCheck[i].value;
                    priUser.userType = $("#userType").val();
                    if ($scope.businessFlag.isMainPriUser == 9) {
                        $scope.businessFlag.isSelMainPriUser = true;
                        priUser.isMainUser = 9;
                        $scope.businessFlag.isMainPriUser = 0;
                    } else {
                        priUser.isMainUser = 0;
                    }
                    $scope.users.forEach(function (u, index) {
                        if (u.id == isCheck[i].value) {
                            u.isSelected = true;
                            priUser.userId = u.id;
                            priUser.userName = u.displayName;
                        }
                    });
                   if($scope.businessFlag.principalUsers==undefined){
                   	$scope.businessFlag.principalUsers=[];//初始化
                   }
                    $scope.businessFlag.principalUsers.push(priUser);
                    //初始化处理人
                    $scope.initDealUserName($scope.businessFlag.principalUsers);
                }
            }
        }		
	//初始化处理人  
    $scope.initDealUserName = function (userList) {
            if (userList && userList.length > 0) {
                var defaultOption = "请（";
                angular.forEach(userList, function (u, i) {
                    if (i > 0) {
                        defaultOption += ","
                    }
                    defaultOption += u.userName;
                })
                defaultOption += " )组织办理。";
                $scope.flow.dealOption = defaultOption;
            } else {
                $scope.flow.dealOption = "";
            }
        }	
	//删除负责人
	$scope.delPriUser = function () {
            var isCheck = $("#xs_bmfb input[name='selPriUser']:checked");
            if (isCheck.length < 1) {
                Message.show("请选择取消的负责人");
            } else {
                for (var i = 0; i < isCheck.length; i++) {
                    $scope.users.forEach(function (u, index) {
                        if (u.id == isCheck[i].value) {
                            u.isSelected = false;
                        }
                    });
                    $scope.businessFlag.principalUsers.forEach(function (pu, index) {
                        if (pu.userId == isCheck[i].value) {
                            if (pu.isMainUser == 9) {
                                $scope.businessFlag.isSelMainPriUser = false;
                            }
                            $scope.businessFlag.principalUsers.splice(index, 1);
                        }
                    });
                }
                //初始化处理人
                $scope.initDealUserName($scope.businessFlag.principalUsers);
            }
    }
			
      //监听是否通过
    $scope.watchPassDis = function () {
            //监听是否关联事件
        $scope.$watch("businessFlag.passDis", function (newValue, oldValue) {
            if (newValue == 9) {
                $scope.flow.dealOption = "通过";
            } else {
                    $scope.flow.dealOption = "不通过";
            }

        });
    }
			activate();
		
			function activate(){
				//详细信息
				signFlowDealService.initFlowPageData($scope.signid).then(function(response){
					$scope.sign=response.data
					$scope.fileRecord=$scope.sign.fileRecord;
					$scope.dispatchDoc=$scope.sign.dispatchDocDto;
					$scope.workProgramList = $scope.sign.workProgramDtoList;
   
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});
         		
         		//流程信息
         		signFlowDealService.flowNodeInfo(parameter).then(function(response){
         		     $scope.flow=response.data;
         		     //初始化业务数据
         		     initBusinessParams($scope);
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});

			}
	   
	
		}
	])

