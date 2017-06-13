(function() {
	'use strict';

	angular.module('app').factory('unitAndUserSvc', unitAndUser);

	unitAndUser.$inject = [ '$http','$compile' ];	
	function unitAndUser($http,$compile) {	
		var url_unitAndUser=rootPath+ '/assistUnit/unitUser';
		var url_userNotIn=rootPath + '/assistUnit/userNotIn';
		
			
		var service = {	
			unitAndUserGrid : unitAndUserGrid,	//获取单位所有人员
			allUserGrid : allUserGrid,			//获取所有非本单位用户
			addUser : addUser,		//添加人员
			removeUser : removeUser	//移除成员
		};		
		return service;	
		
		//begin addUser
		function addUser(vm,assistUnitUserId){
			var httpOptions={
				method:'post',
				url:rootPath+ '/assistUnit/addUser',
				params:{
					unitId:vm.id,
					userId:assistUnitUserId
				}
			}
			 var httpSuccess=function success(response){
			 
			 	common.requestSuccess({
			 	
			 		vm:vm,
			 		response:response,
			 		fn:function(){
			 			vm.unitAndUserGrid.dataSource.read();
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
		
		}//end addUser
		
		
		//begin removeUser
		function removeUser(vm,assistUnitUserId){
			var httpOptions={
				method:'delete',
				url: rootPath+ '/assistUnit/removeUser',
				params:{
					unitId: vm.id,
					userId: assistUnitUserId
				}
			}
			
			var httpSuccess=function success(response){
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function(){
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
		
		}//end removeUser
		
		//begin 
		function unitAndUserGrid(vm){
		
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_unitAndUser+"?assistUnitID="+vm.id),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : false,
				serverSorting : false,
				serverFiltering : false,
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
						field : "userName",
						title : "名称",
						width : 200,
						filterable : false
					},
					{
						field : "",
						title : "操作",
						width : 180,
						template : function(item) {
							return common.format($('#columnBtns').html(),
									"vm.remove('" + item.id + "')", item.id);
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
		
		}//end
		
		//begin 
		function allUserGrid(vm){
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_userNotIn+"?assistUnitID="+vm.id),
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
						field : "userName",
						title : "名称",
						width : 200,
						filterable : true
					},
					{
						field : "",
						title : "操作",
						width : 80,
						template : function(item) {
							return common.format($('#allUserGridBtns').html(),
									"vm.add('" + item.id + "')", item.id);

						}

					}

			];
			// End:column

			vm.unitAndUserGrid = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				resizable : true
			};
			
		
		}
		
		
	}
		
	
	
})();