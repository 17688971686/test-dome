(function() {
	'use strict';
	
	angular.module('app').factory('signFlowSvc', signFlow);
	
	signFlow.$inject = ['$http','$state'];

	function signFlow($http,$state) {
		var service = {
			startFlow : startFlow,			//启动流程
			pendingSign:pendingSign,		//签收待处理
			initBusinessParams:initBusinessParams,	//初始化业务参数
			checkBusinessFill : checkBusinessFill,	//检查相应的表单填写
			getChargeWorkProgram:getChargeWorkProgram
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
		
		//S_pendingSign
		function pendingSign(vm){
			// Begin:dataSource
			var dataSource = common.kendoGridDataSource(rootPath+"/sign/html/pendingSign");				
						
			var columns = [
				 {
                     field: "",
                     title: "序号",
                     template: "<span class='row-number'></span>",
                     width:30
                 },
				{
					field : "projectname",
					title : "项目名称",
					width : 200,
					filterable : true
				},
				{
					field : "projectcode",
					title : "项目编号",
					width : 200,
					filterable : true
				},
				{
					field : "createdDate",
					title : "创建时间",
					width : 180,
					filterable : false,
					format : "{0:yyyy/MM/dd HH:mm:ss}"

				},
				{
					field : "",
					title : "操作",
					width : 180,
					template:function(item){
						//如果项目已暂停，则停止对流程操作
						var hideDealButton = false;
						if(item.folwState && item.folwState == 2){
							hideDealButton = true;
						}
						return common.format($('#columnBtns').html(),item.signid,item.taskId,item.processInstanceId,hideDealButton);
					}	
				}
			];
			// End:column
			vm.gridOptions = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				resizable : true,
				dataBound: function () {  
                    var rows = this.items();  
                    var page = this.pager.page() - 1;  
                    var pagesize = this.pager.pageSize();  
                    $(rows).each(function () {  
                        var index = $(this).index() + 1 + page * pagesize;  
                        var rowLabel = $(this).find(".row-number");  
                        $(rowLabel).html(index);  
                    });  
                } 
			};					
		}//E_pendingSign
		
		//S_initBusinessParams
		function initBusinessParams(vm){			
			if(vm.flow.curNode.activitiId == "FGLD_SP_SW"){//部门分办
				vm.businessTr = true;
				vm.FGLD_SP_SW = true;
				var httpOptions = {
						method : 'get',
						url : rootPath+"/org/findUserChargeOrg"
					}
					
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.orgs = response.data;
						}
					});
				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
			
			}else if(vm.flow.curNode.activitiId == "BM_FB1" || vm.flow.curNode.activitiId == "BM_FB2"){//选择项目负责人	
				vm.businessTr = true;
				vm.BM_FB = true;
				var httpOptions = {
						method : 'get',
						url : rootPath+"/user/findChargeUsers"
					}
					
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.users = response.data;
						}
					});
				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
			}else if(vm.flow.curNode.activitiId == "XMFZR_SP_GZFA1" || vm.flow.curNode.activitiId == "XMFZR_SP_GZFA2"){ //项目负责人承办
				vm.businessTr = true;
				vm.XMFZR_SP_GZFA = true;
				getChargeWorkProgram(vm,vm.flow.curNode.activitiId);
				
			}else if(vm.flow.curNode.activitiId == "BZ_SP_GZAN1" || vm.flow.curNode.activitiId == "BZ_SP_GZAN2"){ //部长审批
				getChargeWorkProgram(vm,vm.flow.curNode.activitiId);
				
			}else if(vm.flow.curNode.activitiId == "FGLD_SP_GZFA1" || vm.flow.curNode.activitiId == "FGLD_SP_GZFA2"){ //分管副主任审批
				getChargeWorkProgram(vm,vm.flow.curNode.activitiId);
				
			}else if(vm.flow.curNode.activitiId == "FW_SQ" ){ //发文
				vm.businessTr = true;
				vm.FW_SQ = true;				
			}else if(vm.flow.curNode.activitiId == "MFZR_GD" ){ //归档
				vm.businessTr = true;
				vm.FW_SQ = true;
				
			}
			
		}//E_initBusinessParams
		
		//S_checkBusinessFill
		function checkBusinessFill(vm){
			vm.flow.businessMap = {};
			var seleteCount = 0;			
			if(vm.flow.curNode.activitiId == "FGLD_SP_SW"){	//部门分办，要选择主办部门						
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
				
			}else if(vm.flow.curNode.activitiId == "XMFZR_SP_GZFA1" || vm.flow.curNode.activitiId == "XMFZR_SP_GZFA2"){
				if(vm.model.isreviewcompleted && vm.model.isreviewcompleted==9){
					return true;
				}else{
					return false;
				}
			}
			else if(vm.flow.curNode.activitiId == "BZ_SP_GZAN1" || vm.flow.curNode.activitiId == "FGLD_SP_GZFA1"){
				vm.flow.businessMap.M_WP_ID = vm.work.id;
				return true;
			}
			else if(vm.flow.curNode.activitiId == "BZ_SP_GZAN2" || vm.flow.curNode.activitiId == "FGLD_SP_GZFA2"){
				vm.flow.businessMap.A_WP_ID = vm.work.id;
				return true; 
			}			
			else if(vm.flow.curNode.activitiId == "FW_SQ"){
				if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted==9){
					return true;
				}else{
					return false;
				}
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
		function getChargeWorkProgram(vm,activitiId){
			vm.flow.businessMap = {};
			var httpOptions = {
					method : 'get',
					url : rootPath+"/workprogram/html/initWorkBySignId",
					params : {signId:vm.model.signid}
				}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {								
						vm.work = response.data;	
						if(activitiId == "BZ_SP_GZAN1" || activitiId == "FGLD_SP_GZFA1" 
							|| activitiId == "BZ_SP_GZAN2" || activitiId == "FGLD_SP_GZFA2"){
							
							$("#show_workprogram_a").click();
						}
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
		
		
	}//E_signFlow		
})();