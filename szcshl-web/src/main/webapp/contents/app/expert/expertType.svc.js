(function() {
	'expertType strict';

	angular.module('app').factory('expertTypeSvc', expertType);

	expertType.$inject = [ '$http','expertSvc' ,'$rootScope'];

	function expertType($http,expertSvc,$rootScope) {
		var service = {
			gotoExpertType : gotoExpertType,
			createExpertType : createExpertType,	//添加专家类型
			cleanValue : cleanValue,
			getExpertType : getExpertType,	//通过专家id获取专家类型
			updateExpertType : updateExpertType,	//进入更新专家类型页面
			getExpertTypeById : getExpertTypeById,	//	通过专家类型ID获取专家类型
			saveUpdate : saveUpdate,	//保存更新数据
			deleteExpertType : deleteExpertType	//删除专家类型
			
			

		};

		return service;
		
		// 清空页面数据
		// begin#cleanValue
		function cleanValue() {
			var tab = $("#addExpertType").find('input');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
		}
		
		//begin getExpertTypeByExpertId
		function getExpertType(vm){
			var httpOptions = {
				method : 'GET',
				url : rootPath + "/expertType/getExpertType?$filter=expert.expertID eq '"+vm.model.expertID+"'"
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.expertTypeList = response.data;
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
		
		//begin gotoExpertType
		function gotoExpertType(vm){
			var WorkeWindow = $("#addExpertType");
			WorkeWindow.kendoWindow({
				width : "690px",
				height : "400px",
				title : "添加专家类型",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
		}//end gotoExpertType
		
		
		//begin getExpertTypeById
		function getExpertTypeById(vm){
			var httpOptions={
				method : "get",
				url : rootPath +"/expertType/getExpertTypeById",
				params:{expertTypeId: vm.expertTypeId}
			}
			
			var httpSuccess=function success(response){			
				vm.expertType=response.data;			
				vm.expertType.majobSmallDicts = $rootScope.topSelectChange(vm.expertType.maJorBig,$rootScope.DICT.MAJOR.dicts)
			}
			
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});

		}
		//end getExpertTypeById
		
		//begin createExpertType
		function createExpertType(vm,callBack){
			common.initJqValidation($('#expertTypeForm'));
			var isValid = $('#expertTypeForm').valid();
			if (isValid) {
				
				var httpOptions = {
					method : 'post',
					url : rootPath + "/expertType",
					data : vm.expertType
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
                                    window.parent.$("#addExpertType").data("kendoWindow").close();
                                    cleanValue();
                                    // getExpertType(vm);
                                    expertSvc.getExpertById(vm);
                                    // vm.showExpertType = false;
									vm.projectTypeList = true;
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
		
		}//end createExpertType
		
		//begin updateExpertType
		function updateExpertType(vm){
			var isCheck=$("input[name='checkEType']:checked");
			if(isCheck.length<1){
				common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				});
			}else if(isCheck.length>1){
				common.alert({
					vm : vm,
					msg : "无法同时操作多条数据",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				});
			}else{
				vm.expertTypeId=isCheck.val();
				getExpertTypeById(vm);
				vm.expertID = vm.model.expertID;
				gotoExpertType(vm);
			}
			
		}//end
		
		//begin
		function saveUpdate(vm){
			common.initJqValidation($('#expertTypeForm'));
			var isValid = $('#expertTypeForm').valid();
			if (isValid) {
//			vm.model.id=vm.id;
			vm.expertType.expertID = vm.expertID;
			var httpOptions={
				method : "put",
				url : rootPath + "/expertType",
				data : vm.expertType
			}
			var httpSuccess=function success(response){
				
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						window.parent.$("#addExpertType").data("kendoWindow").close();
//						getExpertType(vm);
						expertSvc.getExpertById(vm);
						cleanValue();
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
	}//end
	
	function deleteExpertType(vm){
		var isCheck=$("input[name='checkEType']:checked");
		if(isCheck.length<1){
			common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				});
		
		}else{
			var ids="";
			$.each(isCheck,function(i,obj){
				ids += obj.value+",";
			});
			
			vm.isSubmit=true;
			var httpOptions={
				method :"delete",
				url : rootPath + "/expertType",
				data : ids
			}
		var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.isSubmit = false;
							common.alert({
							vm : vm,
							msg : "操作成功",
							fn : function() {
								vm.isSubmit = false;
								$('.alertDialog').modal('hide');
							}
						});
						expertSvc.getExpertById(vm);
//							getExpertType(vm);
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
	
	}

})();