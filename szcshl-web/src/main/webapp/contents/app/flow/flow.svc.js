(function() {
	'use strict';
	
	angular.module('app').factory('flowSvc', flow);
	
	flow.$inject = ['$http','$state','signFlowSvc'];

	function flow($http,$state,signFlowSvc) {
		var service = {
				initFlowData : initFlowData,		//初始化流程数据
				getFlowInfo : getFlowInfo,			//获取流程信息
				commit : commit,					//提交
				rollBackToLast : rollBackToLast,	//回退到上一环节	
				rollBack : rollBack,				//回退到选定环节
				initBackNode : initBackNode,		//初始化回退环节信息
				initDealUerByAcitiviId : initDealUerByAcitiviId,
				suspendFlow : suspendFlow,			//流程挂起
				activeFlow : activeFlow,			//重启流程
				deleteFlow : deleteFlow			//流程终止
		};
		return service;			
		
		//S_初始化流程数据
	    function initFlowData(vm){
	    	var processInstanceId = vm.flow.processInstanceId;
            if(angular.isUndefined(vm.flow.hideFlowImg) || vm.flow.hideFlowImg == false){
                vm.picture = rootPath+"/flow/processInstance/img/"+processInstanceId;
            }

			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/flow/processInstance/history/"+processInstanceId),
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
	    
	    //S_getFlowInfo
	    function getFlowInfo(vm){
	    	var httpOptions = {
					method : 'get',
					url : rootPath+"/flow/processInstance/flowNodeInfo",
					params : {
						taskId: vm.flow.taskId,
						processInstanceId:vm.flow.processInstanceId					
					}
				}

			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {		
						vm.flow = response.data;
						signFlowSvc.initBusinessParams(vm);
					}
					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
	    }//E_getFlowInfo
	    
	    //S_提交下一步
		function commit(vm){
			common.initJqValidation($("#flow_form"));			
			var isValid = $("#flow_form").valid();
			if(isValid){
				vm.isCommit = true;
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
							common.alert({
								vm:vm,
								msg: response.data.reMsg,
								closeDialog : true,
								fn : function() {	
									if(response.data.reCode == "error"){
										vm.isCommit = false;
									}else{
										$state.go('index');
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
					success:httpSuccess,
					onError: function(response){vm.isCommit = false;}
				});
			}			
		}//E_提交下一步
		 
		//S_回退到上一步
		function rollBackToLast(vm){	
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
        			//设置
        			vm.flow.rollBackActiviti = vm.flow.back.activitiId;
        			vm.flow.backNodeDealUser = vm.flow.back.assignee;
        			
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
				
	}
	
})();