(function () {
    'use strict';

    angular.module('myApp').config(roleConfig);

    roleConfig.$inject = ["$stateProvider"];

    function roleConfig($stateProvider) {
        $stateProvider.state('roleEdit', {
            url: "/roleEdit/:id",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/role/html/edit'),
            controller: roleCtrl
        });
    }

    roleCtrl.$inject = ["$scope", "$state", "roleSvc"];

    function roleCtrl($scope, $state, roleSvc) {
        var vm = this;
        vm.roleId = $state.params.id;
        vm.role = {
            roleState: "1"
        };

        if (vm.roleId) {
            roleSvc.findRoleById(vm);
        }

        vm.save = function () {
            if (vm.roleId) {
                roleSvc.update(vm);
            } else {
                roleSvc.create(vm);
            }
        }
    }

})();