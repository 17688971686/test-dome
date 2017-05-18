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
			flowgrid : flowgrid,				//初始化待处理页面			
			startFlow : startFlow,				//发起流程
			findOfficeUsersByDeptId :findOfficeUsersByDeptId,//根据协办部门ID查询用户
			initFlowPageData : initFlowPageData, //初始化流程收文信息
			regNumber:regNumber,				//输入资料分数数字校验
			add:add,							//s_项目建设书阶段(一)
			addCopy:addCopy,
			sugFileDealOriginal:sugFileDealOriginal,
			sugFileDealCopy:sugFileDealCopy,
			sugOrgApplyOriginal:sugOrgApplyOriginal,
			sugOrgApplyCopy:sugOrgApplyCopy,
			sugOrgReqOriginal:sugOrgReqOriginal,
			sugOrgReqCopy:sugOrgReqCopy,
			sugProAdviseOriginal:sugProAdviseOriginal,
			sugProAdviseCopy:sugProAdviseCopy,
			proSugEledocOriginal:proSugEledocOriginal,
			proSugEledocCopy:proSugEledocCopy,
			sugMeetOriginal:sugMeetOriginal,
			sugMeetCopy:sugMeetCopy,       //e_项目建设书阶段(一)
			studyPealOriginal:studyPealOriginal,//s #项目可研究性阶段(二)
			studyProDealCopy:studyProDealCopy,
			studyFileDealOriginal:studyFileDealOriginal,
			studyFileDealCopy:studyFileDealCopy,
			studyOrgApplyOriginal: studyOrgApplyOriginal,
			studyOrgApplyCopy:studyOrgApplyCopy,
			studyOrgReqOriginal:studyOrgReqOriginal,
			studyOrgReqCopy:studyOrgReqCopy,
			studyProSugOriginal:studyProSugOriginal,
			studyProSugCopy:studyProSugCopy,
			studyMeetOriginal:studyMeetOriginal,
			studyMeetCopy:studyMeetCopy,
			envproReplyOriginal:envproReplyOriginal,//右
			envproReplyCopy:envproReplyCopy,
			planAddrOriginal:planAddrOriginal,
			planAddrCopy:planAddrCopy,
			reportOrigin:reportOrigin,
			reportCopy:reportCopy,
			eledocOriginal:eledocOriginal,
			eledocCopy:eledocCopy,
			energyOriginal:energyOriginal,
			energyCopy:energyCopy,				 //s #项目可研究性阶段(二)
			
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
		function sugOrgApplyOriginal(vm){
			if(vm.model.sugOrgApplyOriginal==null){
				vm.model.sugOrgApplyCount=1;
			}if(vm.model.sugOrgApplyOriginal==9){
				vm.model.sugOrgApplyCount="";
			}if(vm.model.sugOrgApplyOriginal==0){
				vm.model.sugOrgApplyCount=1;
			}
		}
		function sugOrgApplyCopy(vm){
			if(vm.model.sugOrgApplyCopy==null){
				vm.model.sugOrgApplyCount=1;
			}if(vm.model.sugOrgApplyCopy==9){
				vm.model.sugOrgApplyCount="";
			}if(vm.model.sugOrgApplyCopy==0){
				vm.model.sugOrgApplyCount=1;
			}
		}
		function sugOrgReqOriginal(vm){
			if(vm.model.sugOrgReqOriginal==null){
				vm.model.sugOrgReqCount=1;
			}if(vm.model.sugOrgReqOriginal==9){
				vm.model.sugOrgReqCount="";
			}if(vm.model.sugOrgReqOriginal==0){
				vm.model.sugOrgReqCount=1;
			}
		}
		function sugOrgReqCopy(vm){
			if(vm.model.sugOrgReqCopy==null){
				vm.model.sugOrgReqCount=1;
			}if(vm.model.sugOrgReqCopy==9){
				vm.model.sugOrgReqCount="";
			}if(vm.model.sugOrgReqCopy==0){
				vm.model.sugOrgReqCount=1;
			}
		}
		function sugProAdviseOriginal(vm){
			if(vm.model.sugProAdviseOriginal==null){
				vm.model.sugProAdviseCount=1;
			}if(vm.model.sugProAdviseOriginal==9){
				vm.model.sugProAdviseCount="";
			}if(vm.model.sugProAdviseOriginal==0){
				vm.model.sugProAdviseCount=1;
			}
		}
		function sugProAdviseCopy(vm){
			if(vm.model.sugProAdviseCopy==null){
				vm.model.sugProAdviseCount=1;
			}if(vm.model.sugProAdviseCopy==9){
				vm.model.sugProAdviseCount="";
			}if(vm.model.sugProAdviseCopy==0){
				vm.model.sugProAdviseCount=1;
			}
		}
		function proSugEledocOriginal(vm){
			if(vm.model.proSugEledocOriginal==null){
				vm.model.proSugEledocCount=1;
			}if(vm.model.proSugEledocOriginal==9){
				vm.model.proSugEledocCount="";
			}if(vm.model.proSugEledocOriginal==0){
				vm.model.proSugEledocCount=1;
			}
		}
		function proSugEledocCopy(vm){
			if(vm.model.proSugEledocCopy==null){
				vm.model.proSugEledocCount=1;
			}if(vm.model.proSugEledocCopy==9){
				vm.model.proSugEledocCount="";
			}if(vm.model.proSugEledocCopy==0){
				vm.model.proSugEledocCount=1;
			}
		}
		function sugMeetOriginal(vm){
			if(vm.model.sugMeetOriginal==null){
				vm.model.sugMeetCount=1;
			}if(vm.model.sugMeetOriginal==9){
				vm.model.sugMeetCount="";
			}if(vm.model.sugMeetOriginal==0){
				vm.model.sugMeetCount=1;
			}
		}
		function sugMeetCopy(vm){
			if(vm.model.sugMeetCopy==null){
				vm.model.sugMeetCount=1;
			}if(vm.model.sugMeetCopy==9){
				vm.model.sugMeetCount="";
			}if(vm.model.sugMeetCopy==0){
				vm.model.sugMeetCount=1;
			}
		}
		//e_项目建设书阶段(一)
		
		//s_项目可研究性阶段(二)
		function studyPealOriginal(vm){
			if(vm.model.studyPealOriginal==null){
				vm.model.studyProDealCount=1;
			}if(vm.model.studyPealOriginal==9){
				vm.model.studyProDealCount="";
			}if(vm.model.studyPealOriginal==0){
				vm.model.studyProDealCount=1;
			}
		}
		function studyProDealCopy(vm){
			if(vm.model.studyProDealCopy==null){
				vm.model.studyProDealCount=1;
			}if(vm.model.studyProDealCopy==9){
				vm.model.studyProDealCount="";
			}if(vm.model.studyProDealCopy==0){
				vm.model.studyProDealCount=1;
			}
		}
		function studyFileDealOriginal(vm){
			if(vm.model.studyFileDealOriginal==null){
				vm.model.studyFileDealCount=1;
			}if(vm.model.studyFileDealOriginal==9){
				vm.model.studyFileDealCount="";
			}if(vm.model.studyFileDealOriginal==0){
				vm.model.studyFileDealCount=1;
			}
		}
		function studyFileDealCopy(vm){
			if(vm.model.studyFileDealCopy==null){
				vm.model.studyFileDealCount=1;
			}if(vm.model.studyFileDealCopy==9){
				vm.model.studyFileDealCount="";
			}if(vm.model.studyFileDealCopy==0){
				vm.model.studyFileDealCount=1;
			}
		}
		function  studyOrgApplyOriginal(vm){
			if(vm.model.studyOrgApplyOriginal==null){
				vm.model.studyOrgApplyCount=1;
			}if(vm.model.studyOrgApplyOriginal==9){
				vm.model.studyOrgApplyCount="";
			}if(vm.model.studyOrgApplyOriginal==0){
				vm.model.studyOrgApplyCount=1;
			}
		}
		function  studyOrgApplyCopy(vm){
			if(vm.model.studyOrgApplyCopy==null){
				vm.model.studyOrgApplyCount=1;
			}if(vm.model.studyOrgApplyCopy==9){
				vm.model.studyOrgApplyCount="";
			}if(vm.model.studyOrgApplyCopy==0){
				vm.model.studyOrgApplyCount=1;
			}
		}
		function studyOrgReqOriginal(vm){
			if(vm.model.studyOrgReqOriginal==null){
				vm.model.studyOrgReqCount=1;
			}if(vm.model.studyOrgReqOriginal==9){
				vm.model.studyOrgReqCount="";
			}if(vm.model.studyOrgReqOriginal==0){
				vm.model.studyOrgReqCount=1;
			}
		}
		function studyOrgReqCopy(vm){
			if(vm.model.studyOrgReqCopy==null){
				vm.model.studyOrgReqCount=1;
			}if(vm.model.studyOrgReqCopy==9){
				vm.model.studyOrgReqCount="";
			}if(vm.model.studyOrgReqCopy==0){
				vm.model.studyOrgReqCount=1;
			}
		}
		function studyProSugOriginal(vm){
			if(vm.model.studyProSugOriginal==null){
				vm.model.studyProSugCount=1;
			}if(vm.model.studyProSugOriginal==9){
				vm.model.studyProSugCount="";
			}if(vm.model.studyProSugOriginal==0){
				vm.model.studyProSugCount=1;
			}
		}
		function studyProSugCopy(vm){
			if(vm.model.studyProSugCopy==null){
				vm.model.studyProSugCount=1;
			}if(vm.model.studyProSugCopy==9){
				vm.model.studyProSugCount="";
			}if(vm.model.studyProSugCopy==0){
				vm.model.studyProSugCount=1;
			}
		}
		function studyMeetOriginal(vm){
			if(vm.model.studyMeetOriginal==null){
				vm.model.studyMeetCount=1;
			}if(vm.model.studyMeetOriginal==9){
				vm.model.studyMeetCount="";
			}if(vm.model.studyMeetOriginal==0){
				vm.model.studyMeetCount=1;
			}
		}
		function studyMeetCopy(vm){
			if(vm.model.studyMeetCopy==null){
				vm.model.studyMeetCount=1;
			}if(vm.model.studyMeetCopy==9){
				vm.model.studyMeetCount="";
			}if(vm.model.studyMeetCopy==0){
				vm.model.studyMeetCount=1;
			}
		}
		function envproReplyOriginal(vm){
			if(vm.model.envproReplyOriginal==null){
				vm.model.envproReplyCount=1;
			}if(vm.model.envproReplyOriginal==9){
				vm.model.envproReplyCount="";
			}if(vm.model.envproReplyOriginal==0){
				vm.model.envproReplyCount=1;
			}
		}
		function envproReplyCopy(vm){
			if(vm.model.envproReplyCopy==null){
				vm.model.envproReplyCount=1;
			}if(vm.model.envproReplyCopy==9){
				vm.model.envproReplyCount="";
			}if(vm.model.envproReplyCopy==0){
				vm.model.envproReplyCount=1;
			}
		}
		function planAddrOriginal(vm){
			if(vm.model.planAddrOriginal==null){
				vm.model.planAddrCount=1;
			}if(vm.model.planAddrOriginal==9){
				vm.model.planAddrCount="";
			}if(vm.model.planAddrOriginal==0){
				vm.model.planAddrCount=1;
			}
		}
		function planAddrCopy(vm){
			if(vm.model.planAddrCopy==null){
				vm.model.planAddrCount=1;
			}if(vm.model.planAddrCopy==9){
				vm.model.planAddrCount="";
			}if(vm.model.planAddrCopy==0){
				vm.model.planAddrCount=1;
			}
		}
		function reportOrigin(vm){
			if(vm.model.reportOrigin==null){
				vm.model.reportCount=1;
			}if(vm.model.reportOrigin==9){
				vm.model.reportCount="";
			}if(vm.model.reportOrigin==0){
				vm.model.reportCount=1;
			}
		}
		function reportCopy(vm){
			if(vm.model.reportCopy==null){
				vm.model.reportCount=1;
			}if(vm.model.reportCopy==9){
				vm.model.reportCount="";
			}if(vm.model.reportCopy==0){
				vm.model.reportCount=1;
			}
		}
		function eledocOriginal(vm){
			if(vm.model.eledocOriginal==null){
				vm.model.eledocCount=1;
			}if(vm.model.eledocOriginal==9){
				vm.model.eledocCount="";
			}if(vm.model.eledocOriginal==0){
				vm.model.eledocCount=1;
			}
		}
		function eledocCopy(vm){
			if(vm.model.eledocCopy==null){
				vm.model.eledocCount=1;
			}if(vm.model.eledocCopy==9){
				vm.model.eledocCount="";
			}if(vm.model.eledocCopy==0){
				vm.model.eledocCount=1;
			}
		}
		
		function energyOriginal(vm){
			if(vm.model.energyOriginal==null){
				vm.model.energyCount=1;
			}if(vm.model.energyOriginal==9){
				vm.model.energyCount="";
			}if(vm.model.energyOriginal==0){
				vm.model.energyCount=1;
			}
		}
		function energyCopy(vm){
			if(vm.model.energyCopy==null){
				vm.model.energyCount=1;
			}if(vm.model.energyCopy==9){
				vm.model.energyCount="";
			}if(vm.model.energyCopy==0){
				vm.model.energyCount=1;
			}
		}
		
		//e_项目可研究性阶段(二)
		
		//S_输入资料分数数字校验
		var reg =true;
		function regNumber(vm){
			alert(vm.model.sugProDealCount);
			var numbers = new RegExp("^[0-9]*$");
			if(!vm.model.sugProDealCount){
				$("#regNumber").hide();
				reg = true;
				return ;
			}
			if(!numbers.test(vm.model.sugProDealCount)){
				$("#regNumber").show();
				reg = false;
			}else{
				$("#regNumber").hide();
				reg = true;
			}
		}
		//E_输入资料分数数字校验
		
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
									return "进行中"
								}else if(item.folwState == 2){
									return "已暂停"
								}else if(item.folwState == 8){
									return "强制结束"
								}else if(item.folwState == 9){
									return "已完成"
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
							return common.format($('#columnBtns').html(), item.signid, 
									item.signid,isFlowStart,"vm.del('" + item.signid + "')",isFlowStart,
									"vm.startFlow('" + item.signid + "')", isFlowStart,
									"vm.stopFlow('" + item.signid + "')", hideStopButton,
									"vm.restartFlow('" + item.signid + "')", hideRestartButton);
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
							return common.format($('#columnNewBtns').html(), 
									"vm.startNewFlow('" + item.signid + "')", isFlowStart,
									"vm.stopNewFlow('" + item.signid + "')", hideStopButton,
									"vm.restartNewFlow('" + item.signid + "')", hideRestartButton);
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
				alert(vm.model.sugFileDealCount + "--"+isValid);
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
					params : {signid : vm.model.signid}						
				}

			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {											
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
		
		
		//S_初始化待处理页面
		function flowgrid(vm){
			// Begin:dataSource
			var dataSource = common.kendoGridDataSource(rootPath+"/sign/html/initflow");										
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
		}//E_初始化待处理页面
		
		
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