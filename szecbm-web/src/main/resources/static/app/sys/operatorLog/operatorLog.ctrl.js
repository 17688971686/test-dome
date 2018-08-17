(function () {
    'use strict';

    var app = angular.module('myApp');

    app.config(function ($stateProvider) {
        $stateProvider
            .state('operatorLog', {
                url: "/operatorLog",
                controllerAs: "vm",
                templateUrl: util.formatUrl('sys/operatorLog/html/list'),
                controller: function ($scope, operatorLogSvc, bsWin) {
                    $scope.csHide("bm");

                    var vm = this;
                    vm.model = {};
                    vm.delLog = function(days){
                        operatorLogSvc.deleteLog(vm,days);
                    }

                    // vm.del = function (id) {
                    //     vm.model.id = id;
                    //     bsWin.confirm("确认删除数据吗？", function () {
                    //         operatorLogSvc.deleteLog(vm);
                    //     })
                    // };
                    //
                    // vm.dels = function () {
                    //     var rows = $('#editTable').bootstrapTable('getSelections');//返回的是所有选中的行对象
                    //     if (rows.length == 0) {
                    //         bsWin.alert("请选择要删除的数据");
                    //         return;
                    //     }
                    //     var ids = [];
                    //     $.each(rows, function (i, row) {
                    //         ids.push(row.id)
                    //     });
                    //     vm.del(ids.join(","));
                    // };

                    // 初始化列表
                    operatorLogSvc.bsTableControl($scope);
                }

            });
    });

})();