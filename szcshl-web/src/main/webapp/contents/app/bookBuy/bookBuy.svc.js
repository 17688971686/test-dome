(function () {
    'use strict';

    angular.module('app').factory('bookBuySvc', bookBuy);

    bookBuy.$inject = ['$http'];

    function bookBuy($http) {
        var url_bookBuy = rootPath + "/bookBuy", url_back = '#/bookBuyList';
        var service = {
            grid: grid,  //图书查询列表
            bookBorrowGrid: bookBorrowGrid, //借书列表
            getBookBuyById: getBookBuyById,
            createBookBuy: createBookBuy,
            deleteBookBuy: deleteBookBuy,
            updateBookBuy: updateBookBuy,
            saveBorrowBookDetail: saveBorrowBookDetail  //保存借书信息
        };

        return service;

        //保存借书详细信息
        function saveBorrowBookDetail(vm,callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/bookBuy/saveBorrowDetail",
                data : vm.model
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "保存成功",
                            closeDialog: true,
                            fn: function () {
                                vm.isSubmit = false;
                                $('.alertDialog').modal('hide');
                            }
                        })
                    }

                })
            };
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }


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

        /**
         * 借书列表
         */
        function bookBorrowGrid(vm) {
            console.log(1111);
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/bookBorrow/findByOData", $("#bookForm")),
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

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.id)
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
                    field: "booksCode",
                    title: "图书编号",
                    width: 100,
                    filterable: false
                },
                {
                    field: "booksName",
                    title: "图书名称",
                    width: 100,
                    filterable: false
                },

                {
                    field: "bookBorrower",
                    title: "借书人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "borrowNum",
                    title: "借书数量",
                    width: 100,
                    filterable: false
                },
                {
                    field: "borrowDate",
                    title: "借书日期",
                    width: 100,
                    filterable: false
                },
                {
                    field: "returnDate",
                    title: "应还日期",
                    width: 100,
                    filterable: false
                },
                {
                    field: "storeTime",
                    title: "入库时间",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            item.id);
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
                resizable: true,
                selectable: "row"
            };
        }

        // begin#grid
        function grid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/bookBuy/findByOData", $("#bookForm"),{filter: "storeConfirm eq 'isNotNull' "}),
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

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.id)
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
                    field: "booksCode",
                    title: "图书编号",
                    width: 100,
                    filterable: false
                },
                {
                    field: "booksName",
                    title: "图书名称",
                    width: 100,
                    filterable: false
                },
                {
                    field: "booksType",
                    title: "图书分类",
                    width: 100,
                    filterable: false
                },
                {
                    field: "professionalType",
                    title: "专业类别",
                    width: 100,
                    filterable: false
                },
                {
                    field: "booksPrice",
                    title: "价格",
                    width: 100,
                    filterable: false
                },
                {
                    field: "bookNumber",
                    title: "数量",
                    width: 100,
                    filterable: false
                },
                {
                    field: "storeConfirm",
                    title: "库存",
                    width: 100,
                    attributes: {style: "color:red"},
                    filterable: false
                },
                {
                    field: "storeTime",
                    title: "入库时间",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            item.id);
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
                resizable: true,
                selectable: "row"
            };

        }// end fun grid
    }
})();