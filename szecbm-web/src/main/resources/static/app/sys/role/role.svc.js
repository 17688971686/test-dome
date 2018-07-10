(function () {
    'use strict';

    angular.module('myApp').factory("roleSvc", function ($http, bsWin, $state) {
        var role_url = util.formatUrl("sys/role");

        return {
            bsTableControl: function (vm) {
                vm.bsTableControl = {
                    options: util.getTableFilterOption({
                        url: role_url,
                        columns: [{
                            title: '行号',
                            width: 50,
                            switchable: false,
                            formatter: function (value, row, index) {
                                var state = vm.bsTableControl.state;
                                if (state.pageNumber && state.pageSize) {
                                    return index + 1 + (state.pageNumber - 1) * state.pageSize;
                                } else {
                                    return index + 1
                                }
                            }
                        }, {
                            checkbox: true
                        }, {
                            field: 'roleName',
                            title: '角色名',
                            sortable: true,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'displayName',
                            title: '角色显示名',
                            sortable: false,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'roleState',
                            title: '状态',
                            width: 100,
                            align: "center",
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: '<span class="bg-green" ng-if="row.roleState == 1">启用</span><span class="bg-red" ng-if="row.roleState != 1">禁用</span>'
                        }, {
                            title: '操作',
                            width: 350,
                            align: "center",
                            formatter: $("#columnBtns").html()
                        }]
                    })
                }
            },
            create: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.post(role_url, vm.role).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("创建成功", function () {
                            $state.go("role");
                        });
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            findRoleById: function (vm, fn) {
                $http.get(role_url + "/" + (vm.roleId || "")).success(function (data) {
                    if(!fn) {
                        vm.role = data;
                    } else {
                        fn(data);
                    }
                });
            },
            update: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(role_url, vm.role).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("更新成功");
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            deleteById: function (vm) {
                vm.isSubmit = true;
                $http['delete'](role_url, {params: {"roleId": vm.roleId || ""}}).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("删除成功");
                    //刷新表格数据
                    vm.bsTableControl.refresh();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            authorization: function (vm) {
                if (!vm.roleId) {
                    bsWin.warning("缺少参数");
                    return;
                }
                vm.isSubmit = true;
                $http.put(role_url + "/authorization?roleId=" + vm.roleId, vm.resources).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("权限更新成功");
                }, function () {
                    vm.isSubmit = false;
                });
            },
            stateEnable: function (roleId, vm) {
                //状态启用
                $http.put(role_url + "/enable?id=" + roleId).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("状态已启用");
                    //刷新表格数据
                    vm.bsTableControl.refresh();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            stateDisable: function (roleId, vm) {
                //状态停用
                $http.put(role_url + "/disable?id=" + roleId).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("状态已停用");
                    //刷新表格数据
                    vm.bsTableControl.refresh();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            findRoles: function (vm, params, fn) {
                if (angular.isFunction(params)) {
                    fn = params;
                    params = "";
                } else {
                    params = params || ""
                }
                $http.get(role_url, {
                    params: params
                }).success(function (data) {
                    if(!fn) {
                        vm.roles = data;
                    } else {
                        fn(data);
                    }
                });
            },
            findUserRoles: function (vm, userId, fn) {
                if (angular.isFunction(userId)) {
                    fn = userId;
                    userId = "";
                } else {
                    userId = userId || ""
                }
                $http.get(role_url + "/userRoles", {
                    params: {
                        userId: userId
                    }
                }).success(function (data) {
                    if(!fn) {
                        vm.roles = data;
                    } else {
                        fn(data);
                    }
                });
            }
        }
    });

})();