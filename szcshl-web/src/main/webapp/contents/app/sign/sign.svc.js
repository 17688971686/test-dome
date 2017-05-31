(function() {
	'use strict';
	
	angular.module('app').factory('signSvc', sign);
	
	sign.$inject = ['$http','$state'];

	function sign($http,$state) {
		var service = {
			grid : grid,						//初始化项目列表
			querySign : querySign,				//查询
			createSign : createSign,			//新增
			initFillData : initFillData,		//初始化表单填写页面（可编辑）
			initDetailData : initDetailData,	//初始化详情页（不可编辑）
			updateFillin : updateFillin,		//申报编辑
			deleteSign :　deleteSign,			//删除收文		
			startFlow : startFlow,				//发起流程
			findOfficeUsersByDeptId :findOfficeUsersByDeptId,//根据协办部门ID查询用户
			initFlowPageData : initFlowPageData, //初始化流程收文信息
			
			add:add,							//s_项目建设书阶段(一)
			addCopy:addCopy,
			sugFileDealOriginal:sugFileDealOriginal,
			sugFileDealCopy:sugFileDealCopy,
			
			
		};
		return service;	
		//s_项目建设书阶段(一)
		function add(vm){
			if(vm.model.sugProDealOriginal==null){
				vm.model.sugProDealCount=1;
			}if(vm.model.sugProDealOriginal==9){
				vm.model.sugProDealCount="";
			}if(vm.model.sugProDealOriginal==0){
				vm.model.sugProDealCount=1;
			}
		}
		function addCopy(vm){
			if(vm.model.sugProDealCopy==null){
			vm.model.sugProDealCount=1;
			}if(vm.model.sugProDealCopy==9){
			vm.model.sugProDealCount="";
			}if(vm.model.sugProDealCopy==0){
			vm.model.sugProDealCount=1;
			}
		}
		function sugFileDealOriginal(vm){
			if(vm.model.sugFileDealOriginal==null){
			vm.model.sugFileDealCount=1;
			}if(vm.model.sugFileDealOriginal==9){
			vm.model.sugFileDealCount="";
			}if(vm.model.sugFileDealOriginal==0){
			vm.model.sugFileDealCount=1;
			}
		}
		
		function sugFileDealCopy(vm){
			if(vm.model.sugFileDealCopy==null){
				vm.model.sugFileDealCount=1;
				}if(vm.model.sugFileDealCopy==9){
				vm.model.sugFileDealCount="";
				}if(vm.model.sugFileDealCopy==0){
				vm.model.sugFileDealCount=1;
			}
		}
		//E_项目建设书阶段(一)
		
		//S_初始化grid
		function grid(vm){
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata', 
				transport :common.kendoGridConfig().transport(rootPath+"/sign/fingByOData",$("#searchform")),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,
				pageSize : 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			// End:dataSource

			// Begin:column
			var columns = [
					{
						template : function(item) {
							return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",item.signid)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

					},
					{
						field : "projectname",
						title : "项目名称",
						width : 160,
						filterable : false
					},
					{
						field : "projectcode",
						title : "项目编号",
						width : 160,
						filterable : false,
					},
					{
						field : "maindeptName",
						title : "主办事处名称",
						width : 100,
						filterable : false,
					},
					{
						field : "mainDeptUserName",
						title : "主办事处联系人",
						width : 100,
						filterable : false,
					},
					{
						field : "createdDate",
						title : "创建时间",
						width : 160,
						filterable : false,
						format : "{0:yyyy/MM/dd HH:mm:ss}"

					},					
					{
						field : "",
						title : "流程状态",
						width : 160,
						filterable : false,
						template : function(item) {
							if(item.folwState){
								if(item.folwState == 1){
									return '<span style="color:green;">进行中</span>';
								}else if(item.folwState == 2){
									return '<span style="color:orange;">已暂停</span>';
								}else if(item.folwState == 8){
									return '<span style="color:red;">强制结束</span>';
								}else if(item.folwState == 9){
									return '<span style="color:blue;">已完成</span>';
								}
							}else{
								return "未发起"
							}
						}
					},
					{
						field : "",
						title : "操作",
						width : 180,
						template : function(item) {
							//如果已经发起流程，则只能查看
							var isFlowStart = false,hideStopButton = true,hideRestartButton=true;
							if(item.folwState && item.folwState > 0){
								isFlowStart = true;
								if(item.folwState == 1){
									hideStopButton = false;
								}
								if(item.folwState == 2){
									hideRestartButton = false;
								}																								
							}						
							return common.format($('#columnBtns').html(), item.signid, item.folwState,
									item.signid,"vm.del('" + item.signid + "')",isFlowStart,
									"vm.startNewFlow('" + item.signid + "')", isFlowStart,
									"vm.stopFlow('" + item.signid + "')", hideStopButton,
									"vm.restartFlow('" + item.signid + "')", hideRestartButton);
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
				resizable : true
			};
		}//E_初始化grid
		
		//S_查询grid
		function querySign(vm){	
			vm.gridOptions.dataSource.read();			
		}//E_查询grid		
								
		//S_创建收文
		function createSign(vm){
			common.initJqValidation();
			var isValid = $('form').valid();				
			if (isValid) {				
				var httpOptions = {
						method : 'post',
						url : rootPath+"/sign",
						data : vm.model
					}
					var httpSuccess = function success(response) {									
						common.requestSuccess({
							vm:vm,
							response:response,
							fn:function() {				
								common.alert({
									vm:vm,
									msg:"操作成功,请继续填写报审登记表！",
									closeDialog:true,
									fn:function() {
										$state.go('fillSign', {signid: response.data.signid});
									}
								})
							}						
						});
					}
					common.http({
						vm:vm,
						$http:$http,
						httpOptions:httpOptions,
						success:httpSuccess
					});
			}
		}//E_创建收文	
		
		//start  根据协办部门查询用户
		function findOfficeUsersByDeptId(vm,status){
			var param = {};
			if("main" == status){
				param.deptId = vm.model.maindepetid;
			}else{
				param.deptId = vm.model.assistdeptid;
			}
			var httpOptions = {
					method : 'get',
					url : rootPath+"/officeUser/findOfficeUserByDeptId",
					params:param					
				};
				
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {		
						if("main" == status){
							vm.mainOfficeList = {};
							vm.mainOfficeList = response.data;
						}else{
							vm.assistOfficeList = {};
							vm.assistOfficeList = response.data;
						}
					}
				});
			};
			
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		//end  根据协办部门查询用户
		
		//Start 申报登记编辑
		function updateFillin(vm){
				common.initJqValidation($('#sign_fill_form'));
				var isValid = $('#sign_fill_form').valid();	 
				if (isValid) {
				var httpOptions = {
					method : 'put',
					url : rootPath+"/sign",
					data : vm.model,
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');
								}
							})
						}
					})
				}
	
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
			}
		}
		//End 申报登记编辑
		
		//Start 删除收文 
		function deleteSign(vm, signid) {			
			vm.isSubmit = true;
			var httpOptions = {
				method : 'delete',
				url : rootPath+"/sign",
				data : signid
			}
			
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.isSubmit = false;
						vm.gridOptions.dataSource.read();
					}

				});

			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		//End 删除收文				
		
		//S_初始化填报页面数据
		function initFillData(vm){		
			var httpOptions = {
					method : 'get',
					url : rootPath+"/sign/html/initFillPageData",
					params : {signid : vm.model.signid}						
				}

			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {											
						vm.model = response.data.sign;	
						vm.deptlist =response.data.deptlist	
						
						if(response.data.mainOfficeList){
							vm.mainOfficeList=response.data.mainOfficeList;
						}
						if(response.data.assistOfficeList){
							vm.assistOfficeList=response.data.assistOfficeList;
						}
						//建设单位
						vm.builtcomlist = response.data.builtcomlist;
						//编制单位
						vm.designcomlist = response.data.designcomlist;
					}					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//E_初始化填报页面数据				
		
		//S_初始化详情数据	
		function initDetailData(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/sign/html/initDetailPageData",
					params : {signid:vm.model.signid}
				}

			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {
					    console.log(response.data);
						vm.model = response.data;													
					}					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//E_初始化详情数据						
				
		//S_发起流程
		function startFlow(vm,signid){
			var httpOptions = {
					method : 'post',
					url : rootPath+"/sign/html/startFlow",
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
		}//E_发起流程				
						
		//S_初始化流程页面
		function initFlowPageData(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/sign/html/initFlowPageData",
					params : {signid:vm.model.signid}
				}
				
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.model = response.data;	
					}
				});
			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}//E_初始化流程页面				
	}		
})();