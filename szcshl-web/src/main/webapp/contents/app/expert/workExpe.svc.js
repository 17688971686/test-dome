(function() {
	'workExpe strict';

	angular.module('app').factory('workExpeSvc', workExpe);

	workExpe.$inject = [ '$http'];

	function workExpe($http) {
		var service = {
				createWork:createWork,
				saveWork:saveWork,
				deleteWork:deleteWork,
				updateWork:updateWork,
				gotoWPage:gotoWPage,
				getWorkById:getWorkById,
				getWork:getWork
			 
			 
		};

		return service;
		//begin#getWork
		function getWork(vm){
			alert("ddfdf");
			var httpOptions = {
					method : 'GET',
					url : rootPath + "/workExpe/getWork?$filter=expert.expertID eq '"+vm.model.expertID+"'"
				}
				var httpSuccess = function success(response) {
					console.log(response);
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.work=response.data;
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
		
		//begin#deleteWork
		function deleteWork(vm){
			common.initJqValidation();
			var isCheck=$("input[name='checkwr']:checked");
			if(isCheck.length<1){
				common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			}else{
				var ids="";
				 $.each(isCheck,function(i,obj){
					 ids+=obj.value+",";
				 });
				 
				/*for(var i=0;i<isCheck.length;i++){
					ids+=isCheck[i].val()+",";
				}*/
				vm.isSubmit = true;
				var httpOptions = {
					method : 'delete',
					url :  rootPath + "/workExpe/deleteWork",
					data : ids
	
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.isSubmit = false;
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
		//end#delertWork
		
		//begin#updateWork
		function updateWork(vm){
			common.initJqValidation();
			var isCheck=$("input[name='checkwr']:checked");
			if(isCheck.length<1){
				common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			}else if(isCheck.length>1){
				common.alert({
					vm : vm,
					msg : "无法同时操作多条数据",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			}else{
				    vm.weID=isCheck.val();
				    getWorkById(vm);
				    gotoWPage(vm);
					
				
				
				
			}
		}
		
		//begin#getWorkById
		function getWorkById(vm){
			
			var httpOptions = {
					method : 'get',
					url : common.format( rootPath + "/workExpe/getWork?$filter=weID eq '{0}'", vm.weID)
				}
				var httpSuccess = function success(response) {
					vm.model = response.data[0];
					console.log(vm.model);
					if (vm.isUpdate) {
						//initZtreeClient(vm);
					}
				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
			
			
		}
		//清空页面数据
		function cleanValue(){
			$("input").val("");
		}
		// begin#gotoWPage
		function gotoWPage(vm){
			var WorkeWindow = $("#wrwindow");
			//WorkeWindow.show();
            WorkeWindow.kendoWindow({
                width: "1000px",
                height: "500px",
                title: "添加工作经历",
                visible: false,
                modal: true,
                closable: true,
                actions: [
                    "Pin",
                    "Minimize",
                    "Maximize",
                    "Close"
                ]             
            }).data("kendoWindow").center().open();
		}
		// end#gotoWPage
		
		
		//begin#getWork
		function getWork(vm){
			var httpOptions = {
					method : 'GET',
					url : rootPath + "/workExpe/getWork?$filter=expert.expertID eq '"+vm.model.expertID+"'"
				}
				var httpSuccess = function success(response) {
					console.log(response);
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.work=response.data;
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
		
		//begin#saveWork
		function saveWork(vm){
			vm.isSubmit = true;
			vm.model.weID=vm.weID;
			vm.model.beginTime=$('#beginTime').val();
			vm.model.endTime=$('#endTime').val();

			var httpOptions = {
				method : 'put',
				url : rootPath + "/workExpe/updateWork",
				data : vm.model
			}

			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						 window.parent.$("#wrwindow").data("kendoWindow").close();
						 getWork(vm);
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
		//begin#createWork
		function createWork(vm){
			common.initJqValidation();
			var isValid = $('form').valid();
				//vm.isSubmit = true;
				vm.model.beginTime=$('#beginTime').val();
				vm.model.endTime=$('#endTime').val();
				console.log(vm.model);
				var httpOptions = {
					method : 'post',
					url : rootPath+"/workExpe/workExpe",
					data : vm.model
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
									//vm.isSubmit = false;
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
})();