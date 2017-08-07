(function () {
    'use strict';

    angular.module('app').factory('registerFileSvc', registerFile);

    registerFile.$inject = ['$rootScope', '$http'];

    function registerFile($rootScope, $http) {
		var addregister_url = rootPath+"/addRegisterFile";
        var service = {
        	grid:　grid,
        }
        return service;
        
        
       
        // begin#grid
        function grid(vm) {
            // Begin:dataSource
             var dataSource = new kendo.data.DataSource({
                            transport: {
                                type: 'odata',
                                read:  {
                                    url: addregister_url + "/findByOData",
                                    dataType: "json",
                                    type: "post"
                                },
                                update: {
                                    url: addregister_url + "/update",
                                    dataType: "json",
                                    type: "post",
                                    contentType: "application/json"
                                },
                                destroy: {
                                    url: addregister_url + "/delete",
                                    dataType: "json",
                                    type: "post",
                                    contentType: "application/json"
                                },
                                create:{
                                    url: addregister_url + "/create",
                                    dataType: "json",
                                    type: "post",
                                },
                                parameterMap: function(options, operation) {
                                    if (operation == "create") {
                                        return {signid:vm.model.signid,models:kendo.stringify(options.models)};
                                    }
                                    if (operation == "update" || operation == "destroy") {
                                        return kendo.stringify(options.models);
                                    }
                               }
                            },
                            batch: true,
                            pageSize: 10,
                    schema: common.kendoGridConfig().schema({
	                    id: "id",
	                    fields: {
	                    	fileName: { nullable: true,type: "String"  },
	                        totalNum: { type: "number",validation: { required: true } },
	                        isHasOriginfile: { type: "boolean" },
	                        isHasCopyfile: { type: "boolean" },
	                        suppleDeclare: { type: "string" },
	                        suppleDate: { type: "date"},
	                        createdDate: {type: "date"},
	                        modifiedDate: {type: "date"}
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
                  
                var columns = [
                			 {
			                    field: "",
			                    title: "序号",
			                    width: 50,
			                    filterable: false,
			                    template: "<span class='row-number'></span>"
               				 },
                            {
                            	field: "fileName", 
                            	title: "资料名称", 
                            	width: "120px" 
                            },
                            {
                            	field: "totalNum", 
                            	title:"份数",
                            	width: "120px"
                            },
                            { 
                            	field:"isHasOriginfile" ,
                            	title:"原件",
                            	width: "120px",
                            	template: function(item){
                            		if(item.isHasOriginfile){
                            			return "是";
                            		}else{
                            			return "否";
                            		}
                            	}
                            },
                            { 
                            	field:"isHasCopyfile" ,
                            	title:"复印件",
                            	width: "120px",
                            	template: function(item){
                            		if(item.isHasCopyfile){
                            			return "是";
                            		}else{
                            			return "否";
                            		}
                            	}
                            },
                            { 
                            	field:"suppleDeclare" ,
                            	title:"补充说明",
                            	width: "120px"
                            },
                            { 
                            	field:"suppleDate" ,
                            	title:"补充日期",
                            	width: "120px",
                            	format:"{0:yyyy-MM-dd}" 
                            }
                            ,{ 
                            	command: ["destroy"],
                            	title: "&nbsp;", 
                            	width: "250px" 
                            }
            ];//列
            
             //S_序号
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }
			/*  vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                toolbar: ["新增"],
                columns: [
                            "ProductName",
                            { field: "UnitPrice", title: "Unit Price", format: "{0:c}", width: "120px" },
                            { field: "UnitsInStock", title:"Units In Stock", width: "120px" },
                            { field: "Discontinued", width: "120px" },
                            { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                //dataBound: dataBound,
                //resizable: true,
                editable: "inline"
              };*/
              $("#grid").kendoGrid({
                        dataSource: dataSource,
                        filterable: common.kendoGridConfig().filterable,
                        pageable: common.kendoGridConfig().pageable,
                        toolbar: ["create", "save", "cancel"],
                        columns:columns,
                        dataBound:dataBound,
                        navigatable: true,
                        editable: true,
                        resizable: true
                    });
        }// end fun grid

    }
})();