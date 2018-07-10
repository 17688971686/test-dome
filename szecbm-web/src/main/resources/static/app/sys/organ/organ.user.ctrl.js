(function () {
    'use strict';

    angular.module('myApp').config(myConfig);

    myConfig.$inject = ["$stateProvider"]

    function myConfig($stateProvider) {
        $stateProvider.state('organ.user', {
            url: "/organUser/:id",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/organ/html/user'),
            controller: myCtrl
        });
    }

    myCtrl.$inject = ["$scope", "$state", "organSvc", "userSvc", "bsWin"];

    function myCtrl($scope, $state, organSvc, userSvc, bsWin) {
        $scope.organId = $state.params.id || '';
        $scope.isSubmit = false;
        $scope.isUpdate = false;
        $scope.openUserEditWin = function (userId) {
            userEditWin.modal("show");
            if (userId) {
                $scope.isUpdate = true;
                $scope.userId = userId;
                userSvc.findUserById($scope, function (data) {
                    $scope.model = data;
                });
            } else {
                $scope.isUpdate = false;
                $scope.model = {
                    useState: 1,
                    organ: {
                        organId: $scope.organId
                    }
                };
            }
        }

        var userEditWin = $("#organUserEditModel").on('hidden.bs.modal', function (e) {
            $scope.$apply(function () {
                $scope.model = null;
            })
        });

        $scope.saveUser = function () {
            if ($scope.isUpdate) {
                userSvc.updateUser($scope, function () {
                    userEditWin.modal("hide");
                    $("#editTable").bootstrapTable('refresh');//刷新表格数据
                });
            } else {
                userSvc.createUser($scope, function () {
                    userEditWin.modal("hide");
                    $("#editTable").bootstrapTable('refresh');//刷新表格数据
                });
            }
        }

        $scope.delUser = function (userId) {
            $scope.userId = userId;
            bsWin.confirm("确认删除数据吗？", function () {
                userSvc.deleteUser($scope);
            })
        };

        $scope.delUsers = function () {
            var rows = $('#editTable').bootstrapTable('getSelections');//返回的是所有选中的行对象
            if (rows.length == 0) {
                bsWin.alert("请选择要删除的数据");
                return;
            }
            var ids = [];
            $.each(rows, function (i, row) {
                ids.push(row.userId)
            });
            $scope.delUser(ids.join(","));
        };

        $scope.start = function (row) {
            $scope.userId = row.userId;
            userSvc.enableUser($scope);
        }

        $scope.stop = function (row) {
            $scope.userId = row.userId;
            userSvc.disableUser($scope);
        }

        organSvc.findOrganById($scope, function (data) {
            $scope.organ = data;
        });
        userSvc.bsTableControl($scope, {field: "organ.organId", operator: "eq", value: $scope.organId});
        userSvc.reloadBsTable($scope);
    }

})();