(function() {
	'use strict';

	angular.module('app').factory('roomCountSvc', roomCount);

	roomCount.$inject = [ '$http','$compile' ];	
	function roomCount($http,$compile) {	
		var url_roomCount = rootPath +"/roomCount";
		var url_room = rootPath + "/room";
		//alert(url_roomCount);
		var url_back = '#/roomCount';
		var url_user=rootPath +'/user';
			
		var service = {
			grid : grid,
			roomShow : roomShow,
			showClick : showClick,
		//	getroomCountById : getroomCountById,
			
		};		
		return service;	
		
		//会议预定查询下拉框使用
		function roomShow(vm){
			
			 $http.get(url_room+"/roomShow" 
			  ).success(function(data) {  
				  //console.log(data);
				  vm.room ={};
				  vm.room=data;
				
			  }).error(function(data) {  
			      //处理错误  
				  alert("查询会议室失败");
			  }); 
			
		}
		//多条件查询
		function showClick(vm){
			
			
			//alert(common.format(url_room + "?$filter=dueToPeople eq '{0}' and rbName eq '{1}'", vm.model.dueToPeople,vm.model.rbName));
			
		//	return;
			var httpOptions = {
					method : 'get',
					url : common.format(url_room + "?$filter=dueToPeople eq '{0}' and rbName eq '{1}'", vm.model.dueToPeople,vm.model.rbName)
			};
			
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {						
						vm.gridOptions.dataSource.data([]);
						vm.gridOptions.dataSource.data(response.data.value);
						vm.gridOptions.dataSource.total(response.data.count);
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
		function grid(vm) {
			//时间控件start
			$(document).ready(function () {
			  	 kendo.culture("zh-CN");
			      $("#beginTime").kendoDatePicker({
			      	 format: "yyyy-MM-dd",
			      
			      
			      });
			  }); 
			$(document).ready(function () {
			 	 kendo.culture("zh-CN");
			     $("#endTime").kendoDatePicker({
			     	 format: "yyyy-MM-dd",
			     
			     
			     });
			 }); 
			//时间控件end
			
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_room),
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
						field : "host",
						title : "序号",
						width : 100,						
						filterable : false
					},
					{
						field : "rbName",
						title : "会议名称",
						width : 120,						
						filterable : false
					},
					{
						field : "addressName",
						title : "会议地点",
						width : 120,						
						filterable : false
					},
					{
						field : "rbDay",
						title : "会议日期",
						width : 160,						
						filterable : false
					},
					{
						field : "beginTime",
						title : "会议开始时间",
						width : 160,						
						filterable : false
					},
					{
						field : "endTime",
						title : "会议结束时间",
						width : 160,						
						filterable : false
					},
					{
						field : "rbType",
						title : "会议类型",
						width : 100,						
						filterable : false
					},
					{
						field : "dueToPeople",
						title : "会议预定人",
						width : 100,						
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
					
					/*{
						field : "",
						title : "操作",
						width : 100,
						template:function(item){							
							return common.format($('#columnBtns').html(),"vm.del('"+item.id+"')",item.id);
							
						}
						

					}*/

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



	}
	
	
})();