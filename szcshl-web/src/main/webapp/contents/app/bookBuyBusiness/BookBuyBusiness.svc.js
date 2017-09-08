(function () {
    'use strict';

    angular.module('app').factory('bookBuyBusinessSvc', bookBuyBusiness);

    bookBuyBusiness.$inject = ['$http'];

    function bookBuyBusiness($http) {
        var url_bookBuyBusiness = rootPath + "/bookBuyBusiness", url_back = '#/bookBuyBusinessList';
        var service = {
            grid: grid,
            getBookBuyBusinessById: getBookBuyBusinessById,
            createBookBuyBusiness: createBookBuyBusiness,
            deleteBookBuyBusiness: deleteBookBuyBusiness,
            updateBookBuyBusiness: updateBookBuyBusiness,
            saveBookBuyBusinessDetail:saveBookBuyBusinessDetail
        };

        return service;

        // begin#updateBookBuyBusiness
        function updateBookBuyBusiness(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_bookBuyBusiness,
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

        // begin#deleteBookBuyBusiness
        function deleteBookBuyBusiness(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_bookBuyBusiness,
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

        // begin#createBookBuyBusiness
        function createBookBuyBusiness(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            var isValid = true;
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: 'post',
                    url: url_bookBuyBusiness,
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

        function saveBookBuyBusinessDetail(conditions,callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/bookBuyBusiness/saveBooksDetailList",
                headers:{
                    "contentType":"application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType : "json",
                data : angular.toJson(conditions)//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }

        // begin#getBookBuyBusinessById
        function getBookBuyBusinessById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/bookBuyBusiness/html/findById",
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
                transport: common.kendoGridConfig().transport(url_bookBuyBusiness),
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
                    field: "buyChannel",
                    title: "buyChannel",
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
                    field: "orgSLeaderId",
                    title: "orgSLeaderId",
                    width: 100,
                    filterable: true
                },
                {
                    field: "orgSLeader",
                    title: "orgSLeader",
                    width: 100,
                    filterable: true
                },
                {
                    field: "orgSLeaderHandlesug",
                    title: "orgSLeaderHandlesug",
                    width: 100,
                    filterable: true
                },
                {
                    field: "orgSLeaderDate",
                    title: "orgSLeaderDate",
                    width: 100,
                    filterable: true,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "orgMLeaderId",
                    title: "orgMLeaderId",
                    width: 100,
                    filterable: true
                },
                {
                    field: "orgMLeader",
                    title: "orgMLeader",
                    width: 100,
                    filterable: true
                },
                {
                    field: "orgMLeaderHandlesug",
                    title: "orgMLeaderHandlesug",
                    width: 100,
                    filterable: true
                },
                {
                    field: "orgMLeaderDate",
                    title: "orgMLeaderDate",
                    width: 100,
                    filterable: true,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "filerId",
                    title: "filerId",
                    width: 100,
                    filterable: true
                },
                {
                    field: "filer",
                    title: "filer",
                    width: 100,
                    filterable: true
                },
                {
                    field: "filerHandlesug",
                    title: "filerHandlesug",
                    width: 100,
                    filterable: true
                },
                {
                    field: "filerDate",
                    title: "filerDate",
                    width: 100,
                    filterable: true,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "bookBuyList",
                    title: "bookBuyList",
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