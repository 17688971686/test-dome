(function() {
	'expertGlory strict';

	angular.module('app').factory('expertGlorySvc', expertGlory);

	expertGlory.$inject = [ '$http' ];

	function expertGlory($http) {
		var service = {
			deleteGlory : deleteGlory,
			gotoGPage : gotoGPage,      
			getGloryById : getGloryById,
			getGlory : getGlory,
			updatePage : updatePage,
			updateGlory :updateGlory,
			createPage : createPage,
			saveGlory : saveGlory,
			cleanValue : cleanValue
			
			

		};

		return service;
		// begin#updatePage
		function updatePage(vm){
			vm.isUpdateGlory=true;
			var isCheck = $("input[name='checkgl']:checked");
			if (isCheck.length < 1) {
				common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			} else if (isCheck.length > 1) {
				common.alert({
					vm : vm,
					msg : "无法同时操作多条数据",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			} else {				
				vm.gID = isCheck.val();
				getGloryById(vm);
				gotoGPage(vm);
				vm.expertID = vm.model.expertID;

			}
		
		}
		
		// begin#createPage
		function createPage(vm){
			vm.isSaveGlory=true;
			gotoGPage(vm);
		}
		
		// begin#saveGlory
		function saveGlory(vm) {
			common.initJqValidation($('#gloryForm'));
			//var isValid = $('#gloryForm').valid();
			//if (isValid) {
				vm.model.beginTime = $('#gGetCertifiDate').val();
				vm.model.endTime = $('#gExpiryDateTo').val();
				
				var httpOptions = {
					method : 'put',
					url : rootPath + "/expertGlory",
					data : vm.model
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							cleanValue();
							window.parent.$("#grwindow").data("kendoWindow").close();
							vm.isSaveGlory=false;
							getGlory(vm);
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									
									vm.showExpertGlory = true;
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

			//}
		}
		
		// begin#getGlory
		function getGlory(vm) {
			var httpOptions = {
				method : 'GET',
				url : rootPath + "/workExpe?$filter=expert.expertID eq '" + vm.model.expertID + "'"
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.glory= response.data;
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

		// begin#deleteGlory
		function deleteGlory(vm) {
			var isCheck = $("input[name='checkgl']:checked");
			if (isCheck.length < 1) {
				common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			} else {
				var ids = "";
				$.each(isCheck, function(i, obj) {
					ids += obj.value + ",";
				});

				vm.isSubmit = true;
				var httpOptions = {
					method : 'delete',
					url : rootPath + "/expertGlory/deleteGlory",
					data : ids
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.isSubmit = false;
							getGlory(vm);
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
		// end#delertWork

		// begin#updateGlory
		/*function updateGlory(vm) {
			var isCheck = $("input[name='checkgl']:checked");
			if (isCheck.length < 1) {
				common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			} else if (isCheck.length > 1) {
				common.alert({
					vm : vm,
					msg : "无法同时操作多条数据",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			} else {				
				vm.gID = isCheck.val();
				getGloryById(vm);
				gotoGPage(vm);
				vm.expertID = vm.model.expertID;

			}
		}
*/
		// begin#getGloryById
		function getGloryById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(rootPath + "/expertGlory?$filter=gID eq '{0}'", vm.gID)
			}
			var httpSuccess = function success(response) {
				// vm.model = response.data[0];
				vm.model.gQualifications = response.data[0].gQualifications;
				vm.model.gTitle = response.data[0].gTitle;
				vm.model.gExpiryDate = response.data[0].gExpiryDate;
				vm.model.gFileNum = response.data[0].gFileNum;
				vm.model.isGetCertificate = response.data[0].isGetCertificate;
				vm.model.isNoUsefull = response.data[0].isNoUsefull;
				vm.model.gRemark = response.data[0].gRemark;
				$('#gGetCertifiDate').val(response.data[0].gGetCertifiDate);
				$('#gExpiryDateTo').val(response.data[0].gExpiryDateTo);
				if (vm.isUpdate) {
				}
			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});

		}

		// 清空页面数据
		// begin#cleanValue
		function cleanValue() {
			var tab = $("#grwindow").find('input');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
		}

		// begin#gotoWPage
		function gotoGPage(vm) {
			var WorkeWindow = $("#grwindow");
			// WorkeWindow.show();
			WorkeWindow.kendoWindow({
				width : "1000px",
				height : "600px",
				title : "专家聘书",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
		}
		// end#gotoWPage

		// begin#getGlory
		function getGlory(vm) {
			var httpOptions = {
				method : 'GET',
				url : rootPath+ "/expertGlory?$filter=expert.expertID eq '"+ vm.model.expertID + "'"
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.glory = response.data;
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

		// begin#updateGlory
		function updateGlory(vm) {
			common.initJqValidation();
			//var isValid = $('form').valid();
			//if (isValid) {
				vm.model.gID = vm.gID;
				vm.model.expertID = vm.expertID;
				//vm.model.beginTime = $('#beginTime').val();
				//vm.model.endTime = $('#endTime').val();

				var httpOptions = {
					method : 'put',
					url : rootPath + "/expertGlory/updateGlory",
					data : vm.model
				}

				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							getGlory(vm);
							cleanValue();
							window.parent.$("#grwindow").data("kendoWindow").close();
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.showGloryHistory = true;
									vm.isUpdateGlory=false;
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
			//}
		}
	}

})();