(function() {
	'use strict';
	
	angular.module('app').factory('flowSvc', flow);
	
	flow.$inject = ['$http','$state'];

	function flow($http,$state) {
		var service = {
				initFlowData : initFlowData,		//初始化流程数据
				getNextStepInfo : getNextStepInfo,	//获取下一环节信息
				commit : commit,					//提交
				rollBackToLast : rollBackToLast,	//回退到上一环节	
				rollBack : rollBack,				//回退到选定环节
				initBackNode : initBackNode,		//初始化回退环节信息
				initDealUerByAcitiviId : initDealUerByAcitiviId,
				suspendFlow : suspendFlow,			//流程挂起
				activeFlow : activeFlow,			//重启流程
				deleteFlow : deleteFlow,			//流程终止
				disableButton : disableButton,		//禁用按钮
				enableButton : enableButton			//启用按钮
		};
		return service;			
		
		//S_初始化流程数据
	    function initFlowData(vm){
	    	var processInstanceId = vm.flow.processInstanceId;
			
			vm.picture = rootPath+"/flow/proccessInstance/img/"+processInstanceId;		
			
			
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/flow/proccessInstance/history/"+processInstanceId),
				schema : common.kendoGridConfig().schema({
					id : "id"
				}),
				rowNumber: true,  
	            headerCenter: true,  
			});
						
			var columns = [	
				{
	                field: "",
	                title: "序号",
	                template: "<span class='row-number'></span>",
	                width:30
	            },
				{
					field : "activityName",
					title : "环节名称",
					width : 120,
					filterable : false
				},
				{
					field : "assignee",
					title : "处理人",
					width : 80,
					filterable : false
				},
				{
					field : "startTime",
					title : "开始时间",
					width : 120,
					filterable : false,
					template: "#=  (startTime == null)? '' : kendo.toString(new Date(startTime), 'yyyy-MM-dd hh:mm:ss') #"	
				},											
				{
					field : "",
					title : "结束时间",
					width : 120,
					filterable : false,
					template: function(item) {
						if(item.endTime){
							return kendo.toString(new Date(item.endTime), 'yyyy-MM-dd hh:mm:ss');
						}
						else{
							return " ";
						}
					}	
				},
				{
					field : "duration",
					title : "处理时长",
					width : 120,
					filterable : false
				},
				{
					field : "message",
					title : "处理信息",
					width : 300,
					filterable : false
				}
			];
			// End:column
			vm.historygrid = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				resizable : true,
				dataBound: function () {  					
	                var rows = this.items(); 	               
	                $(rows).each(function (i) {	
	                	 if(i == rows.length -1 ){
	                		 initBackNode(vm);
	                	 }
	                     $(this).find(".row-number").html(i+1);                
	                });  
	            } 
			};				
	    }//E_初始化流程数据
	    
	    //S_获取下一环节信息
	    function getNextStepInfo(vm){
	    	var httpOptions = {
					method : 'get',
					url : rootPath+"/flow/proccessInstance/nextNodeDeal",
					params : {
						taskId: vm.flow.taskId,
						proccessInstanceId:vm.flow.processInstanceId					
					}
				}

			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {		
						vm.flow.end = response.data.end;
						vm.isOverStep = vm.flow.end;
						vm.isHaveNext = vm.flow.end == true?false:true;	
						vm.flow.processKey = response.data.processKey;
						
						if(vm.flow.end == false){
							if(response.data.curNode){
								vm.flow.curNodeName = response.data.curNode.activitiName;
								vm.flow.curNodeAcivitiId = response.data.curNode.activitiId;
								//显示相应的按钮
								if(vm.flow.curNodeAcivitiId == "approval" || vm.flow.curNodeAcivitiId == "dispatch" 
									|| vm.flow.curNodeAcivitiId == "doFile"){	
									vm.showBtByAcivitiId(vm.flow.curNodeAcivitiId);								
								}							
							}												
							if(response.data.nextNode){
								vm.nextNode = response.data.nextNode;
								vm.flow.nextNodeAcivitiId = response.data.nextNode[0].activitiId;
							}
							if(response.data.nextGroup){
								vm.flow.nextGroup = response.data.nextGroup;	
							}						
							if(response.data.nextDealUserList){
								vm.nextDealUserList = response.data.nextDealUserList;	
								if(response.data.nextDealUserList && response.data.nextDealUserList.length > 0){
									vm.flow.nextDealUser = response.data.nextDealUserList[0].loginName;	//默认选中
								}
							}	
							if(response.data.nextUserListMap){
								vm.nextDealUserMap = {};
								vm.nextDealUserMap = response.data.nextUserListMap;
								vm.nextDealUserList = vm.nextDealUserMap[vm.flow.nextNodeAcivitiId];
								if(vm.nextDealUserList){
									vm.flow.nextDealUser = vm.nextDealUserList[0].loginName;	//默认选中
								}
							}
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
	    }//E_获取下一环节信息
	    
	    //S_提交下一步
		function commit(vm){
			common.initJqValidation($("#flow_form"));			
			var isValid = $("#flow_form").valid();
			if(isValid){
				disableButton(vm);
				var httpOptions = {
						method : 'post',
						url : rootPath+"/flow/commit",
						data : vm.flow						
					}

				var httpSuccess = function success(response) {					
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {	
							if(response.data.reCode == "error"){
								enableButton(vm);
							}
							common.alert({
								vm:vm,
								msg: response.data.reMsg,
								closeDialog : true,
								fn : function() {									
									if(vm.flow.processKey == "signflow"){
										$state.go('flowSign');
									}									
								}
							})
						}
						
					})
				}

				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess
				});
			}			
		}//E_提交下一步
		
		//S_回退到上一步
		function rollBackToLast(vm){
			disableButton(vm);			
			var httpOptions = {
					method : 'post',
					url : rootPath+"/flow/rollbacklast",
					data : vm.flow							
				}
			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {	
						if(response.data.reCode == "error"){
							enableButton(vm);
						}
						common.alert({
							vm:vm,
							msg: response.data.reMsg
						})
					}
					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//E_回退到上一步
		
		//S_回退到指定环节
		function rollBack(vm){	
			if(vm.flow.back == null || vm.flow.back.activitiId == null || vm.flow.back.activitiId == ""){
				common.alert({
					vm:vm,
					msg: "请先选择要会退的环节！"
				})
				return;
			}
        	      	
        	common.confirm({
             	 vm:vm,
             	 title:"",
             	 msg:"确认回退吗？",
             	 fn:function () {
             		disableButton(vm);
        			//设置
        			vm.flow.rollBackActiviti = vm.flow.back.activitiId;
        			vm.flow.rollBackActiviti = vm.flow.back.assignee;
        			
        			var httpOptions = {
        					method : 'post',
        					url : rootPath+"/flow/rollback",
        					data : vm.flow							
        				}
        			var httpSuccess = function success(response) {					
        				common.requestSuccess({
        					vm:vm,
        					response:response,
        					fn:function() {	
        						if(response.data.reCode == "error"){
        							enableButton(vm);
        						}
        						common.alert({
        							vm:vm,
        							msg: response.data.reMsg
        						})
        					}       					
        				})
        			}

        			common.http({
        				vm:vm,
        				$http:$http,
        				httpOptions:httpOptions,
        				success:httpSuccess
        			});                   	
                }
             })            			
		}//E_回退到指定环节
		
		//S_初始化回退环节信息
		function initBackNode(vm){
			vm.backNode = [];
			//初始化可回退环节
			var datas = vm.historygrid.dataSource.data()						
			var totalNumber = datas.length;			
			for(var i = 0; i<totalNumber; i++) {
				if(datas[i].assignee && datas[i].endTime){				
					vm.backNode.push({"activitiId":datas[i].activityId,"activitiName":datas[i].activityName,"assignee":datas[i].assignee});
				}							    
			}
		}//E_初始化回退环节信息				
		
		//S_初始化下一环节处理人
		function initDealUerByAcitiviId(vm){
			vm.nextDealUserList = vm.nextDealUserMap[vm.flow.nextNodeAcivitiId];
			if(vm.nextDealUserList){
				vm.flow.nextDealUser = vm.nextDealUserList[0].loginName;	//默认选中
			}
		}//E_初始化下一环节处理人	
		
		//S_流程挂起
		function suspendFlow(vm,businessKey){
			var httpOptions = {
					method : 'post',
					url : rootPath+"/flow/suspend/"+businessKey						
				}
			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {	
						common.alert({
							vm:vm,
							msg: "操作成功！"
						})
					}       					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});                   	
		}//E_流程挂起
		
		//S_流程激活
		function activeFlow(vm,businessKey){
			var httpOptions = {
					method : 'post',
					url : rootPath+"/flow/active/"+businessKey						
				}
			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {	
						common.alert({
							vm:vm,
							msg: "操作成功！"
						})
					}       					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//E_流程激活
		
		//S_终止流程
		function deleteFlow(vm){
			if(vm.flow.dealOption == null || vm.flow.dealOption == ""){
				common.alert({
					vm:vm,
					msg: "请填写处理信息！"
				})
				return ;
			}
			var httpOptions = {
					method : 'post',
					url : rootPath+"/flow/deleteFLow",
					data : vm.flow								
				}
			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {	
						common.alert({
							vm:vm,
							msg: "操作成功！"
						})
					}       					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//E_终止流程
		
		//S_禁用按钮
		function disableButton(vm){
			vm.disabledButton = true;
		}//E_禁用按钮
		
		//S_启用按钮
		function enableButton(vm){
			vm.disabledButton = false;
		}//E_启用按钮
	}
	
})();