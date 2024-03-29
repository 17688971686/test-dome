(function () {
    'use strict';

    angular.module('app').factory('officeUserSvc', officeUser);

    officeUser.$inject = ['$http','bsWin'];

    function officeUser($http,bsWin) {
        var url_officeUser = rootPath + "/officeUser", url_back = '#/officeUserList';
        var service = {
            grid: grid,
            getOfficeUserById: getOfficeUserById,
            createOfficeUser: createOfficeUser,
            deleteOfficeUser: deleteOfficeUser,
            updateOfficeUser: updateOfficeUser,
            getDepts:getDepts,						//获取所有办事处
            queryOffice:queryOffice,
        };

        return service;
        
        function queryOffice(vm){
            vm.gridOptions.dataSource._skip=0;
        	vm.gridOptions.dataSource.read();	
        }
        //start 获取所有办事处
        function getDepts(vm){
        	var httpOptions = {
                    method: 'get',
                    url: common.format(url_officeUser + "/getDepts")
                }
                var httpSuccess = function success(response) {
                    vm.depts = {};
                    vm.depts = response.data;
                    
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        	
        }
        //end 获取所有办事处
        
        // begin#updateOfficeUser
        function updateOfficeUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id
                var httpOptions = {
                    method: 'put',
                    url: url_officeUser,
                    data: vm.model
                }

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            bsWin.alert("操作成功");
                            vm.isSubmit = false;
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

        // begin#deleteOfficeUser
        function deleteOfficeUser(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_officeUser,
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        bsWin.alert("操作成功");
                        vm.isSubmit = false;
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

        // begin#createOfficeUser
        function createOfficeUser(vm) {
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: 'post',
                    url: url_officeUser,
                    data: vm.model
                };
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            bsWin.alert("操作成功");
                            vm.isSubmit = false;
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

        // begin#getOfficeUserById
        function getOfficeUserById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/officeUser/html/findById",
                params:{officeID:vm.officeID}
            };
            var httpSuccess = function success(response) {
            	if(response.data.dept){
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

        // begin#grid
        function grid(vm) {

            // Begin:dataSource
            var dataSource = common.kendoGridDataSource(url_officeUser + "/fingByOData",$("#formOffice"),vm.queryParams.page,vm.queryParams.pageSize,vm.gridParams);
            // End:dataSource

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.officeID)
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
                    field: "officeUserName",
                    title: "负责人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "deptName",
                    title: "所在办事处",
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
                    field: "officeEmail",
                    title: "邮件",
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
                            "vm.del('" + item.officeID + "')", item.officeID);
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
                dataBound:common.kendoGridConfig(vm.queryParams).dataBound,
                resizable: true
            };

        }// end fun grid

    }
})();