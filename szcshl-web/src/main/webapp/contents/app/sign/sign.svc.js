(function() {
	'use strict';
	
	angular.module('app').factory('signSvc', sign);
	
	sign.$inject = ['$http','$state'];

	function sign($http,$state) {
		var service = {
			grid : grid,						//初始化项目列表
			querySign : querySign,
			initDicListData : initDicListData,
			createSign : createSign,
			updateSign : updateSign,
			initFillData : initFillData,		//初始化表单填写页面
			completeFill : completeFill,		//提交表单填写
			flowgrid : flowgrid				//初始化待处理页面
		};
		return service;			
		
		//S_初始化grid
		function grid(vm){
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/sign"),
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
						template : function(item) {
							return kendo
									.format(
											"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
											item.signid)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

					},
					{
						field : "projectname",
						title : "项目名称",
						width : 200,
						filterable : true
					},
					{
						field : "projectcode",
						title : "项目编号",
						width : 200,
						filterable : true
					},
					{
						field : "createdDate",
						title : "创建时间",
						width : 180,
						filterable : false,
						format : "{0:yyyy/MM/dd HH:mm:ss}"

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
		}//E_初始化grid
		
		//S_查询grid
		function querySign(vm){
			var filterParam = common.buildOdataFilter($("#searchform"));
			var httpOptions = {
				method : 'get',
				url : common.format(rootPath+"/sign?"+filterParam) 
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
			};
			
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}//E_查询grid
		
		//S_初始化数字字典
		function initDicListData(vm){
			var httpOptions = {
				method : 'get',
				url : rootPath+"/dict/dictItems",
				params:{
					dictCode:'PRO_STAGE'
				}
			};
			
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {						
						vm.dicList = {};
						vm.dicList = response.data;					
					}
				});
			};
			
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}//E_初始化数字字典  
		
		//S_创建收文
		function createSign(vm){
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
				var httpOptions = {
						method : 'post',
						url : rootPath+"/sign",
						data : vm.model
					}
					var httpSuccess = function success(response) {									
						common.requestSuccess({
							vm:vm,
							response:response,
							fn:function() {				
								common.alert({
									vm:vm,
									msg:"操作成功,请继续填写报审登记表！",
									fn:function() {
										$state.go('fillSign', {signid: response.data});
									}
								})
							}						
						});
					}
					common.http({
						vm:vm,
						$http:$http,
						httpOptions:httpOptions,
						success:httpSuccess
					});
			}
		}//E_创建收文
		
		//S_更新
		function updateSign(vm){
			var updateUrl = rootPath+"/sign";			
			if("fillcomplete" == vm.putType){
				updateUrl = rootPath+"/sign/html/completeFill";
			}
				
			var httpOptions = {
				method : 'put',
				url : updateUrl,
				data : vm.model
			}

			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {						
						common.alert({
							vm:vm,
							msg:"操作成功",
							fn:function() {
								$('.alertDialog').modal('hide');							
							}
						})
					}
					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//E_更新
		
		//S_初始化填报页面数据
		function initFillData(vm){
			var paramsValue = {} ;			
			paramsValue.signid = vm.model.signid;
			if(vm.flow.taskId){
				paramsValue.taskId = vm.flow.taskId; 
			}
			var httpOptions = {
					method : 'get',
					url : rootPath+"/sign/html/initFillPageData",
					params : paramsValue						
				}

			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {						
						//初始化未完成
						//alert(response.data.sign.signid);	
						vm.model = response.data.sign;		
					}
					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//E_初始化填报页面数据
		
		//S_填报完成
		function completeFill(vm){
			vm.putType = "fillcomplete"
			updateSign(vm);
		}//E_填报完成
		
		//S_初始化待处理页面
		function flowgrid(vm){
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/sign/html/initflow"),
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
						
			var columns = [
				 {
                     field: "",
                     title: "序号",
                     template: "<span class='row-number'></span>",
                     width:30
                 },
				{
					field : "projectname",
					title : "项目名称",
					width : 200,
					filterable : true
				},
				{
					field : "projectcode",
					title : "项目编号",
					width : 200,
					filterable : true
				},
				{
					field : "createdDate",
					title : "创建时间",
					width : 180,
					filterable : false,
					format : "{0:yyyy/MM/dd HH:mm:ss}"

				},
				{
					field : "",
					title : "操作",
					width : 180,
					template:function(item){							
						return common.format($('#columnBtns').html(),item.signid,item.taskId,item.processInstanceId);
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
		}//E_初始化待处理页面										
	}		
})();