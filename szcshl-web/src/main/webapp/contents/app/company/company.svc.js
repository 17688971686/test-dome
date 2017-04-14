(function() {
	'use strict';

	angular.module('app').factory('companySvc', company);

	company.$inject = [ '$http','$compile' ];	
	function company($http,$compile) {	
		var url_company = rootPath +"/company";
		var url_back = '#/company';
		var url_user=rootPath +'/user';
			
		var service = {
			grid : grid,
			createcompany : createcompany,			
			getcompanyById : getcompanyById,
			updatecompany:updatecompany,
			deletecompany:deletecompany			
		};		
		return service;	
		
		
		
		function grid(vm) {

			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_company),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,			
				pageSize: 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});

			// End:dataSource

			// Begin:column
			var columns = [
					{
						template : function(item) {
							return kendo
									.format(
											"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
											item.id)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
						
					},  {
						field : "coName",
						title : "单位名称",
						width : 200,						
						filterable : false
					},
					{
						field : "coPhone",
						title : "单位电话",
						width : 200,						
						filterable : false
					},
					{
						field : "coPC",
						title : "邮编",
						width : 200,						
						filterable : false
					},
					{
						field : "coAddress",
						title : "地址",
						width : 200,						
						filterable : false
					},
					{
						field : "coSite",
						title : "网站",
						width : 200,						
						filterable : false
					},
					{
						field : "coSynopsis",
						title : "单位简介",
						width : 200,						
						filterable : false
					},
					/*{
						field : "coDept",
						title : "直属部门",
						width : 200,						
						filterable : false
					},
					{
						field : "coDeptName",
						title : "直属部门名称",
						width : 200,						
						filterable : false
					},*/
					{
						field : "coFax",
						title : "传真",
						filterable : false
					}, 
					{
						field : "",
						title : "操作",
						width : 280,
						template:function(item){							
							return common.format($('#columnBtns').html(),"vm.del('"+item.id+"')",item.id);
							
						}
						

					}

			];
			// End:column
		
			vm.gridOptions={
					dataSource : common.gridDataSource(dataSource),
					filterable : common.kendoGridConfig().filterable,
					pageable : common.kendoGridConfig().pageable,
					noRecords:common.kendoGridConfig().noRecordMessage,
					columns : columns,
					resizable: true
				};
			
		}// end fun grid

		function createcompany(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid && vm.iscompanyExist == false) {
				vm.isSubmit = true;
				
		
	               
				var httpOptions = {
					method : 'post',
					url : url_company,
					data : vm.model
				}

				var httpSuccess = function success(response) {				
					
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {							
							
							common.alert({
								vm:vm,
								msg:"操作成功",
								fn:function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									location.href = url_back;
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

			} else {				
//				common.alert({
//					vm:vm,
//					msg:"您填写的信息不正确,请核对后提交!"
//				})
			}
		}// end fun createcompany

		

		function getcompanyById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(url_company + "?$filter=id eq '{0}'", vm.id)
			}
			var httpSuccess = function success(response) {
				vm.model = response.data.value[0];
				if (vm.isUpdate) {
					//initZtreeClient(vm);
				}
			}
			
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}// end fun getcompanyById
		
		function updatecompany(vm){
			common.initJqValidation();			
			var isValid = $('form').valid();
			if (isValid && vm.iscompanyExist == false) {
				vm.isSubmit = true;
				vm.model.id=vm.id;// id
				
			
               
				var httpOptions = {
					method : 'put',
					url : url_company,
					data : vm.model
				}

				var httpSuccess = function success(response) {
					
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {
							
							common.alert({
								vm:vm,
								msg:"操作成功",
								fn:function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');							
								}
							})
						}
						
					})
				}

				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess
				});

			} else {
//				common.alert({
//				vm:vm,
//				msg:"您填写的信息不正确,请核对后提交!"
//			})
			}
		}// end fun updatecompany
		
		function deletecompany(vm,id) {
		
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url:url_company,
                data:id
                
            }
            var httpSuccess = function success(response) {
                
                common.requestSuccess({
					vm:vm,
					response:response,
					fn:function () {
	                    vm.isSubmit = false;
	                    vm.gridOptions.dataSource.read();
	                }
					
				});

            }
            common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
        }// end fun deletecompany
		
		
		
		

	}
	
	
	
})();