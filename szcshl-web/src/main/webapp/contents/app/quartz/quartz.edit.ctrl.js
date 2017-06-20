(function () {
    'use strict';

    angular.module('app').controller('quartzEditCtrl', quartz);

    quartz.$inject = ['$location', 'quartzSvc', '$state'];

    function quartz($location, quartzSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加定时器配置';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新定时器配置';
        }

        activate();
        function activate() {
            if (vm.isUpdate) {
                quartzSvc.getQuartzById(vm);
            }
        }

        vm.create = function () {
            quartzSvc.createQuartz(vm);
        };
        vm.update = function () {
            quartzSvc.updateQuartz(vm);
        };

    }
})();
