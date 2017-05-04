(function() {
	'use strict';
	
	angular.module('app').factory('workprogramSvc', workprogram);
	
	workprogram.$inject = ['$rootScope','$http','$state'];

	function workprogram($rootScope,$http,$state) {
		var service = {
			initPage : initPage,		//初始化页面参数
			createWP : createWP			//新增操作
		};
		return service;			
		
		//S_初始化页面参数
		function initPage(vm){
			
		}//S_初始化页面参数	
		
		
		//S_保存操作
		function createWP(vm){
			common.initJqValidation($("#work_program_form"));
			var isValid = $("#work_program_form").valid();
			if (isValid) {
				vm.commitProcess = true;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/workprogram",
						data : vm.work
					}
					var httpSuccess = function success(response) {									
						common.requestSuccess({
							vm:vm,
							response:response,
							fn:function() {		
								common.alert({
									vm:vm,
									msg:"操作成功,请继续处理流程！",
									fn:function() {
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
			}
		}//E_保存操作
	}		
})();