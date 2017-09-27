(function() {
	'expert strict';

	angular.module('app').factory('expertSvc', expert);

	expert.$inject = [ '$http'];
	
	function expert($http) {
		var url_expert = rootPath + "/expert";
		var service = {
			grid : grid,						//初始化综合查询grid
			auditGrid : auditGrid,				//初始化审核页面的所有grid
			getExpertById : getExpertById,		//通过ID查询专家信息详情
			saveExpert : saveExpert,            //保存专家信息
			deleteExpert : deleteExpert,        //删除专家信息
			searchMuti : searchMuti,		    //综合查询
			searchAudit : searchAudit,		    //审核查询
			repeatGrid : repeatGrid,		    //重复专家查询
			updateAudit : updateAudit,		    //专家评审
			toAudit : toAudit,				    //由个状态回到审核状态
			auditTo : auditTo,				    //由审核状态去到各个状态
            formReset : formReset,				//重置页面
		};
		return service;	
		
		//begin formReset
		function formReset(vm){
			$("#searchform")[0].reset();
			vm.gridOptions.dataSource.read();
		}
		//end formReset
		
		// begin#deleteUser
		function deleteExpert(vm, id) {
			vm.isSubmit = true;
			var httpOptions = {
				method : 'delete',
				url : url_expert,
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
		// end#deleteUser
		
		// begin#search
		function searchMuti(vm) {
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
            });


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
			var  dataSource = common.kendoGridDataSource(rootPath+"/expert/findByOData",$("#searchform"));
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
						
			// End:column
			vm.gridOptions = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getExpertColumns(),
				dataBound:dataBound,
				resizable : true
			};
		}// end fun grid
		
		function getExpertColumns(){
			var columns = [
				{
					template : function(item) {
						return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",item.expertID)
					},
					filterable : false,
					width : 40,
					title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
				},
				{  
				    field: "rowNumber",  
				    title: "序号",  
				    width: 50,
				    template: "<span class='row-number'></span>"  
			    },
				{
					field : "name",
					title : "姓名",
					width : 80,
					filterable : true
				},
				{
					field : "sex",
					title : "性别",
					width : 50,
					filterable : true
				},
				{
					field : "degRee",
					title : "学位",
					width : 50,
					filterable : true
				},
				{
					field : "userPhone",
					title : "手机号码",
					width : 100,
					filterable : true
				},
				{
					field : "comPany",
					title : "工作单位",
					width : 100,
					filterable : true
				},
				{
					field : "degRee",
					title : "职称",
					width : 100,
					filterable : true
				},				
				{
					field : "majorWork",
					title : "现从事专业",
					width : 100,
					filterable : true
				},
				{
					field : "acaDemy",
					title : "毕业院校",
					width : 100,
					filterable : true
				},
				{
					field : "expertSort",
					title : "专家类别",
					width : 100,
					filterable : true
				},
				{
					field : "",
					title : "操作",
					width : 100,
					template : function(item) {
						return common.format($('#columnBtns').html(), "vm.del('" + item.expertID + "')", item.expertID);
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
					width : 100,
					filterable : false
				},
				{
					field : "degRee",
					title : "学位",
					width : 100,
					filterable : false
				},
				{
					field : "sex",
					title : "性别",
					width : 50,
					filterable : true
				},
				{
					field : "comPany",
					title : "工作单位",
					width : 100,
					filterable : false
				},
				{
					field : "degRee",
					title : "职务",
					width : 100,
					filterable : false
				},{
					field : "",
					title : "专家类别",
					width : 100,
					filterable : false,
					template:function(item){
						if(item.expertTypeDtoList){
							var resultStr='';
							for(var i=0;i<item.expertTypeDtoList.length;i++){
								if(i==0){
									resultStr += item.expertTypeDtoList[i].expertType;
								}else{
									
									resultStr += "、"+item.expertTypeDtoList[i].expertType;
								}
							}
							return resultStr;
						}else{
						 return "";
						}
					}
				}
			];
			
			return columns;
		}
				
		//S_auditGrid
		function auditGrid(vm){
			var dataSource1 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '1' and unable ne '1'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '2' and unable ne '1'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '3' and unable ne '1'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '4' and unable ne '1'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '5' and unable ne '1'"}),
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
				columns : getExpertColumns(),
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
	}
})();