(function() {
	'use strict';

	angular.module('app').factory('adminSvc', admin);

	admin.$inject = ['$rootScope', '$http'];	
	
	function admin($rootScope,$http) {
		
		var service = {
			gtasksGrid : gtasksGrid,		//个人待办
            etasksGrid : etasksGrid,		//个人办结
            dtasksGrid : dtasksGrid,        //在办任务
            countWorakday : countWorakday
		}
		return service;	
		
		function countWorakday(vm){
			var httpOptions={
				method:"get",
				url:rootPath +"/workday/countWorkday"
			}
		
			var httpSuccess=function success(response){
			}
			 common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

		}

		//S_gtasksGrid
		function gtasksGrid(vm){
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/flow/html/tasks"),
				schema : {
					data: "value",
                    total: function (data) {
					    if(data['count']){
                            $('#GtasksCount').html(data['count']);
                        }else{
                            $('#GtasksCount').html(0);
                        }
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
                    title: "",
                    width:30,
                    template:function(item){
                       switch(item.lightState){
                           case "4":          //暂停
                               return $('#span1').html();
                               break;
                           case "8":         	//存档超期
                               return $('#span5').html();
                               break;
                           case "7":           //超过25个工作日未存档
                               return $('#span4').html();
                               break;
                           case "6":          	//发文超期
                               return $('#span3').html();
                               break;
                           case "5":          //少于3个工作日
                               return $('#span2').html();
                               break;
                           case "1":          //在办
                                return "";
                                break;
                           case "2":           //已发文
                               return "";
                               break;
                           case "3":           //已发送存档
                               return "";
                               break;
                           default:
                               ;
                       }
                    }
                },
				 {
                     field: "",
                     title: "序号",
                     template: "<span class='row-number'></span>",
                     width:50
                 },
                 {
                     field: "projectName",
                     title: "项目名称",
                     filterable : false,
                     width:150
                 },
                {
                    field: "reviewStage",
                    title: "项目阶段",
                    filterable : false,
                    width:150
                },
                 {
                     field: "processName",
                     title: "所属流程",
                     width: 120,
                     filterable : false
                 },
                 {
                     field: "nodeName",
                     title: "当前环节",
                     width: 120,
                     filterable : false
                 },
                {
                    field: "",
                    title: "处理人",
                    width: 120,
                    filterable : false,
                    template:function(item) {
                        if(item.assignee){
                            return item.assignee;
                        }else if(item.userName){
                            return item.userName;
                        }
                    }
                },
                 {
                     field: "createTime",
                     title: "接收时间",
                     width: 120,
                     filterable : false,
                     format: "{0: yyyy-MM-dd HH:mm:ss}"
                 },
                 {
                     field: "",
                     title: "流程状态",
                     width: 80,
                     filterable : false,
                     template:function(item){
 						if(item.processState && item.processState == 0){
 							return '<span style="color:orange;">已暂停</span>';
 						}else{
 							return '<span style="color:green;">进行中</span>';
 						}
 					}
                 },
				{
					field : "",
					title : "操作",
					width : 80,
					template:function(item){
						//项目签收流程，则跳转到项目签收流程处理野人
						if(item.processKey=="FINAL_SIGN_FLOW" || item.processKey=="SIGN_XS_FLOW"){
							return common.format($('#columnBtns').html(),"signFlowDeal",item.businessKey,item.taskId,item.processInstanceId);
						}else{
							return "<a class='btn btn-xs btn-danger' >流程已停用</a>";
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
		}//E_gtasksGrid

        //S_etasksGrid
		function etasksGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type : 'odata',
                transport : common.kendoGridConfig().transport(rootPath+"/flow/html/endTasks"),
                schema : {
                    data: "value",
                    total: function (data) {
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
                    width:40
                },
                {
                    field: "businessName",
                    title: "任务名称",
                    filterable : false,
                    width:180
                },
                {
                    field: "createDate",
                    title: "开始时间",
                    width: 150,
                    filterable : false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "endDate",
                    title: "结束时间",
                    width: 150,
                    filterable : false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "用时",
                    width: 180,
                    filterable : false,
                    template:function(item){
                        if(item.durationTime){
                            return item.durationTime;
                        }else{
                            return '<span style="color:orangered;">已办结</span>';
                        }
                    }
                },
                {
                    field: "",
                    title: "流程状态",
                    width: 120,
                    filterable : false,
                    template:function(item){
                        return '<span style="color:orangered;">已办结</span>';
                    }
                },
                {
                    field : "",
                    title : "操作",
                    width : 80,
                    template:function(item){
                        if((item.processDefinitionId).indexOf("FINAL_SIGN_FLOW") >= 0 || (item.processDefinitionId).indexOf("SIGN_XS_FLOW") >= 0){
                            return common.format($('#columnBtns').html(),"endSignDetail",item.businessKey,item.processInstanceId);
                        }else{
                        	return '';
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
        }//E_etasksGrid

        //S_dtasksGrid
        function dtasksGrid(vm){
            var dataSource = new kendo.data.DataSource({
                type : 'odata',
                transport : common.kendoGridConfig().transport(rootPath+"/flow/html/doingtasks"),
                schema : {
                    data: "value",
                    total: function (data) {
                        return data['count'];
                    },
                    model:{
                        id : "id"
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
                    title: "",
                    width:30,
                    template:function(item){
                        switch(item.lightState){
                            case "4":          //暂停
                                return $('#span1').html();
                                break;
                            case "8":         	//存档超期
                                return $('#span5').html();
                                break;
                            case "7":           //超过25个工作日未存档
                                return $('#span4').html();
                                break;
                            case "6":          	//发文超期
                                return $('#span3').html();
                                break;
                            case "5":          //少于3个工作日
                                return $('#span2').html();
                                break;
                            case "1":          //在办
                                return "";
                                break;
                            case "2":           //已发文
                                return "";
                                break;
                            case "3":           //已发送存档
                                return "";
                                break;
                            default:
                                return "";
                                ;
                        }
                    }
                },
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width:50
                },
                {
                    field: "projectName",
                    title: "项目名称",
                    filterable : false,
                    width:150
                },
                {
                    field: "reviewStage",
                    title: "项目阶段",
                    filterable : false,
                    width:150
                },
                {
                    field: "processName",
                    title: "所属流程",
                    width: 120,
                    filterable : false
                },
                {
                    field: "nodeName",
                    title: "当前环节",
                    width: 120,
                    filterable : false
                },
                {
                    field: "",
                    title: "处理人",
                    width: 120,
                    filterable : false,
                    template:function(item) {
                        if(item.assignee){
                            return item.assignee;
                        }else if(item.userName){
                            return item.userName;
                        }
                    }
                },
                {
                    field: "createTime",
                    title: "接收时间",
                    width: 120,
                    filterable : false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "流程状态",
                    width: 80,
                    filterable : false,
                    template:function(item){
                        if(item.processState && item.processState == 0){
                            return '<span style="color:orange;">已暂停</span>';
                        }else{
                            return '<span style="color:green;">进行中</span>';
                        }
                    }
                },
                {
                    field : "",
                    title : "操作",
                    width : 80,
                    template:function(item){
                        //项目签收流程，则跳转到项目签收流程处理野人
                        if(item.processKey=="FINAL_SIGN_FLOW" || item.processKey=="SIGN_XS_FLOW"){
                            return common.format($('#columnBtns').html(),"signFlowDetail",item.businessKey,item.taskId,item.processInstanceId);
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
        }//E_dtasksGrid

	}
})();