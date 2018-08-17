(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('resource', {
            url: "/resource",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/resource/html/list'),
            controller: function ($scope, resourceSvc, bsWin) {
                $scope.csHide("bm");

                $scope.editTitle = "新增系统资源";
                $scope.model = {status: 1};
                $scope.isUpdate = false;
                $scope.isSubmit = false;
                $scope.createResource = createResource;

                function createResource(resId) {
                    if (!resId) {
                        $scope.model = {status: 1};
                        $scope.isUpdate = false;
                        $scope.editTitle = "新增系统资源";
                        var nodes = parentResourceTree.getCheckedNodes(true);
                        for (var i = 0, l = nodes.length; i < l; i++) {
                            parentResourceTree.checkNode(nodes[i], false, true);
                        }
                    } else {
                        $scope.isUpdate = true;
                        $scope.editTitle = "编辑系统资源";
                        if (!$scope.model) $scope.model = {};
                        $scope.resId = resId;
                        resourceSvc.findResourceById($scope, function (data) {
                            $scope.parentId = data.parentId;
                            var node = parentResourceTree.getNodeByParam("resId", data.parentId);
                            node && parentResourceTree.checkNode(node, true, true);
                        });
                    }
                }

                $scope.createSave = function () {
                    var nodes = parentResourceTree.getCheckedNodes(true);
                    if (nodes.length > 0) {
                        $scope.model.parentId = nodes[0].resId;
                    }

                    resourceSvc.createResource($scope, function () {
                        initResourceTree()
                    });
                }

                $scope.updateSave = function () {
                    util.initJqValidation();
                    var isValid = $('form').valid();
                    if (isValid) {
                        var nodes = parentResourceTree.getCheckedNodes(true);
                        if (nodes.length > 0) {
                            $scope.model.parentId = nodes[0].resId;
                        }

                        resourceSvc.updateResource($scope, function () {
                            initResourceTree()
                        });
                    }
                }

                // 重置系统资源
                $scope.resetResource = function () {
                    resourceSvc.resetResource($scope, function () {
                        initResourceTree();
                    });
                }

                // 初始化资源树
                var resourceTree, parentResourceTree;
                initResourceTree();
                function initResourceTree() {
                    resourceTree && resourceTree.destroy();
                    parentResourceTree && parentResourceTree.destroy();

                    resourceSvc.getResourceData($scope, function (data) {
                        data = data.value || [];
                        var setting = {
                            treeId: "resId",
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
                        };

                        resourceTree = $.fn.zTree.init($("#resourceTree"), $.extend({
                            edit: {
                                enable: true,
                                removeTitle: "删除资源",
                                showRemoveBtn: function (treeId, treeNode) {
                                    return !treeNode.isParent;
                                },
                                showRenameBtn: false
                            },
                            callback: {
                                onClick: function (event, treeId, treeNode) {
                                    // console.log(treeId, treeNode);
                                    $scope.$apply(function () {
                                        createResource(treeNode.resId);
                                    })
                                },
                                beforeRemove: function (treeId, treeNode) {
                                    $scope.$apply(function () {
                                        $scope.resId = treeNode.resId;
                                        resourceSvc.deleteResource($scope, function () {
                                            $scope.resId = null;
                                            $scope.parentId = null;
                                            createResource();
                                            initResourceTree();
                                        });
                                    });
                                    return false;
                                },
                                beforeDrag: function () {
                                    return false;
                                }
                            }
                        }, setting), data);

                        if ($scope.parentId) {
                            var node = resourceTree.getNodeByParam("resId", $scope.parentId);
                            resourceTree.expandNode(node, true, false, true);
                        }

                        parentResourceTree = $.fn.zTree.init($("#parentResourceTree"), $.extend({
                            check: {
                                enable: true,
                                chkStyle: "radio"
                            }
                        }, setting), data);

                        // 初始化模糊搜索方法
                        window.fuzzySearch("resourceTree", '#resourceKey', null, true);
                    });
                }

            }

        });
    });

})();