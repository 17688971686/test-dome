(function() {
	'use strict';
	
	angular.module('app').factory('signFlowSvc', signFlow);
	
	signFlow.$inject = ['$http','$state'];

	function signFlow($http,$state) {
		var service = {
			startFlow : startFlow,			//启动流程
			initBusinessParams:initBusinessParams,	//初始化业务参数
			checkBusinessFill : checkBusinessFill,	//检查相应的表单填写
			getChargeWorkProgram:getChargeWorkProgram,//获取工作方案
			getChargeDispatch : getChargeDispatch,		//获取发文	（停用）
			getChargeFilerecord : getChargeFilerecord,	//获取归档信息（停用）
            endSignDetail:endSignDetail,                 //已办结的签收信息（停用）
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
                case "XMFZR_SP_GZFA1":  //项目负责人承办
                    vm.businessTr = true;
                    vm.XMFZR_SP_GZFA = true;
                    vm.MarkAndPay=false;//专家评分费用编辑权限
                    if(vm.model.isreviewCompleted && vm.model.isreviewCompleted == 9){ //如果填报完成，则显示
                        vm.show_workprogram = true;
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "XMFZR_SP_GZFA2":
                    vm.businessTr = true;
                    vm.XMFZR_SP_GZFA = true;
                    vm.MarkAndPay=false;//专家评分费用编辑权限
                    if(vm.model.isreviewACompleted && vm.model.isreviewACompleted == '9' && vm.model.isNeedWrokPrograml == '9'){ //如果填报完成，则显示
                        vm.show_workprogram = true;
                        $("#show_workprogram_a").click();
                    };
                    break;
                case "BZ_SP_GZAN1":
                    vm.show_workprogram = true;
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "FGLD_SP_GZFA1":
                    vm.show_workprogram = true;
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "BZ_SP_GZAN2":
                    vm.show_workprogram = true;
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "FGLD_SP_GZFA2":
                    vm.show_workprogram = true;
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "FW_SQ":
                    vm.businessTr = true;
                    vm.FW_SQ = true;
                    if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted == 0){
                        vm.show_dispatch = true;
                        $("#show_dispatch_a").click();
                    };
                    break;
                case "BZ_SP_FW":
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "FGLD_SP_FW":
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "ZR_SP_FW":
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "MFZR_GD":
                    vm.businessTr = true;
                    vm.MFZR_GD = true;
                    if(vm.model.filenum){
                        vm.show_filerecord = true;
                        $("#show_filerecord_a").click();
                    }
                    break;
                case "AZFR_SP_GD":
                    vm.show_filerecord = true;
                    $("#show_filerecord_a").click();
                    break;
                case "BMLD_QR_GD":
                    vm.show_filerecord = true;
                    $("#show_filerecord_a").click();
                    break;
                //以下为协审流程
                case "XS_XMFZR_GZFA":       //项目负责人承办
                    vm.businessTr = true;
                    vm.XS_XMFZR_GZFA = true;
                    if(vm.model.isreviewCompleted && vm.model.isreviewCompleted == '9' && vm.model.isNeedWrokPrograml == '9'){ //如果填报完成，则显示
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "XS_BZSP_GZFA":             //部长审批工作方案
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "XS_FGLDSP_GZFA":           //分管审批工作方案
                    if(vm.model.isNeedWrokPrograml == '9'){
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "XS_FW":                    //发文申请
                    vm.businessTr = true;
                    vm.FW_SQ = true;
                    if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted == 9){
                        vm.show_dispatch = true;
                        $("#show_dispatch_a").click();
                    };
                    break;
                case "XS_BZSP_FW":               //部长审批发文
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "XS_FGLDSP_FW":             //分管领导审批发文
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "XS_ZRSP_FW":               //主任审批发文
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "XS_FZR_GD":                //第一负责人归档
                    vm.businessTr = true;
                    vm.MFZR_GD = true;
                    if(vm.model.filenum){
                        vm.show_filerecord = true;
                        $("#show_filerecord_a").click();
                    }
                    break;
                case "XS_FZR_SP":                //第二负责人审批归档

                    break;
                case "XS_QRGD":                  //确认归档

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
                    $('.seleteTable input[selectType="main"]:checked').each(function(){
                        vm.flow.businessMap.M_USER_ID = $(this).val();
                        seleteCount++;
                    });
                    if(seleteCount == 0){
                        resultTag = false;
                        break;
                    }
                    $('.seleteTable input[selectType="assist"]:checked').each(function(){
                        vm.flow.businessMap.A_USER_ID = $(this).val();
                    });
                    break;
                case "BM_FB2":      //部门分办，要选择主办部门
                    $('.seleteTable input[selectType="main"]:checked').each(function(){
                        vm.flow.businessMap.M_USER_ID = $(this).val();
                        seleteCount++;
                    });
                    if(seleteCount == 0){
                        resultTag = false;
                        break;
                    }
                    $('.seleteTable input[selectType="assist"]:checked').each(function(){
                        vm.flow.businessMap.A_USER_ID = $(this).val();
                    });
                    break;
                case "XMFZR_SP_GZFA1":
                    if(vm.model.isNeedWrokPrograml && vm.model.isNeedWrokPrograml == '9'){
                        if(vm.model.isreviewCompleted && vm.model.isreviewCompleted==9){
                            resultTag = true;
                        }else{
                            resultTag = false;
                        }
                    }

                    break;
                case "XMFZR_SP_GZFA2":
                    if(vm.model.isNeedWrokPrograml && vm.model.isNeedWrokPrograml == '9') {
                        if (vm.model.isreviewACompleted && vm.model.isreviewACompleted == 9) {
                            resultTag = true;
                        } else {
                            resultTag = false;
                        }
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
                		/*common.initJqValidation($("#dispatch_form"));
						var isValid = $("#dispatch_form").valid();
						if (isValid) {
							resultTag = false;
						}*/
	                    if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted==9 && vm.model.docnum){
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
                    if(vm.model.filenum){
                        resultTag = true;
                    }else{
                        resultTag = false;
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
                    $('.seleteTable input[selectType="main"]:checked').each(function(){
                        vm.flow.businessMap.M_USER_ID = $(this).val();
                        seleteCount++;
                    });
                    if(seleteCount == 0){
                        resultTag = false;
                        break;
                    }
                    $('.seleteTable input[selectType="assist"]:checked').each(function(){
                        vm.flow.businessMap.A_USER_ID = $(this).val();
                    });
                    break;
                case "XS_XMFZR_GZFA":           //项目负责人承办
                    vm.flow.businessMap.PSFA == vm.model.isNeedWrokPrograml
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