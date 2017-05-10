(function() {
	'use strict';

	angular.module('app').factory('roomCountSvc', roomCount);

	roomCount.$inject = [ '$http','$compile' ];	
	function roomCount($http,$compile) {	
		var url_roomCount = rootPath +"/roomCount";
		var url_room = rootPath + "/room";
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
				  vm.room ={};
				  vm.room=data;
			  }).error(function(data) {  
				  alert("查询会议室失败");
			  }); 
			
		}
		
		//多条件查询
		function conneFilter(vm){
			var arr=new Array();
			var filter="?";
			var value="";
			var url=url_room+filter+","+value;
			var i=0;
			if(vm.model.rbName!=null){
				var paramObj = {}
				paramObj.name = "rbName";
				paramObj.value = vm.model.rbName;
				arr[i++]=paramObj;
				
			}
			if(vm.model.rbType !=null){
				var paramObj = {}
				paramObj.name = "rbType";
				paramObj.value = vm.model.rbType;
				arr[i++]=paramObj;
			}
			if(vm.model.mrID!=null){
				var paramObj = {}
				paramObj.name = "mrID";
				paramObj.value = vm.model.mrID;
				arr[i++]=paramObj;
			}
			
		
			/*var rbDay=$("#beginTime").val();
			
			if(rbDay!=null){
				var paramObj = {}
				paramObj.name = "rbDay";
				paramObj.value = rbDay;
				alert(paramObj.value)
				arr[i++]=paramObj;
			}
			var end = $("#endTime").val();
			if(end!=null){
				var paramObj = {}
				paramObj.name ="rbDay";
				paramObj.value=end;
				arr[i++] =paramObj;
			}*/
			if(vm.model.dueToPeople!=null){
				var paramObj = {}
				paramObj.name = "dueToPeople";
				paramObj.value = vm.model.dueToPeople;
				arr[i++]=paramObj;
			}
			
			var param = "?$filter=";
			for(var k=0;k<arr.length;k++){
				if(k > 0){
					param += " and ";
				}
				param += arr[k].name +" eq " + "\'"+arr[k].value+"\'";
			}
			//url=url_expert+filter+value;
			return param;
		}
		function showClick(vm){
			var url=conneFilter(vm);
			var httpOptions = {
					method : 'get',
					url : url_room+url
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
				transport : common.kendoGridConfig().transport(url_room+"/fingByOData"),
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
					    width: 60,
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
						type : "date",
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
					dataBound :dataBound,
					resizable: true
				};
		
			
		}// end fun grid

		


	}
	
	
})();