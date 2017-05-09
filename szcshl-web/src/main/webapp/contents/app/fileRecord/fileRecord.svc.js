(function() {
	'use strict';
	
	angular.module('app').factory('fileRecordSvc', fileRecord);
	
	fileRecord.$inject = ['$rootScope','$http'];

	function fileRecord($rootScope,$http) {
		var service = {
			initFileRecordData : initFileRecordData,		//初始化流程数据	
			saveFileRecord : saveFileRecord					//保存
		};
		return service;			
	
		//S_初始化
		function initFileRecordData(vm){			
			var httpOptions = {
					method : 'get',
					url : rootPath+"/fileRecord/html/initBySignId",
					params : {signId:vm.fileRecord.signId}
				}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {		
						if(response.data != null && response.data != ""){
							vm.fileRecord = response.data;								
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
		}//E_初始化
		
		//S_保存
		function saveFileRecord(vm){
			common.initJqValidation($("#fileRecord_form"));
			var isValid = $("#fileRecord_form").valid();
			if (isValid) {
				vm.saveProcess = true;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/fileRecord",
						data : vm.fileRecord
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