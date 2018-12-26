(function() {
	'use strict';
	
	angular.module('app').factory('workdaySvc', workday);
	
	workday.$inject = ['$http','$state','bsWin'];

	function workday($http,$state,bsWin) {
		
		var url_workday=rootPath+'/workday';
		var url_back="#/workday"
		var service = {
			grid : grid,	//初始化数据
			createWorkday : createWorkday,	//新增工作日
			getWorkdayById : getWorkdayById,	//通过id查找该对象信息
			updateWorkday : updateWorkday,		//更新
			deleteWorkday : deleteWorkday,		//删除
			queryWorkday : queryWorkday,		//模糊查询
			clearValue : clearValue		//重置
			
			
		}
		
		return service;	
		
		//begin clearValue
		function clearValue(vm){
		var tab = $("#workdayForm").find('input,select');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
			
		vm.gridOptions.dataSource.read();
		}
		
		//begin getWorkdayById
		function getWorkdayById(vm){
		
			var httpOptions={
				method:'get',
				url :url_workday+"/getWorkdayById",
				params:{id:vm.id}
			}
			
			var httpSuccess=function success(response){
				vm.workday=response.data;
			}
			
			 common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
		}//end getWorkdayById
		
		function queryWorkday(vm){
			vm.gridOptions.dataSource.read();
		}
		
		//begin createWorkday
		function createWorkday(vm){
		    console.log(vm.workday);
			var httpOptions={
				method :'post',
				url : url_workday+"/createWorkday",
				data : vm.workday
			}
			var httpSuccess=function success(response){
				if(response.data.flag){
                    bsWin.alert("保存成功！", function(){
                        window.parent.$("#workDay").data("kendoWindow").close();
                        vm.gridOptions.dataSource.read();
                        vm.workday="";
                    });
				}else{
                    bsWin.alert(response.data.reMsg+"已存在，不能重复添加！");
				}


			}
			 common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
		
		
		}//end createWorkday
		
		//begin updateWorkday
		function updateWorkday(vm){
			var httpOptions={
				method: "put",
				url : url_workday,
				data : vm.workday
			}
			
			var httpSuccess=function success(response){
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function(){
						common.alert({
							vm: vm,
							msg:"操作成功",
							fn:function(){
								vm.isSubmit = false;
                                $('.alertDialog').modal('hide');
                                $('.modal-backdrop').remove();
                                location.href = url_back;
							}
						});
					}
				});
			}
			common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
		}
		//end updateWorkday
		
		
		//begin deleteWorkday
		function deleteWorkday(vm,id){
			var httpOptions={
				method:'delete',
				url:url_workday,
				data:id
			}
			
			var httpSuccess=function success(response){
				 vm.gridOptions.dataSource.read();
			}
			common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
			
		}
		//end deleteWorkday
		
		function grid(vm){
			//begin dataSource
			var dataSource=new kendo.data.DataSource({
				type:'odata',
				transport:common.kendoGridConfig().transport(url_workday+'/findByOdataObj',$("#workdayForm")),
				schema:common.kendoGridConfig().schema({
					id:'id',
					fields:{
						createdDate:{
							type:"date"
						},
						modifiedDate:{
							type:"date"
						}
					}
				
				}),
				serverPaging:true,
				serverSorting:true,
				serverFiltering:true,
				pageSize :10,
				sort:{
					field:"dates",
					dir:"desc"
				}

				
			});//end dataSource
			
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
            }; 
            //S_序号
			
			var columns=[
				{
				 template: function (item) {
                        return kendo
                            .format(
                                "<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                                item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

                },
                {  
				    field: "rowNumber",  
				    title: "序号",  
				    width: 50,
				    filterable : false,
				    template: "<span class='row-number'></span>"  
				 },
                {  
				    field: "dates",  
				    title: "时间",  
				    width: 200,
				    format:"{0:yyyy-MM-dd}",
				    filterable : false
				 },
                {  
				    field: "",  
				    title: "状态",  
				    width: 100,
				    filterable : false,
				    template:function(item){
				    	if(item.status){
					    	if(item.status=="1"){
					    		return "调休";
					    	}
					    	if(item.status=="2"){
					    		return "加班";
					    	}
				    	}else{
				    		return "";
				    	}
				    }
				 } ,
                {  
				    field: "remark",  
				    title: "备注",  
				    width: 140,
				    filterable : false
				 }
				 ,
                {  
				    field: "",  
				    title: "操作",  
				    width: 140,
				    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.id + "')", item.id);

                    }
				 }
			
			];
			vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound:dataBound,
                resizable: true,
            };

		
		}//end grid
		}
		
	})();