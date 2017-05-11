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
		function initDispatchData(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/dispatch/initData",
					params : {signid : vm.dispatchDoc.signId}						
				}
			
			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {
						vm.dispatchDoc=response.data.dispatch;
						vm.proofread = response.data.mainUserList;
						$("#draftDate").val(vm.dispatchDoc.draftDate);
						$("#dispatchDate").val(vm.dispatchDoc.dispatchDate);
						$("#proofreadId").find("option:selected").text(vm.dispatchDoc.proofreadName);
					}		
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		
		}//E_初始化
		
		//S_保存
		function saveDispatch(vm){
			common.initJqValidation($("#dispatch_form"));
			var isValid = $("#dispatch_form").valid();
			vm.dispatchDoc.proofreadName=$("#proofreadId").find("option:selected").text();
			vm.dispatchDoc.draftDate=$("#draftDate").val();
			vm.dispatchDoc.dispatchDate=$("#dispatchDate").val();
			console.log(vm.dispatchDoc);
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
						fn:function(){		
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
			}
		}//E_保存
		
	}
})();