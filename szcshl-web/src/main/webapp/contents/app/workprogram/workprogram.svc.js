(function() {
	'use strict';
	
	angular.module('app').factory('workprogramSvc', workprogram);
	
	workprogram.$inject = ['$rootScope','$http','$state'];
	function workprogram($rootScope,$http,$state) {
		var url_user = rootPath + "/user";
		var service = {
			initPage : initPage,		//初始化页面参数
			createWP : createWP	,		//新增操作
			findOrgs : findOrgs,		//查找主管部门
			findUsersByOrgId : findUsersByOrgId,//查询评估部门
		};
		return service;	
		
		//start 查找主管部门
		function findOrgs(vm){
			 var httpOptions = {
		                method: 'get',
		                url: common.format(url_user + "/getOrg")
		            }
		            var httpSuccess = function success(response) {
		                vm.orgs = {};
		                vm.orgs = response.data;
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
							console.log(vm.mainUserList);
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
			//时间控件start
			$(document).ready(function () {
			  	 kendo.culture("zh-CN");
			      $("#studyBeginTime").kendoDatePicker({
			      	 format: "yyyy-MM-dd",
			      });
			  }); 
			$(document).ready(function () {
			 	 kendo.culture("zh-CN");
			     $("#studyEndTime").kendoDatePicker({
			     	 format: "yyyy-MM-dd",
			     });
			 }); 
			$(document).ready(function () {
			 	 kendo.culture("zh-CN");
			     $("#suppLetterDate").kendoDatePicker({
			     	 format: "yyyy-MM-dd",
			     });
			 }); 
			
			//时间控件end
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
			
			vm.work.studyBeginTime = $("#studyBeginTime").val();
			vm.work.studyEndTime = $("#studyEndTime").val();
			vm.work.suppLetterDate = $("#suppLetterDate").val();
			common.initJqValidation($("#work_program_form"));
			var isValid = $("#work_program_form").valid();
			
				vm.commitProcess = true;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/workprogram",
						data : vm.work
					}
				var httpSuccess = function success(response) {	
					console.log(response);
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {		
							common.alert({
								vm:vm,
								msg:"操作成功,请继续处理流程！",
								fn:function() {
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									$rootScope.back();	//返回到流程页面
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
			//}
		}//E_保存操作
	}	
	
})();