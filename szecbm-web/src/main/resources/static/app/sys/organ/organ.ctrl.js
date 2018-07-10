(function () {
    'use strict';

    angular.module('myApp').config(organConfig);

    organConfig.$inject = ["$stateProvider"];

    function organConfig($stateProvider) {
        $stateProvider.state('organ', {
            url: "/organ",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/organ/html/list'),
            controller: organCtrl
        });
    }

    organCtrl.$inject = ["$scope", "$state", "organSvc", "bsWin"];

    function organCtrl($scope, $state, organSvc, bsWin) {
        $scope.csHide("bm");

        $scope.deleteOrgan = function (organId) {
            $scope.organId = organId;
            bsWin.confirm("确认删除数据吗？", function () {
                organSvc.deleteOrgan(vm);
            })
        };

        var treeAddBtn = $("#treeAddBtn"),
            organTreeDom = $("#organTree"),
            organTreeDelete = organTreeDom.attr("delete");

        // 初始化机构树
        var organTree;
        initOrganTree();
        function initOrganTree() {
            organTree && organTree.destroy();

            organSvc.getOrganList($scope, function (data) {
                organTree = $.fn.zTree.init(organTreeDom, {
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
                    edit: {
                        enable: true,
                        removeTitle: "删除机构",
                        showRemoveBtn: function (treeId, treeNode) {
                            if (!organTreeDelete) {
                                return false;
                            }
                            return treeNode.organDataType > 0 ? !treeNode.isParent : false;
                        },
                        showRenameBtn: false
                    },
                    view: {
                        addHoverDom: function (treeId, treeNode) {
                            if (treeNode.organType < 2) {
                                var sObj = $("#" + treeNode.tId + "_span");
                                if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
                                if (treeAddBtn.length > 0) {
                                    var addStr = util.format(treeAddBtn.html(), treeNode.tId);
                                    sObj.after(addStr);
                                    var btn = $("#addBtn_" + treeNode.tId);
                                    if (btn) btn.bind("click", function () {
                                        $state.go("organ.add", {id: treeNode.organId});
                                        return false;
                                    });
                                }
                            }
                        },
                        removeHoverDom: function (treeId, treeNode) {
                            $("#addBtn_" + treeNode.tId).unbind().remove();
                        },
                        selectedMulti: false
                    },
                    callback: {
                        onClick: function (event, treeId, treeNode) {
                            // if (treeNode.organDataType == 0 && !treeNode.isParent) {
                            //     $state.go('organ.user', {id: treeNode.organId});
                            // } else {
                            // if (treeNode.organType == 0) {
                            $state.go('organ.edit', {id: treeNode.organId});
                            // } else {
                            //     $state.go('organ.user', {id: treeNode.organId});
                            // }
                            // }
                        },
                        beforeRemove: function (treeId, treeNode) {
                            $scope.$apply(function () {
                                $scope.organId = treeNode.organId;
                                organSvc.deleteOrgan($scope, function () {
                                    $scope.organId = null;
                                    $state.go('organ', {}, {reload: true});
                                });
                            });
                            return false;
                        },
                        beforeDrag: function () {
                            return false;
                        }
                    }
                }, data);

                var rootNode = organTree.getNodeByParam("parentId", null);
                organTree.expandNode(rootNode, true);

                // 初始化模糊搜索方法
                window.fuzzySearch("organTree", '#organKey', null, true);
            })
        }

        $scope.initOrganTree = initOrganTree;
    }

})();