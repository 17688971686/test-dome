(function () {
    'use strict';

    angular.module('app').factory('addSuppLetterQuerySvc', addSuppLetterQuery);
    addSuppLetterQuery.$inject = ['$http', 'bsWin'];
    function addSuppLetterQuery($http, bsWin) {
        var url_addSuppLetterQuery = rootPath + "/addSuppLetter", url_back = '#/addSuppLetterQueryList';
        var service = {
        	approveGrid: approveGrid,//补充资料函审批列表
        	addQueryGrid:addQueryGrid,//补充资料函查询列表
            deleteaddSuppLetterQuery: deleteaddSuppLetterQuery,
            updateaddSuppLetterQuery: updateaddSuppLetterQuery,
            getaddSuppLetterQueryById: getaddSuppLetterQueryById, //根据ID查看拟补充资料函
            createaddSuppLetterQuery: createaddSuppLetterQuery,   //保存补充资料函
            initSuppLetter: initSuppLetter,             //初始化补充资料函
            createFilenum: createFilenum,               //生成文件字号
            initSuppListDate: initSuppListDate,         //初始化拟补充资料函列表
        };

        return service;

        //S 初始化拟补充资料函列表
        function initSuppListDate(businessId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/addSuppLetterQuery/initSuppListDate",
                params: {
                    businessId: businessId,
                }
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //E 初始化拟补充资料函列表
        //S 生成文件字号 
        function createFilenum(id,callBack) {
            var httpOptions = {
                method: 'post',
                url: url_addSuppLetterQuery + "/createFileNum",
                params: {id: id}
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //E 生成文件字号


        //S 初始化补充资料函
        function initSuppLetter(businessId,businessType,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/addSuppLetterQuery/initSuppLetter",
                params: {
                    businessId: businessId,
                    businessType:businessType,
                }
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //E 初始化补充资料函

        // begin#updateaddSuppLetterQuery
        function updateaddSuppLetterQuery(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_addSuppLetterQuery,
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

        // begin#deleteaddSuppLetterQuery
        function deleteaddSuppLetterQuery(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_addSuppLetterQuery,
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
                            closeDialog: true,
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

        // begin#createaddSuppLetterQuery
        function createaddSuppLetterQuery(suppletter, isSubmit,callBack) {
            isSubmit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/addSuppLetterQuery/save",
                data: suppletter
            };
            var httpSuccess = function success(response) {
                isSubmit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function(){isSubmit = false}
            });


        }

        // begin#getaddSuppLetterQueryById
        function getaddSuppLetterQueryById(vm, id) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/addSuppLetter/findById",
                params: {id: vm.id}
            };
            var httpSuccess = function success(response) {
                vm.suppletter = response.data;
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

     // begin#补充资料函查询列表
        function addQueryGrid(vm){
        	// Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_addSuppLetterQuery+"/addsuppListData"),
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
                    field: "title",
                    title: "文件标题",
                    width: 180,
                    filterable: false
                },
                {
                    field: "orgName",
                    title: "拟稿部门",
                    width: 100,
                    filterable: false
                },
                {
                    field: "userName",
                    title: "拟稿人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "suppLetterTime",
                    title: "拟稿时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
              
                {
                    field: "filenum",
                    title: "文件字号",
                    width: 100,
                    filterable: false
                },
              
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            item.id);
                    }
                }
            ];
            // End:column

            vm.queryGridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };

        }
        //end# 补充资料函查询列表
        
        
        // begin#补充资料函审批列表
        function approveGrid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_addSuppLetterQuery+"/addSuppApproveList"),
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
                    field: "title",
                    title: "文件标题",
                    width: 100,
                    filterable: false
                },
                {
                    field: "orgName",
                    title: "拟稿部门",
                    width: 100,
                    filterable: false
                },
                {
                    field: "userName",
                    title: "拟稿人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "suppLetterTime",
                    title: "拟稿时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
              
                {
                    field: "filenum",
                    title: "文件字号",
                    width: 100,
                    filterable: false
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