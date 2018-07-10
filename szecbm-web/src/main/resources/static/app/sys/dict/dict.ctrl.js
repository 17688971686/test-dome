(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('dict', {
            url: "/dict",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/dict/html/list'),
            controller: function ($scope, dictSvc, bsWin) {
                $scope.csHide("bm");
                var vm = this;
                vm.dict = {};
                //deleteDict#Begin
                vm.dels = function () {
                    var nodes = vm.dictsTree.getSelectedNodes();
                    if (nodes && nodes.length > 0) {
                        vm.del(nodes[0].dictId)
                    } else {
                        bsWin.confirm("请选择数据")
                    }
                };
                vm.del = function (dictId) {
                    vm.dict.dictId = dictId;
                    bsWin.confirm("删除字典将会连下级字典一起删除，确认删除数据吗？", function () {
                        dictSvc.deleteDict(vm);
                    });
                };
                //deleteDict#End

                vm.resetDict = function () {
                    bsWin.confirm("您确定要重置数据字典吗？", function () {
                        dictSvc.resetDict(vm);
                    });
                }

                dictSvc.initDictTree(vm);
            }

        });
    });

})();