(function () {
    'use strict';

    angular.module('app').factory('assistUnitSvc', assistUnit);

    assistUnit.$inject = ['$http'];

    function assistUnit($http) {
        var url_assistUnit = rootPath + "/assistUnit";
        var url_back = '#/assistUnit';
        var service = {
            grid: grid,
            deleteAssistUnit : deleteAssistUnit,			//删除协审单位
            createAssistUnit : createAssistUnit,		//新增协审单位
            updateAssistUnit : updateAssistUnit,		//更新协审单位
            getAssistUnitById : getAssistUnitById,		//通过id查询协审单位
            queryAssistUnit : queryAssistUnit			//模糊查询
            
        };

        return service;
        
        function createAssistUnit(vm){
         	common.initJqValidation();
            var isValid = $('form').valid();
            if(isValid  && vm.isUnitExist==false){
        	var httpOptions={
        		method:"post",
        		url:url_assistUnit,
        		data:vm.assistUnit
        	}
        	var httpSuccess=function success(response){
        		common.requestSuccess({
        			vm:vm,
        			response:response,
        			fn:function(){
        				common.alert({
	        				vm:vm,
	        				msg:"操作成功",
	        				fn:function(){
	        					vm.isSubmit=false;
	        					$('.alertDialog').modal('hide');
	        					$('.modal-backdrop').remove();
	        					location.href=url_back;
	        					
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
        }
        
        function deleteAssistUnit(vm,id){
        	 vm.isSubmint=true;
        	var httpOptions={
        		method: 'delete',
        		url: url_assistUnit,
        		data: id
        	
        	};
        	
        	var httpSuccess=function success(response){
        		
        		common.requestSuccess({
        			vm:vm,
        			response:response,
        			fn:function(){
        			 vm.isSubmit=false;
        			 vm.gridOptions.dataSource.read();
        			}
        		});
        	
        	};
        	
        	common.http({
        		vm:vm,
        		$http:$http,
        		httpOptions:httpOptions,
        		success:httpSuccess
        	
        	});
        
        }//end create
        
        function updateAssistUnit(vm){
        	 common.initJqValidation();
            var isValid = $('form').valid();
            if(isValid && vm.isUnitExist==false){
        	var httpOptions={
        		method:"put",
        		url:url_assistUnit,
        		headers:{
                 "contentType":"application/json;charset=utf-8"  //设置请求头信息
              },
			  dataType : "json",
			  data:angular.toJson(vm.assistUnit)
//        		data:vm.assistUnit
        	}
        	
        	var httpSuccess=function success(response){
        		common.requestSuccess({
        			vm:vm,
        			response:response,
        			fn:function(){
        				common.alert({
	        				vm:vm,
	        				msg:"操作成功",
	        				fn:function(){
	        					vm.isSubmit=false;
	        					$('.alertDialog').modal('hide');
	        					$('.modal-backdrop').remove();
	        					location.href=url_back;
	        					
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
        }//end 
        
        function getAssistUnitById(vm){
        	
        	var httpOptions={
        		method:'get',
        		url: url_assistUnit+'/getAssistUnitById',
        		params:{id:vm.id}
        	}
        	
        	var httpSuccess=function success(response){
        		vm.assistUnit=response.data;
        	}
        	
        	common.http({
        		vm:vm,
        		$http:$http,
        		httpOptions:httpOptions,
        		success:httpSuccess
        	});
        }//end
        
        function queryAssistUnit(vm){
        	 vm.gridOptions.dataSource.read();
        }

     
        // begin#grid
        function grid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_assistUnit+"/fingByOData?$orderby=unitSort",$("#assistUnitform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        },
                        modifiedDate: {
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
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false
                },
                {
                    field: "unitName",
                    title: "单位名称",
                    width: 100,
                    filterable: true
                },
                {
                    field: "unitShortName",
                    title: "单位简称",
                    width: 100,
                    filterable: true
                },
                {
                    field: "phoneNum",
                    title: "电话号码",
                    width: 100,
                    filterable: false
                },
                {
                    field: "phoneNum",
                    title: "传真",
                    width: 100,
                    filterable: false
                },
                {
                    field: "principalName",
                    title: "负责人名称",
                    width: 100,
                    filterable: false
                },
                {
                    field: "principalPhone",
                    title: "负责人手机号",
                    width: 100,
                    filterable: false
                },
                {
                    field: "contactName",
                    title: "联系人名称",
                    width: 100,
                    filterable: false
                },
                {
                    field: "contactPhone",
                    title: "联系人手机号",
                    width: 100,
                    filterable: false
                },
                {
                    field: "address",
                    title: "企业地址",
                    width: 100,
                    filterable: true
                },
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
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };

        }// end fun grid

    }
})();