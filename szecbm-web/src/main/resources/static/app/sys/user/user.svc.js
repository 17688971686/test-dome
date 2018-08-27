(function () {
    'use strict';

    angular.module('myApp').factory("userSvc", userSvc);

    userSvc.$inject = ["$http", "bsWin"];

    function userSvc($http, bsWin) {
        var user_url = util.formatUrl("sys/user"), rsaKey_url = util.formatUrl("rsaKey");

        return {
            reloadBsTable: function (vm, filter) {
                var options = vm.bsTableControl.options;
                options.url = user_url;
                if (filter) {
                    options.defaultFilters = filter;
                }
            },
            /**
             * 构建用户列表配置
             * @param vm
             * @param filter
             */
            bsTableControl: function (vm, filter) {
                vm.bsTableControl = {
                    options: util.getTableFilterOption({
                        // url: user_url,
                        defaultFilters: filter,
                        defaultSort: "itemOrder asc,createdDate desc",
                        columns: [{
                            title: '行号',
                            switchable: false,
                            align: "center",
                            width: 50,
                            formatter: function (value, row, index) {
                                var state = vm.bsTableControl.state;
                                if (state.pageNumber && state.pageSize) {
                                    return index + 1 + (state.pageNumber - 1) * state.pageSize;
                                } else {
                                    return index + 1
                                }
                            }
                        }, {
                            checkbox: true,
                            align: "center"
                        }, {
                            field: 'username',
                            title: '用户名',
                            width: 150,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'displayName',
                            title: '姓名',
                            width: 150,
                            sortable: true,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'organ.organName',
                            title: '所属机构',
                            sortable: false,
                            width: 200,
                            filterControl: "input",
                            filterOperator: "like"
                        },  {
                            field: 'lastLoginDate',
                            title: '最后登录时间',
                            width: 120,
                            sortable: true,
                            filterControl: "datepicker",
                            filterOperator: "gt"
                        }, {
                            field: 'useState',
                            title: '状态',
                            width: 60,
                            align: "center",
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: '<span ng-if="row.useState == 1" class="bg-green">启用</span><span ng-if="row.useState != 1"  class="bg-red">禁用</span>'
                        },{
                            field: '',
                            title: '操作',
                            width: 200,
                            formatter: $("#userColumnBtns").html()
                        }]
                    })
                }
            },
            /**
             * 创建用户
             * @param vm
             * @param fn    操作成功的回调函数
             */
            createUser: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;

                    if (vm.model.password) {
                        $.get(rsaKey_url).success(function (data) {
                            var encrypt = new JSEncrypt();
                            encrypt.setPublicKey(data || "");
                            vm.model.password = encrypt.encrypt(vm.model.password);
                            vm.model.verifyPassword = encrypt.encrypt(vm.model.verifyPassword);
                            toCreate();
                        })
                    } else {
                        toCreate();
                    }

                }
                function toCreate() {
                    $http.post(user_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("创建成功");
                        fn && fn();
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            /**
             * 通过用户ID查找用户信息
             * @param vm
             * @param fn    操作成功的回调函数
             * @returns {boolean}
             */
            findUserById: function (vm, fn) {
                if (!vm.userId) return false;
                $http.get(user_url + "/" + vm.userId).success(function (data) {
                    data = data || {};
                    if (!fn) {
                        vm.model = data;
                    } else {
                        fn(data)
                    }
                });
            },
            /**
             * 更新用户信息
             * @param vm
             * @param fn    操作成功的回调函数
             */
            updateUser: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(user_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("更新成功");
                        fn && fn();
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            /**
             * 用户启用
             * @param vm
             * @param fn    操作成功的回调函数
             * @returns {boolean}
             */
            enableUser: function (vm, fn) {
                if (!vm.userId) return false;
                vm.isSubmit = true;
                $http.put(user_url + "/enable?id=" + vm.userId).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("启用成功");
                    $("#editTable").bootstrapTable('refresh');//刷新表格数据
                    fn && fn();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            /**
             * 用户禁用
             * @param vm
             * @param fn    操作成功的回调函数
             */
            disableUser: function (vm, fn) {
                vm.isSubmit = true;
                $http.put(user_url + "/disable?id=" + vm.userId).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("禁用成功");
                    $("#editTable").bootstrapTable('refresh');//刷新表格数据
                    fn && fn();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            /**
             * 删除用户
             * @param vm
             * @returns {boolean}
             */
            deleteUser: function (vm) {
                if (!vm.userId) return false;
                vm.isSubmit = true;
                $http['delete'](user_url + "?userId=" + vm.userId).then(function () {
                    bsWin.success("删除成功");
                    vm.bsTableControl.refresh();//刷新表格数据
                    vm.isSubmit = false;
                }, function () {
                    vm.isSubmit = false;
                });
            },
            /**
             * 重置密码
             * @param vm
             * @param userId
             * @returns {boolean}
             */
            resetPwd: function (vm, userId) {
                userId = userId || vm.userId;
                if (!userId) {
                    bsWin.warning("缺少参数");
                    return false;
                }
                bsWin.confirm("您是否要重置用户密码？", function () {
                    vm.isSubmit = true;
                    $http.put(user_url + "/resetPwd", userId).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("用户密码已重置成功");
                    }, function () {
                        vm.isSubmit = false;
                    });
                });
            },
            userResources: function (vm, params, fn) {
                $http.get(user_url + "/resources", {
                    params: params
                }).success(function (data) {
                    if (!fn) {
                        vm.menus = data;
                    } else {
                        fn(data);
                    }
                });
            },
            toSetRoles: function (vm, roleIds) {
                if (!vm.userId) {
                    bsWin.warning("缺少参数");
                    return false;
                }

                vm.isSubmit = true;
                $http.post(user_url + "/setRoles", {}, {
                    params: {
                        userId: vm.userId,
                        roleIds: roleIds
                    }
                }).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("用户角色设置成功");
                }, function () {
                    vm.isSubmit = false;
                });
            }

        };

    }

})();