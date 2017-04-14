(function() {
	'use strict';

	angular.module('app').factory('meetingSvc', meeting);

	meeting.$inject = [ '$http' ];

	function meeting($http) {
		var url_meeting = rootPath + "/meeting";
		var url_back = '#/meeting';
	//	var url_role = rootPath + "/role";
		var service = {
			grid : grid,
			getMeetingById : getMeetingById,
			initZtreeClient : initZtreeClient,
			createMeeting : createMeeting,
			deleteMeeting : deleteMeeting,
			updateMeeting : updateMeeting
		};

		return service;

		// begin#updateUser
		function updateMeeting(vm) {
			
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
				vm.isSubmit = true;
				vm.model.id = vm.id;// id


				var httpOptions = {
					method : 'put',
					url : url_meeting,
					data : vm.model
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

			} else {
				// common.alert({
				// vm:vm,
				// msg:"您填写的信息不正确,请核对后提交!"
				// })
			}

		}

		// begin#deleteUser
		function deleteMeeting(vm, id) {
			
			vm.isSubmit = true;
			var httpOptions = {
				method : 'delete',
				url : url_meeting,
				data : id

			}
			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.isSubmit = false;
						vm.gridOptions.dataSource.read();
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

		// begin#createUser
		function createMeeting(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
				vm.isSubmit = true;


				var httpOptions = {
					method : 'post',
					url : url_meeting,
					data : vm.model
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
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});

			}
		}

		// begin#initZtreeClient
		function initZtreeClient(vm) {
			var httpOptions = {
				method : 'get',
				url : url_role
			}
			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						var zTreeObj;
						var setting = {
							check : {
								chkboxType : {
									"Y" : "ps",
									"N" : "ps"
								},
								enable : true
							}
						};
						var zNodes = $linq(response.data.value).select(
								function(x) {
									return {
										id : x.id,
										name : x.roleName
									};
								}).toArray();
						var rootNode = {
							id : '',
							name : '角色集合',
							children : zNodes
						};
						zTreeObj = $.fn.zTree.init($("#zTree"), setting,
								rootNode);
						if (vm.isUpdate) {
							//updateZtree(vm);

						}
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

		// begin#getUserById
		function getMeetingById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(url_meeting + "?$filter=id eq '{0}'", vm.id)
			}
			var httpSuccess = function success(response) {
				vm.model = response.data.value[0];
				if (vm.isUpdate) {
					initZtreeClient(vm);
				}
			}

			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		// begin#grid
		function grid(vm) {

			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_meeting),
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
				pageSize : 10,
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

					},
					{
						field : "num",
						title : "会议室编号",
						width : 200,
						filterable : true
					},
					{
						field : "mrName",
						title : "会议室名称",
						width : 200,
						filterable : true
					},
					{
						field : "addr",
						title : "会议室地点",
						filterable : false
					},
					{
						field : "mrStatus",
						title : "会议室状态",
						filterable : false
					},
					{
						field : "capacity",
						title : "会议室容量（人）",
						filterable : false
					},
					{
						field : "userName",
						title : "会议室负责人",
						filterable : false
					},
					{
						field : "userPhone",
						title : "负责人电话",
						filterable : false
					},
					{
						field : "createDate",
						title : "创建时间",
						filterable : false
					},
					{
						field : "mrType",
						title : "会议室类型",
						width : 180,
						filterable : false,
						

					},
					{
						field : "",
						title : "操作",
						width : 180,
						template : function(item) {
							return common.format($('#columnBtns').html(),
									"vm.del('" + item.id + "')", item.id);

						}

					}

			];
			// End:column

			vm.gridOptions = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				resizable : true
			};

		}// end fun grid

		
	}
})();