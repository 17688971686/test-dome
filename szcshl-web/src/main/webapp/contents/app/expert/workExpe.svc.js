(function() {
	'workExpe strict';

	angular.module('app').factory('workExpeSvc', workExpe);

	workExpe.$inject = [ '$http','expertSvc' ];

	function workExpe($http,expertSvc) {
		var service = {
			createWork : createWork,
			updateWork : updateWork,
			deleteWork : deleteWork,
			updateWorkPage : updateWorkPage,
			gotoWPage : gotoWPage,
			getWorkById : getWorkById,
			getWork : getWork,
			cleanValue : cleanValue

		};

		return service;
		// begin#getWork
		function getWork(vm) {
			var httpOptions = {
				method : 'GET',
				url : rootPath + "/workExpe/getWork?$filter=expert.expertID eq '" + vm.model.expertID + "'"
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.workList = response.data;
//						console.log(vm.work);
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

		// begin#deleteWork
		function deleteWork(vm) {
			var isCheck = $("input[name='checkwr']:checked");
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
					url : rootPath + "/workExpe/deleteWork",
					data : ids
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.isSubmit = false;
//							expertSvc.getExpertById(vm);
							getWork(vm);
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

		// begin#updateWork
		function updateWorkPage(vm) {
			var isCheck = $("input[name='checkwr']:checked");
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
				vm.weID = isCheck.val();
				getWorkById(vm);
				gotoWPage(vm);
				vm.expertID = vm.model.expertID;

			}
		}

		// begin#getWorkById
		function getWorkById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(rootPath + "/workExpe/getWork?$filter=weID eq '{0}'", vm.weID)
			}
			var httpSuccess = function success(response) {
				vm.work={};
				vm.work.companyName = response.data[0].companyName;
				vm.work.workJob = response.data[0].workJob;
				vm.work.beginTime = response.data[0].beginTime;
				vm.work.endTime = response.data[0].endTime;
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
			var tab = $("#wrwindow").find('input');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
		}

		// begin#gotoWPage
		function gotoWPage(vm) {
			var WorkeWindow = $("#wrwindow");
			WorkeWindow.kendoWindow({
				width : "690px",
				height : "330px",
				title : "添加工作简历",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
		}
		// end#gotoWPage

		// begin#getWork
		/*function getWork(vm) {
			var httpOptions = {
				method : 'GET',
				url : rootPath+ "/workExpe/getWork?$filter=expert.expertID eq '"+ vm.model.expertID + "'"
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.work = response.data;
					}

				});
			}

			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});

		}*/
		// begin#createWork
		function createWork(vm) {
			common.initJqValidation($('#workForm'));
			var isValid = $('#workForm').valid();
			if (isValid) {
				vm.work.beginTime = $('#beginTime').val();
				vm.work.endTime = $('#endTime').val();
				
				var httpOptions = {
					method : 'post',
					url : rootPath + "/workExpe/workExpe",
					data : vm.work
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							cleanValue();
							window.parent.$("#wrwindow").data("kendoWindow").close();
							getWork(vm);
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.showWorkHistory = true;
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
		}

		// begin#updateWork
		function updateWork(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
				vm.work.weID = vm.weID;
				vm.work.expertID = vm.expertID;
				vm.work.beginTime = $('#beginTime').val();
				vm.work.endTime = $('#endTime').val();

				var httpOptions = {
					method : 'put',
					url : rootPath + "/workExpe/updateWork",
					data : vm.work
				}

				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							window.parent.$("#wrwindow").data("kendoWindow").close();
							getWork(vm);
							cleanValue();
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.showWorkHistory = true;
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
		}
	}

})();