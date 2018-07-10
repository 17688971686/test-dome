(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('role', {
            url: "/role",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/role/html/list'),
            controller: function ($scope, roleSvc, resourceSvc, bsWin) {
                $scope.csHide("bm");
                var vm = this;
                vm.role = {};

                vm.del = function (roleId) {
                    bsWin.confirm("确认删除数据吗？", function () {
                        vm.roleId = roleId;
                        roleSvc.deleteById(vm);
                    });
                };

                vm.dels = function () {
                    var rows = $('#editTable').bootstrapTable('getSelections');//返回的是所有选中的行对象
                    if (rows.length == 0) {
                        bsWin.warning("请选择要删除的数据");
                        return;
                    }
                    var ids = [];
                    $.each(rows, function (i, row) {
                        ids.push(row.roleId);
                    });
                    vm.del(ids.join(","));
                };

                /**
                 * 打开授权窗口
                 * @param role  角色数据
                 */
                vm.authorization = function (role) {
                    vm.roleId = role.roleId;
                    vm.displayName = role.displayName;
                    initResourceTree(role);
                    $("#roleAuthorizationWin").modal('show');
                }

                vm.toAuthorization = function () {
                    if (resourceTree) {
                        vm.resources = resourceTree.getCheckedNodes(true);
                        roleSvc.authorization(vm);
                    }
                }

                // vm.setUserData = function (row) {
                // }

                vm.start = function (roleId) {
                    roleSvc.stateEnable(roleId, vm);
                }

                vm.stop = function (roleId) {
                    roleSvc.stateDisable(roleId, vm);
                }

                var resourceTree;

                function initResourceTree(role) {
                    if (!resourceTree) {
                        resourceSvc.getResourceData($scope, function (data) {
                            data = data.value || [];

                            resourceTree = $.fn.zTree.init($("#resourceTree"), {
                                treeId: "resId",
                                check: {
                                    enable: true,
                                    chkboxType: {"Y": "p", "N": "s"}
                                },
                                data: {
                                    key: {
                                        name: "resName"
                                    },
                                    simpleData: {
                                        enable: true,
                                        idKey: "resId",
                                        pIdKey: "parentId"
                                    }
                                }
                            }, data);

                            checkNodes();
                        });
                    } else {
                        checkNodes();
                    }

                    function checkNodes() {
                        resourceTree.checkAllNodes(false);
                        vm.roleId = role.roleId;
                        roleSvc.findRoleById(vm, function (data) {
                            $.each(data.resources, function (k, v) {
                                resourceTree.checkNode(resourceTree.getNodeByParam("resId", v.resId), true, true);
                            })
                        })
                    }
                }

                // 初始化角色列表
                roleSvc.bsTableControl(vm);
            }
        });
    });

})();