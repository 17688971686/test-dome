(function() {
	'workExpe strict';

	angular.module('app').factory('workExpeSvc', workExpe);

	workExpe.$inject = [ '$http','expertSvc' ];

	function workExpe($http,expertSvc) {
		var service = {
            saveWork : saveWork,
			updateWork : updateWork,
			deleteWork : deleteWork,
			updateWorkPage : updateWorkPage,
			getWorkById : getWorkById,
			getWork : getWork,
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

		// begin#根据ID删除工作简历信息
		function deleteWork(ids,callBack) {
            var httpOptions = {
                method : 'delete',
                url : rootPath + "/workExpe/deleteWork",
                params : {
                    ids : ids,
				}
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
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

		// begin#保存专家工作经历
		function saveWork(work,callBack) {
            var httpOptions = {
                method : 'post',
                url : rootPath + "/workExpe/saveWorkExpe",
                data : work
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
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