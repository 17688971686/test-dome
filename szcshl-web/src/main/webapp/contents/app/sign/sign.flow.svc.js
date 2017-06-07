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
            endSignDetail:endSignDetail                 //已办结的签收信息（停用）
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
			if(vm.flow.curNode.activitiId == "XMFZR_SP_GZFA1" ){ //项目负责人承办
				vm.businessTr = true;
				vm.XMFZR_SP_GZFA = true;	
				if(vm.model.isreviewCompleted && vm.model.isreviewCompleted == 9){ //如果填报完成，则显示
					vm.show_workprogram = true;
					$("#show_workprogram_a").click();					
				}										
			}else if( vm.flow.curNode.activitiId == "XMFZR_SP_GZFA2"){
				vm.businessTr = true;
				vm.XMFZR_SP_GZFA = true; 
				if(vm.model.isreviewACompleted && vm.model.isreviewACompleted == 9){ //如果填报完成，则显示
					vm.show_workprogram = true;
					$("#show_workprogram_a").click();
				}	
			}else if(vm.flow.curNode.activitiId == "BZ_SP_GZAN1" || vm.flow.curNode.activitiId == "FGLD_SP_GZFA1" ){ //部长审批,分管副主任审批
				vm.show_workprogram = true;
				$("#show_workprogram_a").click();				
			}else if(vm.flow.curNode.activitiId == "BZ_SP_GZAN2" || vm.flow.curNode.activitiId == "FGLD_SP_GZFA2"){
				vm.show_workprogram = true;
				$("#show_workprogram_a").click();				
			}else if(vm.flow.curNode.activitiId == "FW_SQ" ){ //发文
				vm.businessTr = true;
				vm.FW_SQ = true;		
				if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted >= 0){
					vm.show_dispatch = true;
					$("#show_dispatch_a").click();
				}	
			
			}//审核发文	
			else if(vm.flow.curNode.activitiId == "BZ_SP_FW" || vm.flow.curNode.activitiId == "FGLD_SP_FW" || vm.flow.curNode.activitiId == "ZR_SP_FW"){ 
				vm.show_dispatch = true;
				$("#show_dispatch_a").click();
				
			}else if(vm.flow.curNode.activitiId == "MFZR_GD" ){ //归档
				vm.businessTr = true;
				vm.MFZR_GD = true;
				if(vm.model.filenum){
					vm.show_filerecord = true;
					$("#show_filerecord_a").click();
				}
			}
			else if(vm.flow.curNode.activitiId == "AZFR_SP_GD" || vm.flow.curNode.activitiId == "BMLD_QR_GD"){ //归档
				vm.show_filerecord = true;
				$("#show_filerecord_a").click();							
			}
						
		}//E_initBusinessParams
		
		//S_checkBusinessFill
		function checkBusinessFill(vm){
			vm.flow.businessMap = {};
			var seleteCount = 0;			
			if(vm.flow.curNode.activitiId == "ZHB_SP_SW"){//综合部拟办
				if($("#viceDirector").val()){
					vm.flow.businessMap.FGLD = $("#viceDirector").val();
					return true;
				}
				return false;				
			}else if(vm.flow.curNode.activitiId == "FGLD_SP_SW"){	//部门分办，要选择主办部门						
				$('.seleteTable input[selectType="main"]:checked').each(function(){ 
					vm.flow.businessMap.hostdept = $(this).val();
					seleteCount++;					
				}); 				 
				if(seleteCount == 0){
					return false;
				}
				$('.seleteTable input[selectType="assist"]:checked').each(function(){ 
					vm.flow.businessMap.assistdept = $(this).val();				
				});
				return true;
				
			}else if(vm.flow.curNode.activitiId == "BM_FB1" || vm.flow.curNode.activitiId == "BM_FB2"){
				$('.seleteTable input[selectType="main"]:checked').each(function(){ 
					vm.flow.businessMap.M_USER_ID = $(this).val();
					seleteCount++;					
				}); 				 
				if(seleteCount == 0){
					return false;
				}
				$('.seleteTable input[selectType="assist"]:checked').each(function(){ 
					vm.flow.businessMap.A_USER_ID = $(this).val();				
				});
				return true;
				
			}else if(vm.flow.curNode.activitiId == "XMFZR_SP_GZFA1"){
				if(vm.model.isreviewCompleted && vm.model.isreviewCompleted==9){
					return true;
				}else{
					return false;
				}
			}else if(vm.flow.curNode.activitiId == "XMFZR_SP_GZFA2"){
				if(vm.model.isreviewACompleted && vm.model.isreviewACompleted==9){
					return true;
				}else{
					return false;
				}
			}
			else if(vm.flow.curNode.activitiId == "BZ_SP_GZAN1" || vm.flow.curNode.activitiId == "FGLD_SP_GZFA1"){
				vm.flow.businessMap.M_WP_ID = vm.mainwork.id;
				return true;
			}
			else if(vm.flow.curNode.activitiId == "BZ_SP_GZAN2" || vm.flow.curNode.activitiId == "FGLD_SP_GZFA2"){
				vm.flow.businessMap.A_WP_ID = vm.assistwork.id;
				return true; 
			}			
			else if(vm.flow.curNode.activitiId == "FW_SQ"){
				if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted==9){
					return true;
				}else{
					return false;
				}
			}else if(vm.flow.curNode.activitiId == "BZ_SP_FW" || vm.flow.curNode.activitiId == "FGLD_SP_FW" || vm.flow.curNode.activitiId == "ZR_SP_FW" ){ //审核发文
				vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
				return true;				
			}else if(vm.flow.curNode.activitiId == "MFZR_GD"){
				if(vm.model.filenum){
					return true;
				}else{
					return false;
				}
			}			
			return true;
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