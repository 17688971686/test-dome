(function() {
	'use strict';
	
	angular.module('app').factory('fileRecordSvc', fileRecord);
	
	fileRecord.$inject = ['$rootScope','$http'];

	function fileRecord($rootScope,$http) {
		var service = {
			initFileRecordData : initFileRecordData,		//初始化流程数据	
			saveFileRecord : saveFileRecord,				//保存
			initfileRecordUpload:initfileRecordUpload,		//初始化上传附件
			delfileSysFile:delfileSysFile,					//删除系统文件
			fileDownload:fileDownload,						//文件下载
		};
		return service;	
		
		//S 文件下载
		function fileDownload(vm,fileId){
			var sysfileId = fileId;
			window.open(rootPath+"/file/fileDownload?sysfileId="+fileId);
		}
		//S 文件下载
		
		//S 删除系统文件
		function delfileSysFile(vm,id){
			var httpOptions = {
					method : 'delete',
					url : rootPath+"/file/deleteSysFile",
					data : id

				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn:function(){		
							window.parent.$("#filequeryWin").data("kendoWindow").close();
							common.alert({
								vm:vm,
								msg:"删除成功",
								fn:function() {
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
		//E 删除系统文件
		
		//S 初始化上传附件
		function initfileRecordUpload(vm){
			var businessId = vm.fileRecord.signId;
            var projectfileoptions = {
                language : 'zh',
                allowedPreviewTypes : ['image'],
                allowedFileExtensions : [ 'jpg', 'png', 'gif',"xlsx","docx" ,"pdf","doc","xls"],
                maxFileSize : 2000,
                showRemove: false,
                uploadUrl:rootPath + "/file/fileUpload",
                uploadExtraData:{businessId:businessId}
            };
            $("#fileRecordphotofile").fileinput(projectfileoptions).on("filebatchselected", function(event, files){

            }).on("fileuploaded", function(event, data) {
                $("#fileRecordPhotoSrc").removeAttr("src");
                $("#fileRecordPhotoSrc").attr("src",rootPath+"/sign/transportImg?signid="+businessId+"&t="+Math.random());
            });
		}
		//E 初始化上传附件
		
		//S_初始化
		function initFileRecordData(vm){				
			var httpOptions = {
					method : 'get',
					url : rootPath+"/fileRecord/html/initFillPage",
					params : {signId:vm.fileRecord.signId}
				}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {		
						if(response.data != null && response.data != ""){
							vm.fileRecord = response.data.file_record;	
							vm.fileRecord.signId = vm.signId;
							vm.signUserList = response.data.sign_user_List;	
							vm.sysFiles = response.data.sysFilelist;
						}	
						initfileRecordUpload(vm);
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
				if(vm.signUser){
					vm.fileRecord.signUserid = vm.signUser.id;
					vm.fileRecord.signUserName = vm.signUser.displayName;
				}				
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
							vm.saveProcess = false;
							common.alert({
								vm:vm,
								msg:"操作成功！",	
							})								
						}						
					});
				}
				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess,
					onError: function(response){vm.saveProcess = false;}
				});
			}
		}//E_保存
		
	}
})();