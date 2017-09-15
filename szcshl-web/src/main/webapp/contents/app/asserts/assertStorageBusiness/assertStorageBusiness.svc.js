(function () {
    'use strict';

    angular.module('app').factory('assertStorageBusinessSvc', assertStorageBusiness);

    assertStorageBusiness.$inject = ['$http'];

    function assertStorageBusiness($http) {
        var url_assertStorageBusiness = rootPath + "/assertStorageBusiness", url_back = '#/assertStorageBusinessList';
        var service = {
            grid: grid,
            getAssertStorageBusinessById: getAssertStorageBusinessById,
            createAssertStorageBusiness: createAssertStorageBusiness,
            deleteAssertStorageBusiness: deleteAssertStorageBusiness,
            updateAssertStorageBusiness: updateAssertStorageBusiness
        };

        return service;

        // begin#updateAssertStorageBusiness
        function updateAssertStorageBusiness(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_assertStorageBusiness,
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

        // begin#deleteAssertStorageBusiness
        function deleteAssertStorageBusiness(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_assertStorageBusiness,
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

        // begin#createAssertStorageBusiness
        function createAssertStorageBusiness(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_assertStorageBusiness,
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

        // begin#getAssertStorageBusinessById
        function getAssertStorageBusinessById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/assertStorageBusiness/html/findById",
                params:{id:vm.id}
            };
            var httpSuccess = function success(response) {
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
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_assertStorageBusiness),
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
                            item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "businessId",
                    title: "businessId",
                    width: 100,
                    filterable: true
                },
                {
                    field: "businessName",
                    title: "businessName",
                    width: 100,
                    filterable: true
                },
                {
                    field: "applyDept",
                    title: "applyDept",
                    width: 100,
                    filterable: true
                },
                {
                    field: "operator",
                    title: "operator",
                    width: 100,
                    filterable: true
                },
                {
                    field: "applyReason",
                    title: "applyReason",
                    width: 100,
                    filterable: true
                },
                {
                    field: "orgDirectorId",
                    title: "orgDirectorId",
                    width: 100,
                    filterable: true
                },
                {
                    field: "orgDirector",
                    title: "orgDirector",
                    width: 100,
                    filterable: true
                },
                {
                    field: "orgDirectorDate",
                    title: "orgDirectorDate",
                    width: 100,
                    filterable: true,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "comprehensivehandlesug",
                    title: "comprehensivehandlesug",
                    width: 100,
                    filterable: true
                },
                {
                    field: "comprehensiveId",
                    title: "comprehensiveId",
                    width: 100,
                    filterable: true
                },
                {
                    field: "comprehensiveName",
                    title: "comprehensiveName",
                    width: 100,
                    filterable: true
                },
                {
                    field: "comprehensiveDate",
                    title: "comprehensiveDate",
                    width: 100,
                    filterable: true,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "leaderhandlesug",
                    title: "leaderhandlesug",
                    width: 100,
                    filterable: true
                },
                {
                    field: "leaderId",
                    title: "leaderId",
                    width: 100,
                    filterable: true
                },
                {
                    field: "leaderName",
                    title: "leaderName",
                    width: 100,
                    filterable: true
                },
                {
                    field: "leaderDate",
                    title: "leaderDate",
                    width: 100,
                    filterable: true,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "goodsDetailList",
                    title: "goodsDetailList",
                    width: 100,
                    filterable: true
                },
                {
                    field: "isFinishFiling",
                    title: "isFinishFiling",
                    width: 100,
                    filterable: true
                },
                {
                    field: "state",
                    title: "state",
                    width: 100,
                    filterable: true
                },
                {
                    field: "remark",
                    title: "remark",
                    width: 100,
                    filterable: true
                },
                {
                    field: "processInstanceId",
                    title: "processInstanceId",
                    width: 100,
                    filterable: true
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.businessId + "')", item.businessId);
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