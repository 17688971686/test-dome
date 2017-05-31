(function() {
	'use strict';

	angular.module('app').factory('roomCountSvc', roomCount);

	roomCount.$inject = [ '$http','$compile' ];	
	function roomCount($http,$compile) {	
		var url_roomCount = rootPath +"/roomCount";
		var url_room = rootPath + "/room";
		var url_user = rootPath + "/user";
		var url_back = '#/roomCount';
		var url_user=rootPath +'/user';
			
		var service = {
			grid : grid,
			queryRoomCount:queryRoomCount,//查询
			roomList : roomList,
			findAllOrg:findAllOrg,//查询部门列表
			
		};		
		return service;	
		
		function queryRoomCount(vm){
			vm.gridOptions.dataSource.read();
		}
		
		//S_查询部门列表
		function findAllOrg(vm){
			var httpOptions = {
					method: 'get',
					url: common.format(url_user + "/getOrg")
			}
			var httpSuccess = function success(response) {
				vm.orglist = {};
				vm.orglist = response.data;
			}
			common.http({
				vm: vm,
				$http: $http,
				httpOptions: httpOptions,
				success: httpSuccess
			});
		}
		//E_查询部门列表
		
		//S_查询所有会议室名称
		function roomList(vm){
			
			var httpOptions = {
					method: 'get',
					url: common.format(url_room + "/roomNamelist")
			}
			var httpSuccess = function success(response) {
				vm.roomlists = {};
				vm.roomlists = response.data;
			}
			common.http({
				vm: vm,
				$http: $http,
				httpOptions: httpOptions,
				success: httpSuccess
			});
			
		}
		//E_查询所有会议室名称
		
		//S_giid
		function grid(vm) {
			
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_room+"/fingByOData",$("#roomCountform")),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					},
					
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,			
				pageSize: 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				},
			
			});

			// End:dataSource
			var  dataBound=function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } 
			// Begin:column
			var columns = [
					
					{  
					    field: "rowNumber",  
					    title: "序号",  
					    width: 40,
					    filterable : false,
					    template: "<span class='row-number'></span>"  
					 }
					,
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
						type : "date",
						width : 120,						
						filterable : false
					},
					{
						field : "beginTime",
						title : "会议开始时间",
						width : 120,						
						filterable : false
					},
					{
						field : "endTime",
						title : "会议结束时间",
						type : "date",
						width : 120,						
						filterable : false
					},
					{
						field : "stageOrg",
						title : "评审部门",
						width : 100,						
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

			];
			// End:column
		
			vm.gridOptions={
					dataSource : common.gridDataSource(dataSource),
					filterable : common.kendoGridConfig().filterable,
					pageable : common.kendoGridConfig().pageable,
					noRecords:common.kendoGridConfig().noRecordMessage,
					columns : columns,
					dataBound :dataBound,
					resizable: true
				};
			
		}// end fun grid

	}
	
	
})();