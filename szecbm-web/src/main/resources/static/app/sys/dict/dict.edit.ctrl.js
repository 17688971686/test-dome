(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('dict.edit', {
            url: "/dictEdit/:dictId",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/dict/html/edit'),
            controller: function ($scope, dictSvc, $state) {
                $scope.csHide("bm");
                var vm = this;
                vm.dict = {};
                vm.dictId = $state.params.dictId;
                if (vm.dictId) {
                    vm.isUpdate = true;
                }
                if (vm.isUpdate) {
                    dictSvc.findDictById(vm);
                } else {
                    dictSvc.initpZtreeClient(vm);
                }
                vm.create = function () {
                    if (vm.dictsTree) {
                        var pNode = vm.dictsTree.getCheckedNodes(true);
                        if (pNode && pNode.length != 0) {
                            vm.dict.parentId = pNode[0].dictId;
                        }
                    }
                    dictSvc.createDict(vm);
                };

                vm.update = function () {
                    dictSvc.updateDict(vm);
                };

                vm.dictTypeChange = function () {
                    if (vm.dict.dictType) {
                        vm.dict.dictKey = '';
                    }
                };

            }
        });
    });

})();