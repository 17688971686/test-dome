(function () {
    'use strict';

    angular.module('app').controller('adminPersonDoingCtrl', adminPersonDoing);

    adminPersonDoing.$inject = ['$location', 'adminSvc', 'flowSvc','pauseProjectSvc'];

    function adminPersonDoing($location, adminSvc, flowSvc,pauseProjectSvc) {
        var vm = this;
        vm.title = '个人主办项目';

        activate();
        function activate() {
            adminSvc.personMainTasksGrid(vm);
        }

        /**
         * 查询
         */
        vm.querySign = function(){
            vm.gridOptions.dataSource.read();
        }
        /**
         * 项目暂停
         * @param signid
         */
        vm.pauseProject = function (signid) {
            pauseProjectSvc.findPausingProject(vm,signid);
        }

        /**
         * 流程激活
         * @param signid
         */
        vm.startProject = function (signid) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认激活吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    flowSvc.activeFlow(vm, signid);
                }
            })
        }
    }
})();
