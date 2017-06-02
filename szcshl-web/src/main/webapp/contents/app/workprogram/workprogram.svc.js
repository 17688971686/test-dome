(function() {
	'use strict';
	
	angular.module('app').factory('workprogramSvc', workprogram);
	
	workprogram.$inject = ['$rootScope','$http','$state'];
	function workprogram($rootScope,$http,$state) {
		var url_user = rootPath + "/user";
		var url_company = rootPath + "/company";
		var url_work = rootPath + "/workprogram";
		var service = {
			initPage : initPage,		//初始化页面参数
			createWP : createWP,		//新增操作
			findCompanys : findCompanys,		//查找主管部门
			findUsersByOrgId : findUsersByOrgId,//查询评估部门
			selectExpert:selectExpert,	//选择专家
			findAllMeeting:findAllMeeting,//查找会议室地点
			saveRoom:saveRoom,//添加会议预定
			addTimeStage:addTimeStage,//添加会议预定弹窗
			ministerSugges:ministerSugges,//部长处理意见弹窗
			findAllUsers:findAllUsers,//查询所有用户
			gotoProjcet:gotoProjcet,//项目关联
			waitProjects:waitProjects,//待选项目列表
			selectedProject:selectedProject,//已选项目列表
			selectworkProject:selectworkProject,//选择项目
			cancelworkProject:cancelworkProject,//取消项目
			mergeAddWork:mergeAddWork,//保存合并评审
			
			
			
		};
		return service;
		
		//S_保存合并评审
		function mergeAddWork(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/workprogram/workAddDispa",
					params : {signId : vm.dispatchDoc.signId,linkSignId : vm.linkSignId}						
				}
			
			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function(){		
						window.parent.$("#stageWorkWindow").data("kendoWindow").close();
						common.alert({
							vm:vm,
							msg:"操作成功,请继续处理流程！!!",
							fn:function() {
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
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
		//E_保存合并评审
		
		//S_已选项目列表
		function selectedProject(vm){
			
			var httpOptions = {
					method : 'post',
					url : rootPath+"/workprogram/selectedProject",
					params:{
						linkSignIds:vm.linkSignId
					}
				}
			var httpSuccess = function success(response) {
				vm.selectedSign=response.data;
			} 
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		//E_已选项目列表
		
		//S_选择取消
		function cancelworkProject(vm){
		
			var idStr=vm.linkSignId;
			var linkSignId=$("input[name='checkcancel']:checked");
			if(linkSignId){
				$.each(linkSignId, function(i, obj) {
					if(idStr.lastIndexOf(obj.value)==0){
						idStr=idStr.replace(obj.value,"");
					}else{
						idStr=idStr.replace(","+obj.value,"");
					}
				});
				vm.linkSignId=idStr
				selectedProject(vm);
				waitProjects(vm);
			}
		}
		//S_选择取消
		
		//S_选择项目
		function selectworkProject(vm){
			var idStr=vm.linkSignId;
			var linkSignId=$("input[name='checkwork']:checked");
			var ids=[];
			if(linkSignId){
				 $.each(linkSignId, function(i, obj) {
					ids.push(obj.value);
				 });
				 if(idStr){
					 idStr+=","+ids.join(',');
				 }else{
					 idStr=ids.join(',');
				 }
				 vm.linkSignId=idStr;
				 selectedProject(vm);
				 waitProjects(vm);
			}
		}
		
		//S_待选项目列表
		function waitProjects(vm){
		
			var httpOptions = {
	                method: 'post',
	                url: common.format(url_work + "/waitProjects")
	            }
	            var httpSuccess = function success(response) {
	                vm.signs = {};
	                vm.signs = response.data;
	            }

	            common.http({
	                vm: vm,
	                $http: $http,
	                httpOptions: httpOptions,
	                success: httpSuccess
	            });
		}
		//E_待选项目列表

		
		//S_关联项目
		function gotoProjcet(vm) {
			/*if(!vm.dispatchDoc.id){
				common.alert({
					vm:vm,
					msg:"请先保存再进行关联！",
					fn:function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
					}
				})	
				return;
			}*/
			var WorkeWindow = $("#stageWorkWindow");
			WorkeWindow.kendoWindow({
				width : "1200px",
				height : "630px",
				title : "合并评审",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
			getSeleSignBysId(vm);
			getsign(vm);
			
		}
		//S_关联项目弹窗
		
		//S_查询所有用户
		function findAllUsers(vm){
			var httpOptions = {
	                method: 'get',
	                url: common.format(rootPath + "/user/findAllUsers")
	            }
	            var httpSuccess = function success(response) {
	                vm.users = {};
	                vm.users = response.data;
	            }
	            common.http({
	                vm: vm,
	                $http: $http,
	                httpOptions: httpOptions,
	                success: httpSuccess
	            });
		}
		//E_查询所有用户
		
	
		
		//S_会议预定添加弹窗
		function addTimeStage(vm){
			var WorkeWindow = $("#stageWindow");
			// WorkeWindow.show();
			WorkeWindow.kendoWindow({
				width : "660px",
				height : "550px",
				title : "会议预定添加",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
		}
		//E_会议预定添加弹窗
		
		//S_部长处理意见弹窗
		function ministerSugges(vm){
			
			//var ministerWindow = $("#ministerSug");
			// WorkeWindow.show();
			$("#ministerSug").kendoWindow({
				width : "700px",
				height : "400px",
				title : "部长处理意见",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
		}
		//E_部长处理意见弹窗
		
		// 清空页面数据
		// begin#cleanValue
		function cleanValue() {
			var tab = $("#stageWindow").find('input');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
		}
		
		//S_会议预定添加
		function saveRoom(vm){
		
			common.initJqValidation($('#stageForm'));
			var isValid = $('#stageForm').valid();
			if (isValid) {
				vm.model.workProgramId = vm.work.id;
				vm.model.stageOrg = vm.work.reviewOrgName;
				var httpOptions = {
					method : 'post',
					url : rootPath + "/room/saveRoom",
					data : vm.model
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							cleanValue();
							window.parent.$("#stageWindow").data("kendoWindow").close();
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.showWorkHistory = true;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
								}
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

			}
			
		}
		//E_会议预定添加
		
		//S_查找所有会议室地点
		function findAllMeeting(vm){
			var httpOptions = {
	                method: 'get',
	                url: common.format(rootPath + "/room/meeting")
	            }
	            var httpSuccess = function success(response) {
	                vm.meetings = {};
	                vm.meetings = response.data;
	            }
	            common.http({
	                vm: vm,
	                $http: $http,
	                httpOptions: httpOptions,
	                success: httpSuccess
	            });
		}
		//E_查找所有会议室地点
		
		//start 查找主管部门
		function findCompanys(vm){
			var httpOptions = {
                method: 'get',
                url: common.format(url_company + "/findCcompanys")
            }
            var httpSuccess = function success(response) {
                vm.companys = {};
                vm.companys = response.data;
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
		}
		//end 查找主管部门
		//S_根据部门ID选择用户
		function findUsersByOrgId(vm,type){
			var param = {};
			if("main" == type){
				param.orgId = vm.work.reviewDept;
			}
			var httpOptions = {
					method : 'get',
					url : rootPath+"/user/findUsersByOrgId",
					params:param					
				};
				
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {		
						if("main" == type){
							vm.mainUserList = {};
							vm.mainUserList = response.data;
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
		//E_根据部门ID选择用户
		
		//S_初始化页面参数
		function initPage(vm){
		
			var httpOptions = {
					method : 'get',
					url : rootPath+"/workprogram/html/initWorkBySignId",
					params : {signId:vm.work.signId}
				}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {		
						if(response.data != null && response.data != ""){
							vm.work = response.data;
							vm.work.signId = $state.params.signid
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
		}//S_初始化页面参数	
		
		
		//S_保存操作
		function createWP(vm){
			common.initJqValidation($("#work_program_form"));			
			var isValid = $("#work_program_form").valid();
			if(isValid){
				vm.iscommit = true;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/workprogram/addWork",
						data : vm.work
					}
				var httpSuccess = function success(response) {	
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {		
							vm.iscommit = false;
							vm.work.id = response.data.id;
							common.alert({
								vm:vm,
								msg:"操作成功！",
								closeDialog:true
							})								
						}						
					});
				}
				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess,
					onError: function(response){vm.iscommit = false;}
				});			
			}			
		}//E_保存操作
		
		//S_selectExpert
		function selectExpert(vm){
			if(vm.work.id && vm.work.id != ''){
				$state.go('expertReviewEdit', { workProgramId:vm.work.id});
			}else{
				common.alert({
					vm:vm,
					msg:"请先保存，再继续执行操作！"					
				})
			}
		}//E_selectExpert
		
	}		
})();