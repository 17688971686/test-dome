(function() {
	'use strict';
	
	angular.module('app').factory('dispatchSvc', dispatch);
	
	dispatch.$inject = ['$rootScope','$http'];

	function dispatch($rootScope,$http) {
		var service = {
			initDispatchData : initDispatchData,		//初始化流程数据	
			saveDispatch : saveDispatch					//保存
		};
		return service;			
	
		//S_初始化
		function initDispatchData(){
			
		}//E_初始化
		
		//S_保存
		function saveDispatch(vm){
			common.initJqValidation($("#dispatch_form"));
			var isValid = $("#dispatch_form").valid();
			if (isValid) {
				vm.saveProcess = true;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/dispatch",
						data : vm.dispatchDoc
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
		}//E_保存
		
	}
})();