(function () {
    'use strict';

    angular.module('app').factory('orgUserSvc', org);

    org.$inject = ['$http', '$compile'];
    function org($http, $compile) {
        var user_userNotIn = rootPath + '/org/userNotIn';

        var service = {
            orgUserGrid: orgUserGrid,
            allUserGrid: allUserGrid,
            add: add,
            remove: remove
        };
        return service;

        //begin#remove
        function remove(vm, userId) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/org/deleteUsers",
                params: {
                    orgId: vm.id,
                    userId: userId
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
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

        //begin#add
        function add(vm, userId) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/org/addUsers",
                params: {
                    orgId: vm.id,
                    userId: userId
                }
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.orgUserGrid.dataSource.read();
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

        //begin#allUserGrid
        function allUserGrid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(user_userNotIn + "?orgId=" + vm.id),
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
                    field: "jobState",
                    dir: "desc"
                }
            });

            // End:dataSource

            // Begin:column
            var columns = [

                {
                    field: "loginName",
                    title: "登录名",
                    width: 200,
                    filterable: true
                },
                {
                    field: "displayName",
                    title: "显示名",
                    width: 200,
                    filterable: true
                },
                {
                    field: "remark",
                    title: "描述",
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 80,
                    template: function (item) {
                        return common.format($('#allUserGridBtns').html(),
                            "vm.add('" + item.id + "')");

                    }

                }

            ];
            // End:column

            vm.orgUserGrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };

        }

        //begin#orgUserGtid
        function orgUserGrid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/org/users?orgId=" + vm.id, $("#form")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: false,
                serverSorting: false,
                serverFiltering: false,
                pageSize: 10,
                sort: {
                    field: "jobState",
                    dir: "desc"
                }
            });

            // End:dataSource

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

                },
                {
                    field: "loginName",
                    title: "登录名",
                    width: 200,
                    filterable: false
                },
                {
                    field: "displayName",
                    title: "显示名",
                    width: 200,
                    filterable: false
                },
                {
                    field: "",
                    title: "在职情况",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if (item.jobState && item.jobState == "t") {
                            return "在职";
                        } else {
                            return "已撤销";
                        }
                    }
                },
                {
                    field: "remark",
                    title: "描述",
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 180,
                    template: function (item) {
                        return common.format($('#columnBtns').html(), "vm.remove('" + item.id + "')");
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
        }


    }


})();