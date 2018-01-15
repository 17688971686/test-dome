(function () {
    'use strict';

    angular.module('app').controller('logCtrl', log);

    log.$inject = ['$location','logSvc']; 

    function log($location, logSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '日志列表';

        activate();
        function activate() {
            logSvc.grid(vm);
        }

        vm.queryLog = function(){
            vm.gridOptions.dataSource._skip="";
            vm.gridOptions.dataSource.read();
        }

        /**
         * 重置
         */
        vm.formReset = function(){
            var tab = $("#logform").find('input,select');
            $.each(tab, function(i, obj) {
                obj.value = "";
            });
        }
    }
})();
