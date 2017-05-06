(function () {
    'use strict';

    angular.module('app').factory('mytestSvc', mytest);

    mytest.$inject = ['$http'];

    function mytest($http) {
        var url_user = rootPath + "/mytest", url_back = '#/mytest';
        var service = {
            grid: grid,
            getMytestById: getMytestById,
            createMytest: createMytest,
            deleteMytest: deleteMytest,
            updateMytest: updateMytest
        };

        return service;

        // begin#updateMytest
        function updateMytest(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_user,
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

        // begin#deleteMytest
        function deleteMytest(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_user,
                data: id

            }
            var httpSuccess = function success(response) {

                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isSubmit = false;
                        vm.gridOptions.dataSource.read();
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

        // begin#createMytest
        function createMytest(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_user,
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
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    location.href = url_back;
                                }
                            })
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
        }

        // begin#getMytestById
        function getMytestById(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(url_user + "?$filter=id eq '{0}'", vm.id)
            };
            var httpSuccess = function success(response) {
                vm.model = response.data.value[0];
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
                transport: common.kendoGridConfig().transport(url_user),
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
                    field: "id",
                    title: "主键",
                    width: 100,
                    filterable: true
                },
                {
                    field: "testName",
                    title: "显示名",
                    width: 100,
                    filterable: true
                },
                {
                    field: "test01",
                    title: "test01",
                    width: 80,
                    filterable: false
                },
                {
                    field: "test02",
                    title: "test02",
                    width: 80,
                    filterable: false
                },
                {
                    field: "",
                    title: "创建时间",
                    width: 80,
                    filterable: false,
                    template: function(item) {
                        if(!item.createdDate){
                            return " ";
                        }
                        else{
                            return kendo.toString(new Date(item.createdDate), 'yyyy-MM-dd hh:mm:ss');
                        }
                    }
                },
                {
                    field: "",
                    title: "更新时间",
                    width: 80,
                    filterable: false,
                    template: function(item) {
                        if(!item.modifiedDate){
                            return " ";
                        }
                        else{
                            return kendo.toString(new Date(item.modifiedDate), 'yyyy-MM-dd hh:mm:ss');
                        }
                    }
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