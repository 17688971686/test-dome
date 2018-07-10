(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('attachment', {
            url: "/attachment",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/attachment/html/list'),
            controller: function ($scope, attachmentSvc, bsWin) {
                $scope.csHide("bm");
                var vm = this;
                vm.attachment = {};

                vm.del = function (fileId) {
                    bsWin.confirm("确认删除数据吗？", function () {
                        attachmentSvc.deleteById(vm, fileId, function () {
                            vm.bsTableControl.refresh();
                        });
                    });
                }

                vm.dels = function () {
                    var rows = $('#editTable').bootstrapTable('getSelections');//返回的是所有选中的行对象

                    if (rows.length == 0) {
                        bsWin.warning("请选择要删除的数据");
                        return;
                    }
                    var ids = [];
                    $.each(rows, function (i, row) {
                        ids.push(row.id);
                    })
                    vm.del(ids.join(","));
                }

                attachmentSvc.bsTableControl(vm);

            }
        });
    });

})();