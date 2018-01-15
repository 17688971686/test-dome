(function () {
    'use strict';

    angular.module('app')
    .filter('date', function(){
        return function(val){
            if(val){
                return (new Date(val.CompatibleDate())).Format("yyyy-MM-dd");
            }else{
                return "";
            }
        }
    }).controller('adminCtrl', admin);

    admin.$inject = ['$location','adminSvc'];

    function admin($location, adminSvc) {
        var vm = this;
        vm.title = '待办项目';

        activate();
        function activate() {
        	adminSvc.gtasksGrid(vm);
        }

        /**
         * 查询
         */
        vm.querySign = function(){
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        /**
         * 重置
         */
        vm.resetForm = function(){
            var tab = $("#searchform").find('input,select');
            $.each(tab, function(i, obj) {
                obj.value = "";
            });
        }

        vm.countWorkday=function(){
        	adminSvc.countWorakday(vm);
        }

    }
})();
