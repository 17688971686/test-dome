(function() {
	'use strict';

	angular.module('app').factory('adminSvc', admin);

	admin.$inject = ['$rootScope', '$http'];	
	
	function admin($rootScope,$http) {
		
		var service = {
			gtasksGrid : gtasksGrid,		//个人待办
		}
		return service;	
		
		function gtasksGrid(vm){
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/flow/html/tasks"),
				schema : {
					data: "value",
                    total: function (data) {
                    	$rootScope.$GtasksCount = data['count']; 
                    	return data['count']; 
                    },
                    model:{
                    	id : "id",                 
						fields : {
							createdDate : {
								type : "date"
							}
						}
                    }
				},
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,
				pageSize : 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			var columns = [
				 {
                     field: "",
                     title: "序号",
                     template: "<span class='row-number'></span>",
                     width:30
                 },
                 {
                     field: "flowName",
                     title: "流程名称",
                     width: 200,
                     filterable : false,
                 },                 
                 {
                     field: "taskName",
                     title: "当前环节",
                     width: 200,
                     filterable : false,
                 },
                 {
                     field: "",
                     title: "开始时间",
                     width: 200,
                     filterable : false,
                     template: function(item) {
 						if(item.createDate){
 							return kendo.toString(new Date(item.createDate), 'yyyy-MM-dd hh:mm:ss');
 						}
 						else{
 							return " ";
 						}
 					}	
                 },
                 {
                     field: "",
                     title: "流程状态",
                     width: 200,
                     filterable : false,
                     template:function(item){
 						if(item.isSuspended){
 							return '<span style="color:orange;">已暂停</span>';
 						}else{
 							return '<span style="color:green;">进行中</span>';
 						}
 					}
                 },
				{
					field : "",
					title : "操作",
					width : 180,
					template:function(item){
						//项目签收流程，则跳转到项目签收流程处理野人
						if(item.flowKey=="FINAL_SIGN_FLOW"){
							return common.format($('#columnBtns').html(),"signFlowDeal",item.businessKey,item.taskId,item.processInstanceId);
						}else{
							return '<a class="btn btn-xs btn-danger" >流程已停用</a>';
						}						
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
				resizable : true,
				dataBound: function () {  
                    var rows = this.items();  
                    var page = this.pager.page() - 1;  
                    var pagesize = this.pager.pageSize();  
                    $(rows).each(function () {  
                        var index = $(this).index() + 1 + page * pagesize;  
                        var rowLabel = $(this).find(".row-number");  
                        $(rowLabel).html(index);  
                    });  
                } 
			};					
		}
		
	}
			
})();