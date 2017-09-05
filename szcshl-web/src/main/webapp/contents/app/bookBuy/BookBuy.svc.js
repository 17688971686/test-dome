(function () {
    'use strict';

    angular.module('app').factory('bookBuySvc', bookBuy);

    bookBuy.$inject = ['$http'];

    function bookBuy($http) {
        var url_bookBuy = rootPath + "/bookBuy", url_back = '#/bookBuyList';
        var service = {
            grid: grid,
            getBookBuyById: getBookBuyById,
            createBookBuy: createBookBuy,
            deleteBookBuy: deleteBookBuy,
            updateBookBuy: updateBookBuy
        };

        return service;

        // begin#updateBookBuy
        function updateBookBuy(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_bookBuy,
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

        // begin#deleteBookBuy
        function deleteBookBuy(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_bookBuy,
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

        // begin#createBookBuy
        function createBookBuy(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_bookBuy,
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

        // begin#getBookBuyById
        function getBookBuyById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/bookBuy/html/findById",
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
                transport: common.kendoGridConfig().transport(url_bookBuy),
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
                    title: "id",
                    width: 100,
                    filterable: true
                },
                {
                    field: "booksBarCode",
                    title: "booksBarCode",
                    width: 100,
                    filterable: true
                },
                {
                    field: "booksCode",
                    title: "booksCode",
                    width: 100,
                    filterable: true
                },
                {
                    field: "booksName",
                    title: "booksName",
                    width: 100,
                    filterable: true
                },
                {
                    field: "booksPrice",
                    title: "booksPrice",
                    width: 100,
                    filterable: true
                },
                {
                    field: "booksType",
                    title: "booksType",
                    width: 100,
                    filterable: true
                },
                {
                    field: "professionalType",
                    title: "professionalType",
                    width: 100,
                    filterable: true
                },
                {
                    field: "storePosition",
                    title: "storePosition",
                    width: 100,
                    filterable: true
                },
                {
                    field: "buyer",
                    title: "buyer",
                    width: 100,
                    filterable: true
                },
                {
                    field: "publishingCompany",
                    title: "publishingCompany",
                    width: 100,
                    filterable: true
                },
                {
                    field: "bookNo",
                    title: "bookNo",
                    width: 100,
                    filterable: true
                },
                {
                    field: "author",
                    title: "author",
                    width: 100,
                    filterable: true
                },
                {
                    field: "publishingTime",
                    title: "publishingTime",
                    width: 100,
                    filterable: true
                },
                {
                    field: "bookNumber",
                    title: "bookNumber",
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