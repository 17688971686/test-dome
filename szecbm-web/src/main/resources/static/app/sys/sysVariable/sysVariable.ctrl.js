(function () {
    'use strict';

    angular.module('myApp').config(variableConfig);

    variableConfig.$inject = ["$stateProvider"];

    function variableConfig($stateProvider) {
        $stateProvider.state('sysVariable', {
            url: "/sysVariable",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/variable/html/list'),
            controller: variableCtrl
        });
    }

    variableCtrl.$inject = ["$scope", "sysVariableSvc", "bsWin"];

    function variableCtrl($scope, sysVariableSvc, bsWin) {
        $scope.csHide("bm");
        var vm = this;

        vm.del = function (varId) {
            bsWin.confirm("确认删除数据吗？", function () {
                vm.varId = varId;
                sysVariableSvc.deleteById(vm);
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
                ids.push(row.varId);
            });
            vm.del(ids.join(","));
        };

        // 初始化系统变量列表
        sysVariableSvc.bsTableControl(vm);
    }

})();