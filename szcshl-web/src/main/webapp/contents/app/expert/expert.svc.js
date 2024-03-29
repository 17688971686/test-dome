(function() {
	'expert strict';

	angular.module('app').factory('expertSvc', expert);

	expert.$inject = [ '$http','FileSaver', 'Blob','templatePrintSvc'];
	
	function expert($http,FileSaver,Blob,templatePrintSvc) {
		var url_expert = rootPath + "/expert";
		var service = {
			grid : grid,						//初始化综合查询grid
			auditGrid : auditGrid,				//初始化审核页面的所有grid
			getExpertById : getExpertById,		//通过ID查询专家信息详情
			saveExpert : saveExpert,            //保存专家信息
			deleteExpert : deleteExpert,        //删除专家信息(逻辑删除)
            deleteExpertData:deleteExpertData,	//删除专家信息(物理删除)
			searchMuti : searchMuti,		    //综合查询
			searchAudit : searchAudit,		    //审核查询
			repeatGrid : repeatGrid,		    //重复专家查询
			updateAudit : updateAudit,		    //专家评审
			toAudit : toAudit,				    //由个状态回到审核状态
			auditTo : auditTo,				    //由审核状态去到各个状态
            formReset : formReset,				//重置页面
            exportToExcel : exportToExcel,      //导出excel功能
            expertSelectHis : expertSelectHis,	//专家抽取统计
            expertScoreHis : expertScoreHis,	//专家评分统计
			reviewProjectGrid : reviewProjectGrid,  //专家评审项目列表
            expertPrint : expertPrint,  //专家评审模板打印
            queryExpertDetail : queryExpertDetail , //查看专家详情弹框
		};
		return service;


        //编辑模板打印
        function expertPrint(){
            templatePrintSvc.templatePrint("expertApply_templ");
    }
		//begin formReset
		function formReset(vm){
			$("#searchform")[0].reset();
			//vm.gridOptions.dataSource.read();
		}
		//end formReset
		
		// begin#deleteUser
		function deleteExpert(vm, id , callBack) {
			vm.isSubmit = true;
			var httpOptions = {
				method : 'delete',
				url : url_expert,
                params:{
                    id : id
                }
			}
			var httpSuccess = function success(response) {
                vm.isSubmit = false;
				if(callBack != undefined && typeof  callBack == 'function'){
					callBack(response.data);
				}
			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		// end#deleteUser

		//begin_deleteExpertData
		function deleteExpertData(vm, id , callBack){
            vm.isSubmit = true;
            var httpOptions = {
                method : 'delete',
                url : rootPath + "/expert/deleteExpertData",
                params:{
                    id : id
                }

            }
            var httpSuccess = function success(response) {
                vm.isSubmit = false;
                if(callBack != undefined && typeof  callBack == 'function'){
                    callBack(response.data);
                }
            }
            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
		}//end_deleteExpertData
		
		// begin#search
		function searchMuti(vm) {
            vm.gridOptions.dataSource._skip=0;
			vm.gridOptions.dataSource.read();	
		}
		// end#searchMuti									
				
		// S_保存专家信息
		function saveExpert(expert,isSubmit,callBack) {
            isSubmit = true;
            var httpOptions = {
                method : 'post',
                url : rootPath + "/expert",
                data : expert
            }
            var httpSuccess = function success(response) {
                isSubmit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess,
				onError:function () {
                    isSubmit = false;
                }
            })

		}
		// end#saveExpert

        // begin#getExpertById
		function getExpertById(expertID,callBack) {
			var httpOptions = {
				method : 'post',
				url : url_expert+"/findById",
				params:{
					id:expertID
				}
			}
			var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
			} 
			common.http({
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}//end#getExpertById								
		
		// begin#grid
		function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#searchform"),vm.gridParams),
                schema: common.kendoGridConfig().schema({
                    id: "expertID",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: vm.queryParams.pageSize || 10,
                page: vm.queryParams.page || 1,
                sort: {
                    field: "applyDate",
                    dir: "desc"
                }
            });
            // End:dataSource

			/*var  dataBound = function () {
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } */
						
			// End:column
			vm.gridOptions = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getExpertColumns(vm),
                pageable: common.kendoGridConfig(vm.queryParams).pageable,
                dataBound: common.kendoGridConfig(vm.queryParams).dataBound,
				resizable : true
			};
		}// end fun grid
		
		function getExpertColumns(vm){
			var columns = [
				/*{
					template : function(item) {
						if(item.state != '5'){
                            return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",item.expertID)
						}else{
                            return kendo.format("<input type='checkbox' disabled='disabled'  relId='{0}' name='checkbox' class='checkbox' />",item.expertID)
						}

					},
					filterable : false,
					width : 40,
					title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
				},*/
				{  
				    field: "rowNumber",  
				    title: "序号",  
				    width: 50,
                    attributes: {
                        "class": "table-cell",
                        style: "text-align: center"
                    },
				    template: "<span class='row-number'></span>",
                    filterable : false,
			    },
				{
					field : "name",
					title : "姓名",
					width : 80,
					filterable : false,
                    template: function (item) {
                        return '<a  ng-click="vm.findExportDetail(\''+item.expertID+'\')">'+item.name+'</a>'
                    }
				},
                {
                    field : "comPany",
                    title : "工作单位",
                    width : 300,
                    filterable : false
                },
                {
                    field : "phone",
                    title : "办公电话",
                    width : 120,
                    filterable : false
                },
				{
					field : "userPhone",
					title : "手机号码",
					width : 120,
					filterable : false
				},
                {
                    field : "job",
                    title : "职位",
                    width : 150,
                    filterable : false
                },
                {
                    field : "post",
                    title : "职称",
                    width : 120,
                    filterable : false
                },
                {
                    field : "expertSort",
                    title : "专家类型",
                    width : 150,
                    filterable : false
                },
                {
                    field : "compositeScore",
                    title : "评分",
                    width : 60,
                    filterable : false
                },
				{
					field : "",
					title : "操作",
					width : 80,
					template : function(item) {
						var showDel = true;
                        if(item.state != '5'){
                            showDel = true;
						}else{
                            showDel = false;
						}
                       return common.format($('#columnBtns').html(),  "vm.del('" + item.expertID + "')", item.expertID  , showDel);
					}
				}
			];			
			return columns;
		}
		
		function getMinColumns(){
			var columns = [
				{
					template : function(item) {
						return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",item.expertID)
					},
					filterable : false,
					width : 25,
					title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
				},
				{
					field : "name",
					title : "姓名",
					width : 60,
					filterable : false,
                    template: function (item) {
                        return '<a  ng-click="vm.findExportDetail(\''+item.expertID+'\')">'+item.name+'</a>'
                    }
				},
				{
					field : "degRee",
					title : "学位",
					width : 50,
					filterable : false
				},
                {
                    field : "post",
                    title : "职称",
                    width : 70,
                    filterable : false
                },
                {
                    field : "applyDate",
                    title : "录入时间",
                    width : 70,
                    filterable : false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field : "compositeScore",
                    title : "专家星级",
                    width : 70,
                    filterable : false
                },
				{
					field : "comPany",
					title : "工作单位",
					width : 120,
					filterable : false
				}
			];
			return columns;
		}
				
		//S_auditGrid
		function auditGrid(vm){
			var dataSource1 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{$filter:"state eq '1'"},true),
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
				pageSize : 25,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			
			var dataSource2 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{$filter:"state eq '2'"},true),
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
				pageSize : 5,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});						
			
			var dataSource3 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{$filter:"state eq '3'"},true),
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
				pageSize : 5,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			
			var dataSource4 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{$filter:"state eq '4'"},true),
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
				pageSize : 5,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			
			var dataSource5 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{$filter:"state eq '0'"},true),
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
				pageSize : 5,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			
			var  dataBound = function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } 
			
			vm.gridOptions1 = {
				dataSource : common.gridDataSource(dataSource1),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getMinColumns(),
				dataBound:dataBound,
				resizable : true
			};
			
			vm.gridOptions2 = {
				dataSource : common.gridDataSource(dataSource2),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getMinColumns(),
				dataBound:dataBound,
				resizable : true
			};
			
			vm.gridOptions3 = {
				dataSource : common.gridDataSource(dataSource3),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getMinColumns(),
				dataBound:dataBound,
				resizable : true
			};
			
			vm.gridOptions4 = {
					dataSource : common.gridDataSource(dataSource4),
					filterable : common.kendoGridConfig().filterable,
					pageable : common.kendoGridConfig().pageable,
					noRecords : common.kendoGridConfig().noRecordMessage,
					columns : getMinColumns(),
					dataBound:dataBound,
					resizable : true
				};
			
			vm.gridOptions5 = {
				dataSource : common.gridDataSource(dataSource5),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getMinColumns(),
				dataBound:dataBound,
				resizable : true
			};
		}//E_auditGrid				
		
		//S_searchAudit
		function searchAudit(vm){
            vm.gridOptions1.dataSource._skip=0;
            vm.gridOptions2.dataSource._skip=0;
            vm.gridOptions3.dataSource._skip=0;
            vm.gridOptions4.dataSource._skip=0;
            vm.gridOptions5.dataSource._skip=0;

			vm.gridOptions1.dataSource.read();
			vm.gridOptions2.dataSource.read();
			vm.gridOptions3.dataSource.read();
			vm.gridOptions4.dataSource.read();
			vm.gridOptions5.dataSource.read();
		}//S_endAudit
		
		//S_repeatGrid
		function repeatGrid(vm){
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findRepeatByOData"),
				schema : common.kendoGridConfig().schema({
					id : "id"
				}),
				rowNumber: true,  
	            headerCenter: true
			});
			
			var  dataBound = function () {  
                var rows = this.items();   
                $(rows).each(function (i) {                    	
                     $(this).find(".row-number").html(i+1);                   
                });  
            } 
						
			// End:column
			vm.repeatGridOptions = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getExpertColumns(vm),
				dataBound:dataBound,
				resizable : true
			};
		}//E_repeatGrid
		
		//S_toAudit
		function toAudit(vm,flag){
        	var selectIds = common.getKendoCheckId('#grid'+flag);
        	if (selectIds.length == 0) {
        		common.alert({
        			vm:vm,
        			msg:'请选择数据'
        		});
        	}else{
        		var ids=[];
        		for (var i = 0; i < selectIds.length; i++) {
        			ids.push(selectIds[i].value);
        		}  
        		var idStr=ids.join(',');
        		updateAudit(vm,idStr,1);
        	}
        }//E_toAudit
		 
		//S_auditTo
		function auditTo(vm,flag){
			var selectIds = common.getKendoCheckId('#grid1');
	       if (selectIds.length == 0) {
	       	common.alert({
	           	vm:vm,
	           	msg:'请选择数据'
	           });
	       }else{
	       	var ids=[];
	           for (var i = 0; i < selectIds.length; i++) {
	           	ids.push(selectIds[i].value);
				}  
	           var idStr=ids.join(',');
	           updateAudit(vm,idStr,flag);
	       }
	   }//E_auditTo
		
		//begin updateAudit
		function updateAudit(vm,ids,flag){
			vm.isSubmit = true;
			var httpOptions = {
				method : 'post',
				url : url_expert+"/updateAudit",
				params:{
					ids:ids,
					flag:flag
				}		
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.isSubmit = false;
						vm.gridOptions1.dataSource.read();
						vm.gridOptions2.dataSource.read();
						vm.gridOptions3.dataSource.read();
						vm.gridOptions4.dataSource.read();
						vm.gridOptions5.dataSource.read();
						common.alert({
							vm : vm,
							msg : "操作成功"
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
		}//end updateAudit

        //S_导出综合查询的excel功能
        function exportToExcel(vm){
            var httpOptions ={
                method : 'post',
                url : rootPath+"/expert/exportToExcel",
                responseType: 'arraybuffer',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                data: {$filter:common.buildOdataFilter($("#searchform")) },
                transformRequest: function(obj) {
                    var str = [];
                    for(var p in obj){
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    }
                    return str.join("&");
                }
            }
            var httpSuccess = function success(response){
                var blob = new Blob([response.data] , {type : "application/vnd.ms-excel"});
                FileSaver.saveAs(blob, "专家信息.xls");
            }
            common.http({
                $http : $http ,
                httpOptions : httpOptions,
                success : httpSuccess
            });

        }//E_exportToExcel

        //S_专家抽取统计
        function expertSelectHis(epSelHis,callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/expert/expertSelectHis",
                data : epSelHis
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess,
            });
        }//E_expertSelectHis

        //S_专家评分统计
        function expertScoreHis(epSelHis,callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/expert/expertScoreHis",
                data : epSelHis
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess,
            });
        }//E_expertScoreHis

		//begin reviewProjectGrid
		function reviewProjectGrid(expertId,callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/expert/reviewProject",
                params : {
                    expertId :expertId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess,
            });

            /*// Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/expert/reviewProject?expertId=" + vm.expertId),
                schema: common.kendoGridConfig().schema({
                    id: "signid",
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
            //S_序号
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
            //S_序号
            // Begin:column
            var columns = [
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable : false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "projectname",
                    title: "项目名称",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "builtcompanyname",
                    title: "建设单位",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "signdate",
                    title: "签收日期",
                    width: 100,
                    filterable: false,
                    format: "{0:yyyy/MM/dd}"
                },{
					field : "",
					title :"操作",
					width : 100 ,
                    filterable: false,
					template : function(item){
                        return common.format($('#columnBtns2').html(),"vm.queryDetail('" + item.signid + "','"+ item.processInstanceId+"')");
					}
				}
            ];
            // End:column

            vm.reviewProjectOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound:dataBound,
                resizable: true
            };*/
		}
		//end reviewProjectGrid

		//begin queryExpertDetail
		function queryExpertDetail(vm , expertId){
            vm.expert = [];
			vm.id = expertId;
            getExpertById(vm.id, function (data) {
                vm.expert = data;
                $("#queryExportDetailsS").kendoWindow({
                    width: "80%",
                    height: "620px",
                    title: "专家详细信息",
                    visible: false,
                    modal: true,
                    open:function(){
                        $("#expertPhotoSrc").attr("src", rootPath + "/expert/transportImg?expertId=" + vm.expert.expertID + "&t=" + Math.random());
                        //tab标签
                        $("#busi_baseinfoS").addClass("active").addClass("in").show(500);

                        $('#myTabExpertS li').click(function (e) {
                            $("#busi_baseinfoS").removeClass("active").removeClass("in");
                            $("#busi_workplanS").removeClass("active").removeClass("in");
                            var aObj = $("a", this);
                            e.preventDefault();
                            aObj.tab('show');
                            var showDiv = aObj.attr("for-div");
                            // $("#" + showDiv).removeClass("active").removeClass("in");
                            $("#" + showDiv).addClass("active").addClass("in").show(500);
                        })
                        //评审过项目
                        vm.reviewProjectList2 = [];
                        reviewProjectGrid(vm.id,function(data){
                            vm.isLoading = false;
                            if(data && data.length > 0){
                                vm.reviewProjectList2 = data;
                                vm.noData = false;
                            }else{
                                vm.noData = true;
                            }

                        });
                    },
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            });
		}
		//end queryExpertDetail
	}

})();