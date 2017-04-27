(function() {
	'use strict';
	
	angular.module('app').factory('flowSvc', flow);
	
	flow.$inject = ['$http','$state'];

	function flow($http,$state) {
		var service = {
				initFlowData : initFlowData,		//初始化流程数据
				getNextStepInfo : getNextStepInfo,	//获取下一环节信息
				commit : commit,					//提交
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
					width : 200,
					filterable : false
				},
				{
					field : "startTime",
					title : "开始时间",
					width : 200,
					filterable : false,
					template: "#=  (startTime == null)? '' : kendo.toString(new Date(startTime), 'yyyy-MM-dd hh:mm:ss') #"	
				},
				{
					field : "",
					title : "结束时间",
					width : 200,
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
					field : "assignee",
					title : "目前处理人",
					width : 200,
					filterable : false
				}
			];
			// End:column
			vm.historygrid = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				resizable : true,
				dataBound: function () {  
	                var rows = this.items();   
	                var pagesize = this.pager.pageSize();  
	                $(rows).each(function (i) {                    	
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
					params : {proccessInstanceId:vm.flow.processInstanceId}						
				}

			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {						
						//初始化未完成
						vm.flow.nextGroup = response.data.nextGroup;	
						vm.nextDealUserList = response.data.nextDealUserList;	
						if(response.data.nextDealUserList){
							vm.flow.nextDealUser = response.data.nextDealUserList[0].loginName;	//默认选中
						}						
						vm.isOverStep = response.data.isEnd;
						vm.isHaveNext = response.data.isEnd == true?false:true;
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
						console.log(response);
						common.alert({
							vm:vm,
							msg: response.data.reMsg,
							fn:function() {
								if(response.data.reCode == "error"){
									enableButton(vm);
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
		}//E_提交下一步
		
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