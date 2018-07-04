(function () {
    'use strict';

    angular.module('app').controller('smslogCtrl', smslog);

    smslog.$inject = ['$location','smslogSvc'];

    function smslog($location, smslogSvc) {
        var vm = this;
        vm.title = '短信列表';

        activate();
        sysConfig();

        function sysConfig() {
            smslogSvc.select(vm);
        };



        function activate() {
            smslogSvc.msgGrid(vm);
        }

        vm.querySMSLog = function(){
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        /**
         * 重置
         */
        vm.formSMSReset = function(){
            var tab = $("#smsLogform").find('input,select');
            $.each(tab, function(i, obj) {
                obj.value = "";
            });
        }
    }
})();
