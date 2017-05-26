(function () {
    'use strict';

    angular.module('app').factory('deptSvc', dept);

    dept.$inject = ['$http'];

    function dept($http) {
        var url_dept = rootPath + "/dept", url_back = '#/listDept';
        var service = {
            grid: grid,
            getDeptById: getDeptById,
            createDept: createDept,
            deleteDept: deleteDept,
            updateDept: updateDept,
            queryDept:queryDept,
        };

        return service;
        function queryDept(vm){
        	vm.gridOptions.dataSource.read();	
        }
        // begin#updateDept
        function updateDept(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: 'put',
                    url: url_dept,
                    data: vm.model
                }

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog :true,
                                fn: function () {
                                    vm.isSubmit = false;
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            } 
        }

        // begin#deleteDept
        function deleteDept(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_dept,
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                    	common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog :true,
                            fn: function () {
                            	vm.isSubmit = false;
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#createDept
        function createDept(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_dept,
                    data: vm.model
                };

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog :true,
                                fn: function () {
                                    vm.isSubmit = false;
                                    location.href = url_back;
                                }
                            });
                        }
                    });
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            }
        }

        // begin#getDeptById
        function getDeptById(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/dept/html/findById",
                params:{deptId:vm.deptId}
            };
            var httpSuccess = function success(response) {
                if(response.data.offices){
                	vm.offices ={};
                	vm.offiecs = response.data.offices;
                }
                vm.model = response.data;
               // console.log(vm.model);
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        
        
        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_dept+"/fingByOData",$("#formDept")),
                schema: common.kendoGridConfig().schema({
                    id: "deptId",
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
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.deptId)
                    },
                    filterable: false,
                    width: 20,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {  
 				    field: "rowNumber",  
 				    title: "序号",  
 				    width: 30,
 				    filterable : false,
 				    template: "<span class='row-number'></span>"  
 				 }
 				,
                {
                    field: "deptName",
                    title: "办事处名称",
                    width: 100,
                    filterable: false
                },
                {
                    field: "deptUserName",
                    title: "办事处联系人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "address",
                    title: "办事处地址",
                    width: 100,
                    filterable: false
                },
                {
                    field: "deptType",
                    title: "类型",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "状态",
                    width: 100,
                    filterable: false,
                    template : function(item) {
						if(item.status){
							if(item.status == 5){
								return "正常"
							}else if(item.status == 7){
								return "已删除"
							}
						}else{
							return " "
						}
					}
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.deptId + "')", item.deptId,item.deptId);
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
            	dataBound :dataBound,
                resizable: true
            };

        }// end fun grid

    }
})();