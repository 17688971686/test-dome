(function() {
	'expert strict';

	angular.module('app').factory('expertSvc', expert);

	expert.$inject = [ '$http'];
	
	//begin expert
	function expert($http) {
		var url_back = '#/expert';
		var url_expert = rootPath + "/expert";
		var service = {
			grid : grid,
			getExpertById : getExpertById,
			createExpert : createExpert,
			deleteExpert : deleteExpert,
			updateExpert : updateExpert,
			searchMuti : searchMuti,
			getDict : getDict,
			gridAudit:gridAudit,
			updateAudit:updateAudit,
			searchMAudit:searchMAudit,
			forward:forward,
			back:back
			
		};
		//end expert
		return service;
		//begin#back
		 //专家审核由各审核状态向待审核状态的改变
		 function back (vm,flag){
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
	        		updateAudit(vm,idStr,5);
	        	}
	        }
		 //begin#forward
		 //专家审核由待审核状态向各状态的改变
		function forward (vm,flag){
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
        }
		//begin updateAudit
		function updateAudit(vm,ids,flag){
			//alert(ids);
			vm.isSubmit = true;
			var httpOptions = {
				method : 'post',
				url : url_expert+"/updateAudit",
				//data : ids
				params:{
					id:ids,
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
		//end updateAudit
		
		// begin#updateExpert
		function updateExpert(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
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
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');
								}
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
		
		// begin#searchMuti
		function searchMuti(vm) {
			//vm.isSubmit = true;
			var url=common.buildOdataFilter($("#form"));
			//alert(url);
			var httpOptions = {
					method : 'get',
					url :url_expert+"?"+url
				}
			//alert(url);
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
		// end#searchMuti
		
		// begin#searchMAudit
		function searchMAudit(vm) {
			var url=common.buildOdataFilter($("#form"));
			//alert(url);
			var httpOptions = {
					method : 'get',
					url :url_expert+"?"+url+" and state eq '1'"
			}
			//alert(url_expert+"?"+url+" and state eq '1'");
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {						
						vm.gridOptions1.dataSource.data([]);
						vm.gridOptions1.dataSource.data(response.data.value);
						vm.gridOptions1.dataSource.total(response.data.count);
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
		// end#searchMAudit
		
		// begin#getDict
		function getDict(vm,dictCodes){
			//var param=new Array();
			//alert(dictCode);
			var httpOptions = {
					method : 'GET',
					url :rootPath +"/dict/dictMapItems",
					params:{
						dictCodes:dictCodes
					}
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.sex=response.data.SEX;
							vm.qualifiCations=response.data.QUALIFICATIONS;
							vm.degRee=response.data.DEGREE;
							vm.job=response.data.JOB;
							vm.title=response.data.TITLE;
							vm.expeRttype=response.data.EXPERTTYPE;
							vm.procoSttype=response.data.PROCOSTTYPE;
							vm.proteChtype=response.data.PROTECHTYPE;
							vm.expertrange=response.data.EXPERTRANGE;
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
		// end#getDict
		
		
		
		// begin#createUser
		
		function createExpert(vm) {	
			common.initJqValidation();
			var isValid = $('form').valid();
			if(isValid){
				vm.model.birthDay=$('#birthDay').val();
				vm.model.createDate=$('#createDate').val();
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
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.isSubmit = true;
									vm.isUpdate=false;
									vm.isHide=false;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									vm.model.expertID=response.data.expertID;
									 // $("#add").html(common.format($('#wk').html(),response.data.expertID));
									 // $("#addPj").html(common.format($('#pj').html(),response.data.expertID));
									  //location.href ="#/expertEdit/#";
									
								}
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
		// end#createUser
		
		
		
		// begin#getUserById
		function getExpertById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(url_expert + "?$filter=expertID eq '{0}'", vm.id)
			}
			var httpSuccess = function success(response) {
				vm.model = response.data.value[0];
				vm.work=response.data.value[0].work;
				vm.project=response.data.value[0].project;
				$('#birthDay').val(vm.model.birthDay);
				$('#createDate').val(vm.model.createDate);
				console.log(vm.model);
				if (vm.isUpdate) {
					//initZtreeClient(vm);
				}
			}

			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		
		function gridData(url){
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url),
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
			return dataSource;
		}
		
		
		// begin#gridAudit
		function gridAudit(vm,url,flag){
			// Begin:dataSource
			var  dataSource=gridData(url);
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
			// End:dataSource
			//alert("dfdsf");
			// Begin:column
		   var columns=[
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
				    width: 100,
				    template: "<span class='row-number'></span>"  
				    },
					{
						field : "name",
						title : "姓名",
						width : 100,
						filterable : false
					},
					{
						field : "sex",
						title : "性别",
						width : 50,
						filterable : false
					},
					{
						field : "expeRttype",
						title : "区域类别",
						width : 100,
						filterable : false
					},
					{
						field : "comPany",
						title : "工作单位",
						width : 100,
						filterable : false
					},
					{
						field : "userPhone",
						title : "手机",
						width : 100,
						filterable : false
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
					},
					{
						field : "maJor",
						title : "现从事专业",
						width : 100,
						filterable : false
					},
					{
						field : "acaDemy",
						title : "毕业院校",
						width : 100,
						filterable : false
					},
					];	
			// End:column
		   if(flag==1){
		   			vm.gridOptions1 = {
		   				dataSource : common.gridDataSource(dataSource),
		   				filterable : common.kendoGridConfig().filterable,
		   				pageable : common.kendoGridConfig().pageable,
		   				noRecords : common.kendoGridConfig().noRecordMessage,
		   				columns : columns,
		   				dataBound:dataBound,
		   				resizable : true
		   			};
		   }else if(flag==2){
			   vm.gridOptions2 = {
		   				dataSource : common.gridDataSource(dataSource),
		   				filterable : common.kendoGridConfig().filterable,
		   				pageable : common.kendoGridConfig().pageable,
		   				noRecords : common.kendoGridConfig().noRecordMessage,
		   				columns : columns,
		   				dataBound:dataBound,
		   				resizable : true
		   			};
		   }else if(flag==3){
			   vm.gridOptions3 = {
		   				dataSource : common.gridDataSource(dataSource),
		   				filterable : common.kendoGridConfig().filterable,
		   				pageable : common.kendoGridConfig().pageable,
		   				noRecords : common.kendoGridConfig().noRecordMessage,
		   				columns : columns,
		   				dataBound:dataBound,
		   				resizable : true
		   			};
		   }else if(flag==4){
			   vm.gridOptions4 = {
		   				dataSource : common.gridDataSource(dataSource),
		   				filterable : common.kendoGridConfig().filterable,
		   				pageable : common.kendoGridConfig().pageable,
		   				noRecords : common.kendoGridConfig().noRecordMessage,
		   				columns : columns,
		   				dataBound:dataBound,
		   				resizable : true
		   			};
		   }else if(flag==5){
			   vm.gridOptions5 = {
		   				dataSource : common.gridDataSource(dataSource),
		   				filterable : common.kendoGridConfig().filterable,
		   				pageable : common.kendoGridConfig().pageable,
		   				noRecords : common.kendoGridConfig().noRecordMessage,
		   				columns : columns,
		   				dataBound:dataBound,
		   				resizable : true
		   			};
		   }
		   
			//alert("dfdf");
		
		}// end#grid
		
		// begin#showWin
		/*function showWin(vm){
			var WorkeWindow = $("#test");
            WorkeWindow.kendoWindow({
                width: "1000px",
                height: "500px",
                title: "添加工作经历",
                visible: false,
                modal: true,
                closable: true,
                actions: [
                    "Pin",
                    "Minimize",
                    "Maximize",
                    "Close"
                ]             
            }).data("kendoWindow").center().open();
		}*/
		
		// begin#grid
		function grid(vm,url) {
			// Begin:dataSource
			var  dataSource=gridData(url);
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
			
			// End:dataSource
			//alert("dfdsf");
			// Begin:column
			var columns = [
					{
					template : function(item) {
						return kendo
								.format(
										"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
										item.expertID)
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
						field : "expeRttype",
						title : "专家类别",
						width : 100,
						filterable : true
					},
					{
						field : "",
						title : "操作",
						width : 100,
						template : function(item) {
							return common.format($('#columnBtns').html(),
									"vm.del('" + item.expertID + "')", item.expertID);
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
				dataBound:dataBound,
				resizable : true
			};
			//alert("dfdf");
		}// end fun grid

	}
})();