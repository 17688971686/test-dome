(function () {
    'use strict';

    angular.module('myApp').config(userConfig);

    userConfig.$inject = ["$stateProvider"];

    function userConfig($stateProvider) {
        $stateProvider.state('user', {
            url: "/user",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/user/html/list'),
            controller: userCtrl
        });
    }

    userCtrl.$inject = ["$scope", "userSvc", "organSvc", "roleSvc", "bsWin"];

    function userCtrl($scope, userSvc, organSvc, roleSvc, bsWin) {
        $scope.csHide("bm");

        var vm = this;

        // 初始化列表
        var userFilter = {
            field: "organ.organRel",
            operator: "like",
            value: "|"
        };

        userSvc.bsTableControl(vm, userFilter);

        function reloadBsTable(treeNode) {
            vm.organName = treeNode.oldname || treeNode.organName || "-";
            vm.organId = treeNode.organId;
            userFilter.value = "|" + treeNode.organId + "|";
            userSvc.reloadBsTable(vm, userFilter);
        }

        vm.resetPwd = function (userId) {
            vm.userId = userId;
            userSvc.resetPwd(vm);
        }
        
        // 删除操作
        vm.del = function (userId) {
            vm.userId = userId;
            bsWin.confirm("确认删除数据吗？", function () {
                userSvc.deleteUser(vm);
            })
        };

        // 批量删除操作
        vm.dels = function () {
            // 返回的是所有选中的行对象
            var rows = vm.bsTableControl.getSelections();
            if (rows.length == 0) {
                bsWin.warning("请选择要删除的数据");
                return;
            }
            var ids = [];
            $.each(rows, function (i, row) {
                ids.push(row.userId)
            });
            vm.del(ids.join(","));
        };

        /**
         * 启用操作
         * @param row
         */
        vm.start = function (row) {
            vm.userId = row.userId;
            userSvc.enableUser(vm);
        }

        /**
         * 禁用操作
         * @param row
         */
        vm.stop = function (row) {
            vm.userId = row.userId;
            userSvc.disableUser(vm);
        }

        // 初始化机构树
        var organTree;
        initOrganTree();

        function initOrganTree() {
            organTree && organTree.destroy();

            organSvc.getOrganList(vm, function (data) {
                organTree = $.fn.zTree.init($("#organTree"), {
                    treeId: "organId",
                    data: {
                        key: {
                            name: "organName"
                        },
                        simpleData: {
                            enable: true,
                            idKey: "organId",
                            pIdKey: "parentId"
                        }
                    },
                    callback: {
                        onClick: function (event, treeId, treeNode) {
                            $scope.$apply(function () {
                                reloadBsTable(treeNode);
                            })
                        }
                    }
                }, data);

                var rootNode = organTree.getNodeByParam("parentId", null);
                organTree.expandNode(rootNode, true);
                reloadBsTable(rootNode);

                // 初始化模糊搜索方法
                window.fuzzySearch("organTree", '#organKey', null, true);
            })
        }

        vm.initOrganTree = initOrganTree;

        //===============用户编辑 begin===============
        vm.openUserEditWin = function (userId) {
            userEditWin.modal("show");
            if (userId) {
                vm.isUpdate = true;
                vm.userId = userId;
                userSvc.findUserById(vm, function (data) {
                    vm.model = data;
                });
            } else {
                vm.isUpdate = false;
                vm.model = {
                    useState: 1,
                    organ: {
                        organId: vm.organId
                    }
                };
            }
        }

        var userEditWin = $("#organUserEditModel").on('hidden.bs.modal', function (e) {
            $scope.$apply(function () {
                vm.model = null;
            })
        });

        vm.saveUser = function () {
            if (vm.isUpdate) {
                userSvc.updateUser(vm, function () {
                    userEditWin.modal("hide");
                    $("#editTable").bootstrapTable('refresh'); //刷新表格数据
                });
            } else {
                userSvc.createUser(vm, function () {
                    userEditWin.modal("hide");
                    $("#editTable").bootstrapTable('refresh'); //刷新表格数据
                });
            }
        }
        //===============用户编辑 end===============

        var userRolesModel = $("#userRolesModel");
        vm.setRoles = function (user) {
            vm.userId = user.userId;
            userRolesModel.modal("show");

            roleSvc.findUserRoles(vm, vm.userId, function (data) {
                vm.roles = data || [];
            });
        }

        vm.toSetRoles = function() {
            var roleIds = [];
            userRolesModel.find("input[name='roleId']:checked").each(function (i, o) {
                if(!this.disabled) {
                    roleIds.push(this.value);
                }
            });
            // console.log(roleIds);
            // return false;
            userSvc.toSetRoles(vm, roleIds.join(","))
        }

    }

})();