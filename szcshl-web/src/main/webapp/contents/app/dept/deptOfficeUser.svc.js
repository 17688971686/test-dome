(function () {
    'use strict';

    angular.module('app').factory('deptOfficeUserSvc', dept);

    dept.$inject = ['$http'];

    function dept($http) {
        var url_dept = rootPath + "/dept", url_back = '#/deptList';
        var url_deptOfficeUsers =rootPath+"/dept/getDeptOfficeUsers";		//在办事处成员
        var url_deptNotInOfficeUser =rootPath+"/dept/NotInoDeptfficeUsers";	//不在办事处成员
        var service = {
            grid: grid,									//办事处列表
            getdeptById: getdeptById,
            createdept: createdept,
            deletedeptOfficeUser: deletedeptOfficeUser,  //移除成员
            updatedept: updatedept,
            getDepts:getDepts,						     //获取所有办事处
            deptOfficeGrid :deptOfficeGrid,              //办事处人员列表
            addOfficeUser:addOfficeUser,					     //添加成员
        };

        return service;
        //start# 继续添加成员
        function addOfficeUser(vm,officeId){
        	 var httpOptions = {
                     method: 'post',
                     url:rootPath+"/dept/addOfficeUser",
                     params:{
                     	deptId:vm.id,
                     	officeId: officeId
                     }                
                 }
                 
                 var httpSuccess = function success(response) {              
                     common.requestSuccess({
     					vm:vm,
     					response:response,
     					fn:function () {
     						vm.deptOfficeGrid.dataSource.read();
     	                    vm.gridOptions.dataSource.read();	                   
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
        //end# 添加成员
        
        //start 获取所有办事处
        function getDepts(vm){
        	var httpOptions = {
                    method: 'get',
                    url: common.format(url_dept + "/getDepts")
                }
                var httpSuccess = function success(response) {
                    vm.depts = {};
                    vm.depts = response.data;
//                    console.log(vm.depts);
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        	
        }
        //end 获取所有办事处
        
        // begin#updatedept
        function updatedept(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

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
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
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

            } else {
                // common.alert({
                // vm:vm,
                // msg:"您填写的信息不正确,请核对后提交!"
                // })
            }

        }

        // begin#deletedeptOfficeUser
        function deletedeptOfficeUser(vm, officeId) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url:rootPath+"/dept/deleteOfficeUsers",
               params:{
            	   deptId:vm.id,
            	   officeId:officeId
            
               }
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

        // begin#createdept
        function createdept(vm) {
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

        // begin#getdeptById
        function getdeptById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/dept/html/findById",
                params:{officeID:vm.officeID}
            };
            var httpSuccess = function success(response) {
            	if(response.data.dept){
					vm.depts = {}
					//vm.depts = response.data.depts;
					//console.log(vm.depts);
					
				}
                vm.model = response.data;
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });                       
        }

        // begin#查看人员列表
        function deptOfficeGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_deptNotInOfficeUser+"?deptId="+vm.id),
                schema: common.kendoGridConfig().schema({
                    id: "id",
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

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.officeID)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                
                {
                    field: "officeUserName",
                    title: "负责人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "officePhone",
                    title: "电话",
                    width: 100,
                    filterable: false
                },
               
                {
                    field: "officeDesc",
                    title: "描述",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#allUserGridBtns').html(),
                            "vm.addOfficeUser('" + item.officeID + "')", item.officeID);
                    }
                }
            ];
            // End:column

            vm.deptOfficeGrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };

        }// end fun 查看人员列表
        
        //start 办事处列表
        function grid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_deptOfficeUsers+"?deptId="+vm.id),
                schema: common.kendoGridConfig().schema({
                    id: "id",
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

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.officeID)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
               
                {
                    field: "officeUserName",
                    title: "负责人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "officePhone",
                    title: "电话",
                    width: 100,
                    filterable: false
                },
               
                {
                    field: "officeDesc",
                    title: "描述",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.remove('" + item.officeID + "')", item.officeID);
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

        }// end 办事处列表

    }
})();