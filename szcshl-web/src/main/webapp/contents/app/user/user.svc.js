(function () {
    'use strict';

    angular.module('app').factory('userSvc', user);

    user.$inject = ['$http'];

    function user($http) {
        var url_user = rootPath + "/user", url_back = '#/user', url_role = rootPath + "/role",
            url_dictgroup = rootPath + "/dict";
        var service = {
            grid: grid,
            getUserById: getUserById,
            initZtreeClient: initZtreeClient,
            createUser: createUser,
            deleteUser: deleteUser,
            updateUser: updateUser,
            getOrg: getOrg,
            getDict: getDict
        };

        return service;

        // begin#updateUser
        function updateUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                // zTree
                var nodes = getZtreeChecked();
                var nodes_role = $linq(nodes).where(function (x) {
                    return x.isParent == false;
                }).select(function (x) {
                    return {
                        id: x.id,
                        roleName: x.name
                    };
                }).toArray();
                vm.model.roles = nodes_role;

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

        // begin#deleteUser
        function deleteUser(vm, id) {
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

        // begin#createUser
        function createUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                // zTree
                var nodes = getZtreeChecked();
                var nodes_roles = $linq(nodes).where(function (x) {
                    return x.isParent == false;
                }).select(function (x) {
                    return {
                        id: x.id,
                        roleName: x.name
                    };
                }).toArray();
                vm.model.roles = nodes_roles;

                var httpOptions = {
                    method: 'post',
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

        //获取字典信息
        function getDict(vm) {
            var httpOptions = {
                method: 'get',
                url: url_dictgroup
            };

            var httpSuccess = function success(response) {
                //vm.dict ={};
                vm.sex = response.data;
                console.log(vm.sex);
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //获取部门信息
        function getOrg(vm) {

            var httpOptions = {
                method: 'get',
                url: common.format(url_user + "/getOrg")
            }
            var httpSuccess = function success(response) {
                vm.org = {};
                vm.org = response.data;

                console.log(vm.org);

            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#initZtreeClient
        function initZtreeClient(vm) {
            var httpOptions = {
                method: 'get',
                url: url_role
            }
            var httpSuccess = function success(response) {

                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        var zTreeObj;
                        var setting = {
                            check: {
                                chkboxType: {
                                    "Y": "ps",
                                    "N": "ps"
                                },
                                enable: true
                            }
                        };
                        var zNodes = $linq(response.data.value).select(
                            function (x) {
                                return {
                                    id: x.id,
                                    name: x.roleName
                                };
                            }).toArray();
                        var rootNode = {
                            id: '',
                            name: '角色集合',
                            children: zNodes
                        };
                        zTreeObj = $.fn.zTree.init($("#zTree"), setting,
                            rootNode);
                        if (vm.isUpdate) {
                            updateZtree(vm);

                        }
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

        // begin#getUserById
        function getUserById(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(url_user + "?$filter=id eq '{0}'", vm.id)
            }
            var httpSuccess = function success(response) {
                vm.model = response.data.value[0];
                if (vm.isUpdate) {
                    initZtreeClient(vm);
                }
            }

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
                        return kendo
                            .format(
                                "<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                                item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

                },
                {
                    field: "loginName",
                    title: "登录名",
                    width: 100,
                    filterable: true
                },
                {
                    field: "displayName",
                    title: "显示名",
                    width: 100,
                    filterable: true
                },
                {
                    field: "userSex",
                    title: "性别",
                    width: 80,
                    filterable: false
                },
                {
                    field: "userPhone",
                    title: "联系电话",
                    width: 80,
                    filterable: false
                },
                {
                    field: "userMPhone",
                    title: "联系手机",
                    width: 80,
                    filterable: false
                },
                {
                    field: "email",
                    title: "电子邮件",
                    width: 80,
                    filterable: false
                },
                {
                    field: "orgDto.name",
                    title: "所属部门",
                    width: 100,
                    filterable: false
                },
                {
                    field: "jobState",
                    title: "在职情况",
                    width: 80,
                    filterable: false
                },
                {
                    field: "useState",
                    title: "是否停用",
                    width: 80,
                    filterable: false
                },
                {
                    field: "pwdState",
                    title: "更改密码",
                    width: 80,
                    filterable: false
                },
                {
                    field: "userIP",
                    title: "登录IP",
                    width: 160,
                    filterable: false
                },
                {
                    field: "lastLogin",
                    title: "最后登录时间",
                    width: 160,
                    filterable: false
                },

                {
                    field: "createdDate",
                    title: "创建时间",
                    width: 160,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"

                },
                {
                    field: "remark",
                    title: "描述",
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

        // begin common fun
        function getZtreeChecked() {
            var treeObj = $.fn.zTree.getZTreeObj("zTree");
            var nodes = treeObj.getCheckedNodes(true);
            return nodes;
        }

        function updateZtree(vm) {
            var treeObj = $.fn.zTree.getZTreeObj("zTree");
            var checkedNodes = $linq(vm.model.roles).select(function (x) {
                return x.roleName;
            }).toArray();
            var allNodes = treeObj.getNodesByParam("level", 1, null);

            var nodes = $linq(allNodes).where(function (x) {
                return $linq(checkedNodes).contains(x.name);
            }).toArray();

            for (var i = 0, l = nodes.length; i < l; i++) {
                treeObj.checkNode(nodes[i], true, true);
            }
        }

        // end common fun
    }
})();