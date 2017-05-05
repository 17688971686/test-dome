(function() {
	'use strict';
	
	angular.module('app').factory('dispatchSvc', dispatch);
	
	dispatch.$inject = ['$http','$state'];

	function sign($http,$state) {
		var service = {
			initEdit : initEdit,						//初始化发文编辑页面
			saveDisPatch : saveDisPatch					//保存发文
		};
		return service;	
		
		//S_初始化发文编辑页面
		function initEdit(){
			
		}//E_初始化发文编辑页面
		
		//S_保存发文
		function saveDisPatch(){
			common.initJqValidation($("#dispatch_form"));
			var isValid = $("#dispatch_form").valid();
			if (isValid) {
				vm.commitProcess = true;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/dispatch",
						data : vm.dispatch
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
		}//E_保存发文
	}
});