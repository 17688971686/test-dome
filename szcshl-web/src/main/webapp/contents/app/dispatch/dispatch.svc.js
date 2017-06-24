(function() {
	'dispatch strict';

	angular.module('app').factory('dispatchSvc', dispatch);

	dispatch.$inject = ['sysfileSvc', '$http'];

	function dispatch(sysfileSvc, $http) {
		var service = {
			initDispatchData : initDispatchData, // 初始化流程数据
			saveDispatch : saveDispatch, // 保存
			gotoMergePage : gotoMergePage, // 打开合并发文页面
			chooseProject : chooseProject, // 选择待选项目
			getsign : getsign, // 显示待选项目
			cancelProject : cancelProject, // 取消选择
			mergeDispa : mergeDispa, // 合并发文
			fileNum : fileNum, // 生成文件字号
			getselectedSign : getselectedSign,
			getSeleSignBysId : getSeleSignBysId,
			deletemerge : deletemerge, // 删除关联信息
			getRelatedFileNum : getRelatedFileNum, // 获取关联文件字号
		
		};
		return service;


		function fileNum(vm) {
			vm.isSubmit = true;
			if (!vm.dispatchDoc.id) {
				common.alert({
							vm : vm,
							msg : "请先保存再生成文件字号",
							fn : function() {
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
							}
						})
				return;
			}

			var httpOptions = {
				method : 'post',
				url : rootPath + "/dispatch/fileNum",
				params : {
					dispaId : vm.dispatchDoc.id
				}
			}

			var httpSuccess = function success(response) {
				common.requestSuccess({
							vm : vm,
							response : response,
							fn : function() {
								vm.showFileNum = false;
								vm.isSubmit = false;
								vm.dispatchDoc.fileNum = response.data;
								common.alert({
											vm : vm,
											msg : "操作成功"
										})
							}

						});
			}

			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess,
						onError : function(response) {
							vm.isSubmit = false;
						}
					});
		}

		// begin#gotoWPage
		function gotoMergePage(vm) {
			if (!vm.dispatchDoc.id) {
				common.alert({
							vm : vm,
							msg : "请先保存再进行关联！",
							fn : function() {
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
							}
						})
			}else{
				vm.mwindowHide = false;
				var WorkeWindow = $("#mwindow");
				WorkeWindow.kendoWindow({
							width : "1200px",
							height : "630px",
							title : "合并发文",
							visible : false,
							modal : true,
							closable : true,
							actions : ["Pin", "Minimize", "Maximize", "Close"]
						}).data("kendoWindow").center().open();
	
				getSeleSignBysId(vm);
				getsign(vm);
			}

		}
		// end#gotoWPage

		// S_初始化
		function initDispatchData(vm) {
			vm.isEdit=false;
			if (vm.dispatchDoc.id) {
				getRelatedFileNum(vm);
			}
			var httpOptions = {
				method : 'get',
				url : rootPath + "/dispatch/initData",
				params : {
					signid : vm.dispatchDoc.signId
				}
			}

			var httpSuccess = function success(response) {
				common.requestSuccess({
							vm : vm,
							response : response,
							fn : function() {
							    var data = response.data;
							    vm.sign = data.sign;
								vm.dispatchDoc = data.dispatch;
								vm.associateDispatchs = data.associateDispatchs;
								vm.proofread = data.mainUserList;
								vm.org = data.orgList;
								vm.showCreate = true;
								// 初始化获取合并发文关联的linkSignId
								vm.linkSignId = response.data.linkSignId;
								vm.mergeDispaId=response.data.businessId;
								if (vm.dispatchDoc.id && !vm.dispatchDoc.fileNum) {
									vm.showFileNum = true;
								}
                                //初始化附件上传
								if(vm.dispatchDoc.id){
                                sysfileSvc.initUploadOptions({
                                    businessId:vm.dispatchDoc.id,
                                    sysSignId :vm.dispatchDoc.signId,
                                    sysfileType:"发文",
                                    uploadBt:"upload_file_bt",
                                    detailBt:"detail_file_bt",
                                    vm:vm
                                });
								}
							}
						})
			}
			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess
					});

		}// E_初始化

		function mergeDispa(vm) {
			if (!vm.linkSignId && vm.mergeDispaId) {
				deletemerge(vm);
				vm.dispatchDoc.isRelated = "否";
				window.parent.$("#mwindow").data("kendoWindow").close();
				common.alert({
							vm : vm,
							msg : "操作成功！",
							fn : function() {
								$('.alertDialog')
										.modal('hide');
								$('.modal-backdrop')
										.remove();
							}
						})
			vm.isnotEdit=false;		
			} else if(!vm.linkSignId && !vm.mergeDispaId){
				
				vm.dispatchDoc.isRelated = "否";
				vm.isnotEdit=false;	
				window.parent.$("#mwindow").data("kendoWindow").close();
			}else {
				vm.isnotEdit=false;	
				//vm.message = "";
				vm.dispatchDoc.isRelated = "是";
				var httpOptions = {
					method : 'get',
					url : rootPath + "/dispatch/mergeDispa",
					params : {
						signId : vm.dispatchDoc.signId,
						linkSignId : vm.linkSignId
					}
				}

				var httpSuccess = function success(response) {
					common.requestSuccess({
								vm : vm,
								response : response,
								fn : function() {
									window.parent.$("#mwindow")
											.data("kendoWindow").close();
									common.alert({
												vm : vm,
												msg : "操作成功！",
												fn : function() {
													$('.alertDialog')
															.modal('hide');
													$('.modal-backdrop')
															.remove();
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

		// S_保存
		function saveDispatch(vm) {
			/*common.initJqValidation($("#dispatch_form"));
			var isValid = $("#dispatch_form").valid();
			if (isValid) {*/
				// 是否关联其它项目判断
				if (vm.dispatchDoc.isMainProject == "9" && vm.dispatchDoc.id) {
					if (!vm.linkSignId) {
						common.alert({
									vm : vm,
									msg : "请关联其它项目",
									fn : function() {
										$('.alertDialog').modal('hide');
										$('.modal-backdrop').remove();
									}
								})
						return;
					}

				}
				var httpOptions = {
					method : 'post',
					url : rootPath + "/dispatch",
					data : vm.dispatchDoc
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
								vm : vm,
								response : response,
								fn : function() {
									common.alert({
												vm : vm,
												msg : "操作成功！",
												fn : function() {
													$('.alertDialog')
															.modal('hide');
													$('.modal-backdrop')
															.remove();
													// vm.showFileNum=true;

													// 初始化数据获得保存后的数据
													initDispatchData(vm);
													// $rootScope.back();
													// //返回到流程页面
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
						// onError: function(response){vm.saveProcess = false;}
					});

			//}
		}// E_保存

		// begin##chooseProject
		function chooseProject(vm) {
			var idStr = vm.linkSignId;
			var linkSignId = $("input[name='checksign']:checked");
			var ids = [];
			if (linkSignId.length != 0) {
				$.each(linkSignId, function(i, obj) {
							ids.push(obj.value);
						});
				if (idStr) {
					idStr += "," + ids.join(',');
				} else {
					idStr = ids.join(',');
				}
				vm.linkSignId = idStr;
				getselectedSign(vm);
				getsign(vm);
			}
		}
		// end##chooseProject

		// begin##chooseProject
		function cancelProject(vm) {
			var idStr = vm.linkSignId;
			var linkSignId = $("input[name='checkss']:checked");
			if (linkSignId.lenght != 0) {
				$.each(linkSignId, function(i, obj) {
							if (idStr.lastIndexOf(obj.value) == 0) {
								idStr = idStr.replace(obj.value, "");
							} else {
								idStr = idStr.replace("," + obj.value, "");
							}
						});
				vm.linkSignId = idStr
				getselectedSign(vm);
				getsign(vm);
			}
		}
		// end##chooseProject

		// begin##getSeleSignBysId
		function getSeleSignBysId(vm) {
			var httpOptions = {
				method : 'get',
				url : rootPath + "/dispatch/getSignByDId",
				params : {
					bussnessId : vm.dispatchDoc.id
				}
			}
			var httpSuccess = function success(response) {
				vm.selectedSign = response.data.signDtoList;
				vm.linkSignId = response.data.linkSignId;
			}
			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess
					});
		}
		// end##getSeleSignBysId

		// 获取已选项目
		// begin##getselectedSign
		function getselectedSign(vm) {

			var httpOptions = {
				method : 'get',
				url : rootPath + "/dispatch/getSelectedSign",
				params : {
					linkSignIds : vm.linkSignId
				}
			}
			var httpSuccess = function success(response) {
				vm.selectedSign = response.data;
			}
			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess
					});
		}

		// begin##getsign
		function getsign(vm) {
			vm.sign.signid = vm.linkSignId;
			var httpOptions = {
				method : 'post',
				url : rootPath + "/dispatch/getSign",
				data : vm.sign
			}
			var httpSuccess = function success(response) {
				vm.signs = response.data;
			}
			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess,
						onError : function(response) {
						}
					});

		}// end##getsign

		// begin##deletemerge
		function deletemerge(vm) {
			var httpOptions = {
				method : 'post',
				url : rootPath + "/dispatch/deleteMerge",
				params : {
					dispatchId : vm.dispatchDoc.id
				}
			}
			var httpSuccess = function success(response) {
				vm.linkSignId="";
			}
			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess,
						onError : function(response) {
						}
					});

		}// end##deletemerge

		function getRelatedFileNum(vm) {
			var httpOptions = {
				method : 'get',
				url : rootPath + "/dispatch/getRelatedFileNum",
				params : {
					dispatchId : vm.dispatchDoc.id
				}
			}
			var httpSuccess = function success(response) {
				vm.dispatchDoc.fileNum = response.data;
			}
			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess,
						onError : function(response) {
						}
					});
		}
		
	}
})();