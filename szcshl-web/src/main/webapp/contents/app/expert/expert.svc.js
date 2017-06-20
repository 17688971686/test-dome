(function() {
	'expert strict';

	angular.module('app').factory('expertSvc', expert);

	expert.$inject = [ '$http'];
	
	function expert($http) {
		var url_back = '#/expert';
		var url_expert = rootPath + "/expert";
		var service = {
			grid : grid,						//初始化综合查询grid
			auditGrid : auditGrid,				//初始化审核页面的所有grid
			getExpertById : getExpertById,		//通过ID查询专家信息详情
			createExpert : createExpert,        //创建专家信息
			deleteExpert : deleteExpert,        //删除专家信息
			updateExpert : updateExpert,        //更新专辑信息
			searchMuti : searchMuti,		    //综合查询
			searchAudit : searchAudit,		    //审核查询
			repeatGrid : repeatGrid,		    //重复专家查询
			updateAudit : updateAudit,		    //专家评审
			toAudit : toAudit,				    //由个状态回到审核状态
			auditTo : auditTo,				    //由审核状态去到各个状态
            initUpload : initUpload             //初始化附件上传
		};
		return service;				
		
		// begin#updateExpert
		function updateExpert(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (true) {
				vm.isSubmit = true;
				vm.model.expertID = vm.expertID;// id
				vm.model.birthDay= $("#birthDay").val();
				vm.model.createDate= $("#createDate").val();

				var httpOptions = {
					method : 'put',
					url : url_expert,
					data : vm.model
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.isSubmit = false;
							common.alert({
								vm : vm,
								msg : "操作成功"
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
			}
		}
		
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
				
		// begin#createExpert		
		function createExpert(vm) {	
			common.initJqValidation();
			var isValid = $('form').valid();
			if(isValid){				
				vm.isSubmit = true;												
				vm.model.birthDay=$('#birthDay').val();
				vm.model.graduateDate=$('#graduateDate').val();
				
				var httpOptions = {
					method : 'post',
					url : url_expert,
					data : vm.model
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.model.expertID = response.data.expertID;
							vm.isUpdate=true;
							vm.showBt=true;	
							vm.isSubmit = false;	
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
			}
		}
		// end#createExpert
						
		// begin#getExpertById
		function getExpertById(vm) {
			var httpOptions = {
				method : 'get',
				url : url_expert+"/findById",
				params:{
					id:vm.expertID
				}
			}
			var httpSuccess = function success(response) {
				vm.showBt = true;
				vm.model = response.data;
				//工作简历
				if(response.data.workDto && response.data.workDto.length > 0){
					vm.showWorkHistory = true;
					vm.work=response.data.workDto;
					
				}
				//项目经验
				if(response.data.projectDto && response.data.projectDto.length > 0){
					vm.projectkHistory = true;
					vm.project=response.data.projectDto;					
				}
				//专家聘书
				if(response.data.expertOfferDtoList && response.data.expertOfferDtoList.length > 0){
                    vm.showExpertOffer = true;
                    vm.expertOfferList = response.data.expertOfferDtoList;
                }
                initUpload(vm);
                $("#expertPhotoSrc").attr("src",rootPath+"/expert/transportImg?expertId="+vm.model.expertID+"&t="+Math.random());
			} 
			common.http({
				vm : vm,
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
					width : 100,
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
					width : 100,
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
					title : "职务",
					width : 100,
					filterable : true
				},
				{
					field : "idCard",
					title : "身份证号",
					width : 100,
					filterable : true
				},
				{
					field : "maJor",
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
					field : "",
					title : "专家类别",
					width : 100,
					filterable : true,
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '1'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '2'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '3'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '4'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '5'"}),
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
	            headerCenter: true,  
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

        //S_initUpload
        function initUpload(vm){
            var projectfileoptions = {
                language : 'zh',
                allowedPreviewTypes : ['image'],
                allowedFileExtensions : [ 'jpg', 'png', 'gif' ],
                maxFileSize : 2000,
                showRemove: false,
                uploadUrl:rootPath + "/expert/uploadPhoto",
                uploadExtraData:{expertId:vm.model.expertID}
            };
            $("#expertphotofile").fileinput(projectfileoptions).on("filebatchselected", function(event, files){

            }).on("fileuploaded", function(event, data) {
                $("#expertPhotoSrc").removeAttr("src");
                $("#expertPhotoSrc").attr("src",rootPath+"/expert/transportImg?expertId="+vm.model.expertID+"&t="+Math.random());
            });
        }//E_initUpload


	}
})();