(function () {
    'use strict';

    angular.module('app').factory('assistUnitSvc', assistUnit);

    assistUnit.$inject = ['$http','$state','bsWin'];

    function assistUnit($http,$state,bsWin) {
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
                        bsWin.alert("操作成功",function(){$state.go("assistUnit");})
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
                        bsWin.alert("操作成功",function(){$state.go("assistUnit");})
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
             vm.gridOptions.dataSource._skip="";
        	 vm.gridOptions.dataSource.read();
        }

     
        // begin#grid
        function grid(vm) {

            // Begin:dataSource
  /*          var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_assistUnit+"/fingByOData",$("#assistUnitform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        },
                        modifiedDate: {
                        	type: "date"
                        },
                        isUse:{
                        },
                        unitSort:{
                        }
                        
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: [
                 {
	                    field: "unitSort",
	                    dir: "asc"
	                }
                ]
	               
            });*/
            var dataSource = common.kendoGridDataSource(url_assistUnit+"/fingByOData?$orderby=unitSort",$("#assistUnitform"),vm.queryParams.page,vm.queryParams.pageSize,vm.gridParams);
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
                    width: "20%",
                    filterable: false
                },
                {
                    field: "unitShortName",
                    title: "简称",
                    width: "8%",
                    filterable: false
                },
                {
                    field: "principalName",
                    title: "负责人",
                    width: "8%",
                    filterable: false
                },
                {
                    field: "principalPhone",
                    title: "负责人电话",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "contactName",
                    title: "联系人",
                    width: "8%",
                    filterable: false
                },
                {
                    field: "contactTell",
                    title: "联系人电话",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "address",
                    title: "企业地址",
                    width: "20%",
                    filterable: true
                },
                {
                    field: "isUse",
                    title: "状态",
                    width: "5%",
                    filterable: false,
                    template:function(item){
                    	if(item.isUse){
                    		if(item.isUse=="0"){
                    			return "已停用";
                    		}
                    		if(item.isUse=="1"){
                    			return "在用";
                    		}
                    	}else{
                    		return "";
                    	}
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: "8%",
                    template: function (item) {
                        return common.format($('#columnBtns').html(),"vm.del('" + item.id + "')", item.id,item.isUse);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                pageable : common.kendoGridConfig(vm.queryParams).pageable,
                resizable: true
            };

        }// end fun grid

    }
})();