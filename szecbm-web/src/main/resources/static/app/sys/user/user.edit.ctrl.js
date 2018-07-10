(function () {
    'use strict';

    var app = angular.module('myApp');

    app.config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('userEdit', {
                url: "/userEdit/:id",
                controllerAs: "vm",
                templateUrl: util.formatUrl('sys/user/html/edit'),
                controller: function ($scope, userSvc, $state) {
                    $scope.csHide("基础管理");
                    var vm = this;
                    vm.userId = $state.params.id;

                    vm.isUpdate = false;
                    if (vm.userId) {
                        vm.isUpdate = true;
                        userSvc.findUserById(vm);
                    } else {
                        vm.model = {
                            useState: 1
                        };
                    }

                    vm.save = function () {
                        if (vm.isUpdate) {
                            userSvc.updateUser(vm);
                        } else {
                            userSvc.createUser(vm, function () {
                                $state.go("user");
                            });
                        }
                    };
                }
            });
    });

})();