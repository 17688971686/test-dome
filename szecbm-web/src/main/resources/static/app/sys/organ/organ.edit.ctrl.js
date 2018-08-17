(function () {
    'use strict';

    angular.module('myApp').config(myConfig);

    myConfig.$inject = ["$stateProvider"];

    function myConfig($stateProvider) {
        $stateProvider.state('organ.edit', {
            url: "/organEdit/:id",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/organ/html/edit'),
            controller: myCtrl
        });
    }

    myCtrl.$inject = ["$scope", "$state", "organSvc", "resourceSvc"];

    function myCtrl($scope, $state, organSvc, resourceSvc) {
        $scope.organId = $state.params.id;
        $scope.isSubmit = false;

        if ($scope.organId) {
            organSvc.findOrganById($scope, function (data) {
                $scope.model = data;
                if (data.parentId) {
                    organSvc.findOrganById({organId: data.parentId}, function (parentOrgan) {
                        $scope.parentOrgan = parentOrgan;
                    });
                }
            });
        }

        $scope.saveOrgan = function () {
            organSvc.updateOrgan($scope, function () {
                $scope.initOrganTree && $scope.initOrganTree();
            })
        };

        $scope.authorization = function () {
            initResourceTree();
            $("#organAuthorizationWin").modal('show');
        }

        $scope.toAuthorization = function () {
            if (resourceTree) {
                $scope.model.resources = resourceTree.getCheckedNodes(true);
                organSvc.authorization($scope);
            }
        }

        var resourceTree;

        function initResourceTree() {
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
                $.each($scope.model.resources, function (k, v) {
                    resourceTree.checkNode(resourceTree.getNodeByParam("resId", v.resId), true, true);
                })
            }
        }
    }

})();
