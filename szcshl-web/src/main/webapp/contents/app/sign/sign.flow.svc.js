(function() {
	'use strict';
	
	angular.module('app').factory('signFlowSvc', signFlow);
	
	signFlow.$inject = ['$http','$state',];

	function signFlow($http,$state) {
		var service = {
			startFlow : startFlow,			            //启动流程
			initBusinessParams:initBusinessParams,	    //初始化业务参数
			checkBusinessFill : checkBusinessFill,	    //检查相应的表单填写
			getChargeWorkProgram:getChargeWorkProgram,  //获取工作方案
			getChargeDispatch : getChargeDispatch,		//获取发文
			getChargeFilerecord : getChargeFilerecord,	//获取归档信息
            endSignDetail:endSignDetail,                 //已办结的签收信息
		};
		return service;		

		//S_startFlow
		function startFlow(vm,signid){
			var httpOptions = {
					method : 'post',
					url : rootPath+"/sign/html/startNewFlow",
					params : {signid:signid}
				}
				
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.gridOptions.dataSource.read();
						common.alert({
							vm : vm,
							msg : "操作成功"								
						})
					}
				});
			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}//E_startFlow

		//S_initBusinessParams
		function initBusinessParams(vm){
            switch (vm.flow.curNode.activitiId)
            {
                case "QS":        //项目签收环节
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeSign = true;
                    break ;
                case "ZHB_SP_SW":        //综合部办理
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelViceMgr = true;
                    if (vm.flow.businessMap){
                        vm.viceDirectors = vm.flow.businessMap.viceDirectors;
                    }
                    break;
                case "FGLD_SP_SW":      //分管领导审批工作方案
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelOrgs = true;
                    if (vm.flow.businessMap){
                        vm.orgs = vm.flow.businessMap.orgs;
                    }
                    break;
                case "BM_FB1":          //部门分办
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelPrincipal = true;
                    if(vm.flow.businessMap){
                        vm.users = vm.flow.businessMap.users;
                    }
                    break ;
                case "BM_FB2":          //部门分办
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelPrincipal = true;
                    if(vm.flow.businessMap){
                        vm.users = vm.flow.businessMap.users;
                    }
                    break ;
                case "XMFZR_SP_GZFA1":  //项目负责人承办
                    vm.showFlag.businessTr = true;
                    vm.showFlag.businessDis = true;     //显示是否直接发文
                    vm.showFlag.nodeWorkProgram = true; //显示工作方案和会签准备材料按钮
                    vm.businessFlag.editExpertSC = true;//编辑专家评分和评审费
                    if(vm.model.isreviewCompleted && vm.model.isreviewCompleted == 9){ //如果填报完成，则显示
                        vm.showFlag.tabWorkProgram = true;
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "XMFZR_SP_GZFA2":
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeWorkProgram = true; //显示工作方案和会签准备材料按钮
                    vm.businessFlag.editExpertSC = true;//编辑专家评分和评审费
                    if(vm.model.isreviewACompleted && vm.model.isreviewACompleted == '9'){ //如果填报完成，则显示
                        vm.showFlag.tabWorkProgram = true;
                        $("#show_workprogram_a").click();
                    };
                    break;
                case "BZ_SP_GZAN1":
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabWorkProgram = true;
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "FGLD_SP_GZFA1":
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabWorkProgram = true;
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "BZ_SP_GZAN2":
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabWorkProgram = true;
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "FGLD_SP_GZFA2":
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabWorkProgram = true;
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "FW_SQ":
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeDispatch = true;
                    if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted == 9){
                        vm.showFlag.tabDispatch = true;
                        $("#show_dispatch_a").click();
                    };
                    break;
                case "BZ_SP_FW":
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "FGLD_SP_FW":
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "ZR_SP_FW":
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "MFZR_GD":
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeFileRecord = true;
                    //如果是合并发文次项目，则不用生成发文编号
                    if((vm.dispatchDoc.dispatchWay == 2 && vm.dispatchDoc.isMainProject == 0)
                        || vm.dispatchDoc.fileNum){
                        vm.businessFlag.isCreateDisFileNum = true;
                    }else{
                        vm.showFlag.buttDisFileNum = true;
                    }
                    //有第二负责人，则显示
                    if (vm.flow.businessMap && vm.flow.businessMap.secondUserList){
                        vm.showFlag.businessNext = true;
                        vm.businessFlag.isHaveSePri = true;
                        vm.secondUserList = vm.flow.businessMap.secondUserList;
                    }
                    if(vm.model.filenum){
                        vm.showFlag.tabFilerecord = true;
                        $("#show_filerecord_a").click();
                    }
                    break;
                case "AZFR_SP_GD":
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabFilerecord = true;
                    $("#show_filerecord_a").click();
                    break;
                case "BMLD_QR_GD":
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabFilerecord = true;
                    $("#show_filerecord_a").click();
                    break;
                //以下为协审流程
                case "XS_XMQS":        //项目签收环节
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeSign = true;
                    break ;
                case "XS_ZHBBL":        //综合部拟办
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelViceMgr = true;
                    if (vm.flow.businessMap){
                        vm.viceDirectors = vm.flow.businessMap.viceDirectors;
                    }
                    break;
                case "XS_FGLD_SP":    //分管领导审核
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelXSOrg = true;
                    if (vm.flow.businessMap){
                        vm.xsOrgs = vm.flow.businessMap.xsOrgs;
                    }
                    break;
                case "XS_BMFB":       //部门分办
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelXSPri = true;
                    if (vm.flow.businessMap){
                        vm.xsusers = vm.flow.businessMap.xsusers;
                        //每个人员默认添加一个未选择属性
                        vm.xsusers.forEach(function(u,index){
                            u.isSelected = false;
                        })
                    }
                    break;
                case "XS_XMFZR_GZFA":       //项目负责人承办
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeXSWorkProgram = true;

                    if(vm.model.isreviewCompleted && vm.model.isreviewCompleted == '9' && vm.model.isNeedWrokPrograml == '9'){ //如果填报完成，则显示
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "XS_BZSP_GZFA":             //部长审批工作方案
                    vm.showFlag.buttBack = true;    //可回退
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "XS_FGLDSP_GZFA":           //分管审批工作方案
                    vm.showFlag.buttBack = true;    //可回退
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "XS_FW":                    //发文申请
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeDispatch = true;
                    if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted == 9){
                        vm.showFlag.tabDispatch = true;
                        $("#show_dispatch_a").click();
                    };
                    break;
                case "XS_BZSP_FW":               //部长审批发文
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "XS_FGLDSP_FW":             //分管领导审批发文
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "XS_ZRSP_FW":               //主任审批发文
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "XS_FZR_GD":                //第一负责人归档
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeFileRecord = true;
                    //有第二负责人，则显示
                    if (vm.flow.businessMap && vm.flow.businessMap.secondUserList){
                        vm.showFlag.businessNext = true;
                        vm.businessFlag.isHaveSePri = true;
                        vm.secondUserList = vm.flow.businessMap.secondUserList;
                    }
                    if(vm.model.filenum){
                        vm.showFlag.tabFilerecord = true;
                        $("#show_filerecord_a").click();
                    }
                    break;
                case "XS_FZR_SP":                //第二负责人审批归档
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabFilerecord = true;
                    $("#show_filerecord_a").click();
                    break;
                case "XS_QRGD":                  //确认归档
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.tabFilerecord = true;
                    $("#show_filerecord_a").click();
                    break;
                default:
                    ;
            }
		}//E_initBusinessParams
		
		//S_checkBusinessFill
		function checkBusinessFill(vm){
			vm.flow.businessMap = {};
			var seleteCount = 0;
			var resultTag = true;
			switch (vm.flow.curNode.activitiId){
                case "ZHB_SP_SW":       //综合部拟办
                    if($("#viceDirector").val()){
                        vm.flow.businessMap.FGLD = $("#viceDirector").val();
                    }else{
                        resultTag = false;
                    }
                    break;
                case "FGLD_SP_SW":      //分管领导审批，要选择主办部门
                    $('.seleteTable input[selectType="main"]:checked').each(function(){
                        vm.flow.businessMap.hostdept = $(this).val();
                        seleteCount++;
                    });
                    if(seleteCount == 0){
                        resultTag = false;
                        break;
                    }
                    $('.seleteTable input[selectType="assist"]:checked').each(function(){
                        vm.flow.businessMap.assistdept = $(this).val();
                    });
                    break;
                case "BM_FB1":      //部门分办，要选择主办部门
                    var selUserId = $("#selPrincipalMainUser").val();
                    if(!selUserId){
                        resultTag = false;
                        break;
                    }
                    vm.flow.businessMap.M_USER_ID = selUserId;
                    //以下为第二负责人
                    var assistIdArr = [];
                    $('#principalAssistUser input[selectType="assistUser"]:checked').each(function(){
                        assistIdArr.push($(this).val());
                    });
                    if(assistIdArr.length > 0){
                        vm.flow.businessMap.A_USER_ID = assistIdArr.join(',');
                    }
                    break;
                case "BM_FB2":      //部门分办，要选择主办部门
                    var selUserId = $("#selPrincipalMainUser").val();
                    if(!selUserId){
                        resultTag = false;
                        break;
                    }
                    vm.flow.businessMap.M_USER_ID = selUserId;
                    //以下为第二负责人
                    var assistIdArr = [];
                    $('#principalAssistUser input[selectType="assistUser"]:checked').each(function(){
                        assistIdArr.push($(this).val());
                    });
                    if(assistIdArr.length > 0){
                        vm.flow.businessMap.A_USER_ID = assistIdArr.join(',');
                    }
                    break;
                case "XMFZR_SP_GZFA1":
                    //直接发文
                    if(vm.businessFlag.isGotoDis){
                        vm.flow.businessMap.ZJFW = '9';
                    //否则要填写工作方案才能提交
                    }else{
                        if(vm.model.isreviewCompleted && vm.model.isreviewCompleted == 9){
                            resultTag = true;
                        }else{
                            resultTag = false;
                        }
                    }
                    break;
                case "XMFZR_SP_GZFA2":
                     if (vm.model.isreviewACompleted && vm.model.isreviewACompleted == 9) {
                          resultTag = true;
                     } else {
                          resultTag = false;
                     }
                    break;
                case "BZ_SP_GZAN1":
                    	vm.flow.businessMap.M_WP_ID = vm.mainwork.id;
                    break;
                case "BZ_SP_GZAN2":
	                    vm.flow.businessMap.A_WP_ID = vm.assistwork.id;
                    break;
                case "FGLD_SP_GZFA1":
                    vm.flow.businessMap.M_WP_ID = vm.mainwork.id;
                    break;
                case "FGLD_SP_GZFA2":
                    vm.flow.businessMap.A_WP_ID = vm.assistwork.id;
                    break;
                case "FW_SQ":
                    if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted == 9){
                        resultTag = true;
                    }else{
                        resultTag = false;
                    }
                    break;
                case "BZ_SP_FW":
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "FGLD_SP_FW":
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "ZR_SP_FW":
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "MFZR_GD":
                    //没有归档
                    if(vm.businessFlag.isCreateDisFileNum == false){
                        resultTag = false;
                    }else{
                        if(vm.model.filenum){
                            resultTag = true;
                            if(vm.businessFlag.isHaveSePri){
                                if($("#secondPriUser").val()){
                                    vm.flow.businessMap.SEPRI_ID = $("#secondPriUser").val();
                                    resultTag = true;
                                }else{
                                    resultTag = false;
                                }
                            }
                        }else{
                            resultTag = false;
                        }
                    }
                    break;
                 //以下是协审流程环节
                case "XS_ZHBBL":            //综合部办理
                    if($("#viceDirector").val()){
                        vm.flow.businessMap.FGLD = $("#viceDirector").val();
                    }else{
                        resultTag = false;
                    }
                    break;
                case "XS_FGLD_SP":          //分管领导审批
                    $('#xs_table input[selectType="xs_main"]:checked').each(function(){
                        vm.flow.businessMap.deptid = $(this).val();
                    });
                    if(!vm.flow.businessMap.deptid){
                        resultTag = false;
                    }
                    break;
                case "XS_BMFB":          //部门分办
                    if(vm.businessFlag.principalUsers.length == 0){
                        resultTag = false;
                    }else{
                        var mainPriUserCount = 0;
                        vm.businessFlag.principalUsers.forEach(function(pu,index){
                            if(pu.isMainUser == 9){
                                mainPriUserCount ++;
                            }
                        });
                        //选择的总负责人不对
                        if(mainPriUserCount == 0 || mainPriUserCount > 1){
                            resultTag = false;
                        }else{
                            vm.flow.businessMap.PRINCIPAL = vm.businessFlag.principalUsers;
                        }
                    }

                    break;
                case "XS_XMFZR_GZFA":           //项目负责人承办
                    vm.flow.businessMap.PSFA = vm.model.isNeedWrokPrograml;
                    //选择了工作方案没填写的，不给通过
                    if(vm.model.isNeedWrokPrograml && vm.model.isNeedWrokPrograml == '9' && (!vm.mainwork || !vm.mainwork.id)) {
                        resultTag = false;
                    }
                    break;
                case "XS_BZSP_GZFA":             //部长审批工作方案
                    vm.flow.businessMap.WP_ID = vm.mainwork.id;
                    break;
                case "XS_FGLDSP_GZFA":           //分管领导审批
                    vm.flow.businessMap.WP_ID = vm.mainwork.id;
                    break;
                case "XS_FW":                    //发文申请
                    if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted==9){
                        resultTag = true;
                    }else{
                        resultTag = false;
                    }
                    break;
                case "XS_BZSP_FW":               //部长审批发文
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "XS_FGLDSP_FW":             //分管领导审批发文
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "XS_ZRSP_FW":               //主任审批发文
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "XS_FZR_GD":                //第一负责人归档
                    if(vm.model.filenum){
                        resultTag = true;
                        if(vm.businessFlag.isHaveSePri){
                            if($("#secondPriUser").val()){
                                vm.flow.businessMap.SEPRI_ID = $("#secondPriUser").val();
                                resultTag = true;
                            }else{
                                resultTag = false;
                            }
                        }
                    }else{
                        resultTag = false;
                    }
                    break;
                case "XS_FZR_SP":                //第二负责人审批归档

                    break;
                case "XS_QRGD":                  //确认归档

                    break;
                default:
                    ;
            }

            return resultTag;
		}//E_checkBusinessFill
		
		//S_getChargeWorkProgram
		function getChargeWorkProgram(vm,isMain){
			var mainState = isMain == true?"9":"0";
			var httpOptions = {
					method : 'get',
					url : rootPath+"/workprogram/html/initWorkBySignId",
					params : {signId:vm.model.signid,isMain:mainState}
				}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {								
						vm.work = response.data;	
						$("#show_workprogram_a").click();
					}						
				});
			}
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});		
		}//E_getChargeWorkProgram
		
		//S_getChargeDispatch
		function getChargeDispatch(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/dispatch/html/initDispatchBySignId",
					params : {signId:vm.model.signid}
				}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {								
						vm.dispatchDoc = response.data;	
						$("#show_dispatch_a").click();
					}						
				});
			}
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});		
		}//E_getChargeDispatch
		
		//S_getChargeFilerecord
		function getChargeFilerecord(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/fileRecord/html/initBySignId",
					params : {signId:vm.model.signid}
				}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {								
						vm.fileRecord = response.data;	
						$("#show_filerecord_a").click();
					}						
				});
			}
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});		
		}//E_getChargeFilerecord

        //S_endSignDetail
        function endSignDetail(vm){
            var httpOptions = {
                method : 'get',
                url : rootPath+"/sign/html/initDetailPageData",
                params : {signid:vm.model.signid,queryAll:true}
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        vm.model = response.data;
                        if(vm.model.workProgramDtoList && vm.model.workProgramDtoList.length > 0){
                            vm.show_workprogram = true;
                            vm.model.workProgramDtoList.forEach(function(w,index){
                            	if(w.isMain == 9){
                                    vm.showMainwork = true;
                                    vm.mainwork = {};
                                    vm.mainwork = w;
								}else if(w.isMain == 0){
                                    vm.showAssistwork = true;
                                    vm.assistwork = {};
                                    vm.assistwork = w;
								}
							});
                        }
                        if(vm.model.dispatchDocDto){
                            vm.show_dispatch = true;
                            vm.dispatchDoc = vm.model.dispatchDocDto;
                        }
                        if(vm.model.fileRecordDto){
                            vm.show_filerecord = true;
                            vm.fileRecord = vm.model.fileRecordDto;
                        }
                    }
                })
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess
            });
        }//E_endSignDetail
        
	}//E_signFlow		
})();