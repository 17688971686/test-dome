(function () {
    'use strict';

    angular.module('app').controller('adminSignListCtrl', admin);

    admin.$inject = ['signSvc', 'adminSvc'];

    function admin(signSvc, adminSvc) {
        var vm = this;
        vm.title = '项目查询统计';
        activate();
        function activate() {
            //初始化查询参数
            adminSvc.initSignList(function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.orgsList = data.reObj;
                }
            });
            adminSvc.getSignList(vm);
            //项目关联初始化
            //signSvc.associateGrid(vm);
        }

        //重置
        vm.formReset = function () {
            var tab = $("#searchform").find('input,select');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        //項目查詢統計
        vm.searchSignList = function () {
            vm.signListOptions.dataSource.read();
        }
    }
})();
