(function () {
    'use strict';

    angular.module('myApp').config(ariableConfig);

    ariableConfig.$inject = ["$stateProvider"];

    function ariableConfig($stateProvider) {
        $stateProvider.state('sysVariableEdit', {
            url: "/sysVariableEdit/:id",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/variable/html/edit'),
            controller: variableCtrl
        });
    }

    variableCtrl.$inject = ["$state", "sysVariableSvc"];

    function variableCtrl($state, sysVariableSvc) {
        var vm = this;
        vm.varId = $state.params.id;

        if (vm.varId) {
            sysVariableSvc.findById(vm);
        }

        vm.save = function () {
            if (vm.varId) {
                sysVariableSvc.update(vm);
            } else {
                sysVariableSvc.create(vm);
            }
        }
    }

})();