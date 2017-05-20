(function() {
	'use strict';
	
	angular.module('app').factory('workprogramSvc', workprogram);
	
	workprogram.$inject = ['$rootScope','$http','$state'];
	function workprogram($rootScope,$http,$state) {
		var url_user = rootPath + "/user";
		var service = {
			initPage : initPage,		//初始化页面参数
			createWP : createWP,		//新增操作
			findOrgs : findOrgs,		//查找主管部门
			findUsersByOrgId : findUsersByOrgId,//查询评估部门
			selectExpert:selectExpert	//选择专家
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
			kendo.culture("zh-CN");
		    $("#studyBeginTime").kendoDatePicker({
		    	format: "yyyy-MM-dd",
		    });
		    $("#studyEndTime").kendoDatePicker({
		     	format: "yyyy-MM-dd",
		    });
		    $("#suppLetterDate").kendoDatePicker({
		     	format: "yyyy-MM-dd",
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
			vm.work.studyBeginTime = $("#studyBeginTime").val();
			vm.work.studyEndTime = $("#studyEndTime").val();
			vm.work.suppLetterDate = $("#suppLetterDate").val();
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