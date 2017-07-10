(function() {
	'use strict';

	angular.module('app').factory('adminSvc', admin);

	admin.$inject = ['$rootScope', '$http'];	
	
	function admin($rootScope,$http) {

		var service = {
			gtasksGrid : gtasksGrid,		//个人待办
            etasksGrid : etasksGrid,		//个人办结
            dtasksGrid : dtasksGrid,        //在办任务
            countWorakday : countWorakday,	//计算工作日
            initAnnountment : initAnnountment,	//初始化通知公告栏
            initFile : initFile,	//初始化附件
            upload : upload,	//	下载附件
            getSignList: getSignList,  //项目查询统计
            initSignList: initSignList,//初始化項目查詢統計
            searchSignList : searchSignList, //項目查詢統計
		}
		return service;	
		
		//begin countWorakday
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

		}//end countWorakday
		
		//begin initAnnountment
		function initAnnountment(vm){
			var httpOptions={
				method :"get",
				url : rootPath+"/annountment/getHomePageAnnountment"
			}
			
			var httpSuccess=function success(response){
				vm.annountmentList=response.data;
			}
			
			common.http({
				vm:vm,
				$http:$http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		
		}//end initAnnountment
		


        //begin initFile
        function initFile(vm){
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/file/findByBusinessId",
                params: {businessId: vm.anId}
            }
        	
        	var httpSuccess=function success(response){
        		vm.file=response.data;
        	}
        	 common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        }//end initFile
        
        //begin upload
        function upload(vm,sysFileId){
        	window.open(rootPath + "/file/fileDownload?sysfileId=" + sysFileId);
        }//end upload
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
		
         //begin_getSignList
         function getSignList(vm){
       		//var url=common.kendoGridConfig().transport(rootPath + "/sign/getSignList", $("#searchform"),{filter: "mOrgId eq '"+vm.sign.mOrgId+"'"});
       		//console.log(url);
       		console.log(typeof vm.sign.isAssociate);
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sign/getSignList", $("#searchform"),{filter: 'isAssociate eq '+vm.isAssociate}),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource

            // Begin:column
            var columns = [
            	 {
                     field: "",
                     title: "序号",
                     template: "<span class='row-number'></span>",
                     width:50
                 },
                {
                    field: "projectname",
                    title: "项目名称",
                    width: 160,
                    filterable: false
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 140,
                    filterable: false
                },
                {
                    field: "receivedate",
                    title: "收文日期",
                    width: 200,
                    filterable: false
                },
                {
                    field: "dispatchDate",
                    title: "发文日期",
                    width: 160,
                    filterable: false
                },
                {
                    field: "reviewdays",
                    title: "评审天数",
                    width: 140,
                    filterable: false
                },
                {
                    field: "surplusdays",
                    title: "剩余工作日",
                    width: 140,
                    filterable: false
                },
                {
                    field: "mOrgName",
                    title: "评审部门",
                    width: 140,
                    filterable: false
                },
                {
                    field: "mainuserName",
                    title: "项目负责人",
                    width: 140,
                    filterable: false
                },
                {
                    field: "filenum",
                    title: "归档编号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dfilenum",
                    title: "文件字号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "appalyInvestment",
                    title: "申报投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "authorizeValue",
                    title: "审定投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extraValue",
                    title: "核减（增）投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extraRate",
                    title: "核减率",
                    width: 140,
                    filterable: false
                },
                {
                    field: "approveValue",
                    title: "批复金额",
                    width: 140,
                    filterable: false
                },
                {
                    field: "approveTime",
                    title: "批复来文时间",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dispatchType",
                    title: "发文类型",
                    width: 140,
                    filterable: false
                },
                {
                    field: "fileDate",
                    title: "归档日期",
                    width: 140,
                    filterable: false
                },
                {
                    field: "builtcompanyName",
                    title: "建设单位",
                    width: 140,
                    filterable: false
                },
                {
                    field: "isassistproc",
                    title: "是否协审",
                    width: 140,
                    filterable: false
                },
                {
                    field: "daysafterdispatch",
                    title: "发文后工作日",
                    width: 140,
                    filterable: false
                }
               /* {
                    field: "",
                    title: "操作",
                    width: 60,
                    template: $('#columnBtns').html()
                }*/
            ];
            // End:column
            vm.signListOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
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
            
       }//end_getSignList
       
       	//begin_searchSignList
        function searchSignList(vm){
       	//console.log(vm.sign.isAssociate);
       	if(vm.sign.isAssociate == '1'){
       		var isAssociate=1;
       	}else{
       		var isAssociate=0;
       	}
       vm.signListOptions.dataSource.read();
       }//end_searchSignList
       
       	 //begin_initSignList
        function initSignList(vm){
       	console.log(vm.isHaveEIA);
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/initSignList"
            }
            var httpSuccess = function success(response) {
            	 vm.sign.mOrgId = response.data.mOrgId;
            	 vm.usersList = response.data.usersList;
            	 vm.orgsList = response.data.orgsList;
            	 
            	 vm.signListOptions.dataSource.read();
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
       } //end_initSignList
	}
})();