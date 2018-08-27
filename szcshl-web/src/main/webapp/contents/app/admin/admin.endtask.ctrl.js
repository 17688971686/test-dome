(function () {
    'use strict';

    angular.module('app').controller('adminEndTaskCtrl', endTask);

    endTask.$inject = ['adminSvc', '$state', '$rootScope'];

    function endTask(adminSvc, $state, $rootScope) {
        var vm = this;
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function () {
            $rootScope.storeView(vm.stateName, {
                gridParams: vm.gridOptions.dataSource.transport.options.read.data(),
                queryParams: vm.queryParams,
                data: vm
            });
        }
        //查询
        vm.query = function () {
            vm.gridOptions.dataSource._skip = "";
            vm.gridOptions.dataSource.read();
        }

        activate();
        function activate() {
            adminSvc.workName(vm);

            if ($rootScope.view[vm.stateName]) {
                var preView = $rootScope.view[vm.stateName];
                //恢复grid
                if (preView.gridParams) {
                    vm.gridParams = preView.gridParams;
                }
                //恢复表单参数
                if (preView.data) {
                    vm.model = preView.data.model;
                }
                //恢复数据
                vm.project = preView.data.project;
                //恢复页数页码
                if (preView.queryParams) {
                    vm.queryParams = preView.queryParams;
                }
                adminSvc.endTaskGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            } else {
                adminSvc.endTaskGrid(vm);
            }

        }
    }
})();
