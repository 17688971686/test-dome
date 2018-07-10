(function () {
    'use strict';

    angular.module('myApp').config(organAddConfig);

    organAddConfig.$inject = ["$stateProvider"];

    function organAddConfig($stateProvider) {
        $stateProvider
            .state('organ.add', {
                url: "/organAdd/:id",
                controllerAs: "vm",
                templateUrl: util.formatUrl('sys/organ/html/add'),
                controller: organAddCtrl
            });
    }

    organAddCtrl.$inject = ["$scope", "organSvc", "$state"];

    function organAddCtrl($scope, organSvc, $state) {
        $scope.organId = $state.params.id;
        $scope.isSubmit = false;
        $scope.hasParent = false;

        if ($scope.organId) {
            $scope.hasParent = true;
            organSvc.findOrganById($scope, function (data) {
                $scope.parentOrgan = data;
                $scope.model = {
                    parentId: data.organId
                };
            });
        } else {
            var parentOrganTree;
            $scope.$watch("organList", function (organList) {
                parentOrganTree && parentOrganTree.destroy();

                if (!organList || organList.length == 0) {
                    $scope.hasParent = true;
                } else {
                    $scope.hasParent = false;
                    parentOrganTree = $.fn.zTree.init($("#parentOrganTree"), {
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
                        check: {
                            enable: true,
                            chkStyle: "radio"
                        },
                        callback: {
                            onCheck: function (event, treeId, treeNode) {
                                if (treeNode.checked) {
                                    $scope.$apply(function () {
                                        if(!$scope.model) $scope.model = {};
                                        $scope.model.parentId = treeNode.organId;
                                    })
                                }
                            }
                        }
                    }, organList);
                }
            })
        }

        $scope.saveOrgan = function () {
            organSvc.createOrgan($scope, function () {
                $state.go("organ", {}, {reload: true});
            });
        };

    }

})();