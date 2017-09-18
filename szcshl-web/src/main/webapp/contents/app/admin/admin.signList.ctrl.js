(function () {
    'use strict';

    angular.module('app').controller('adminSignListCtrl', admin);

    admin.$inject = ['signSvc', 'adminSvc','bsWin','$state' , 'headerSvc'];

    function admin(signSvc, adminSvc,bsWin , $state , headerSvc) {
        var vm = this;
        vm.title = '项目查询统计';
        vm.currentAssociateSign = {};
        vm.signList={};
        vm.headerList={};
        vm.header = "";
        vm.headerType = "项目类型";
        activate();
        function activate() {
            //初始化查询参数
            adminSvc.initSignList(function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.orgsList = data.reObj;
                }
            });
            adminSvc.getSignList(vm);
        }

        //重置
        vm.formReset = function () {
            var tab = $("#searchform").find('input,select');
            $.each(tab, function (i, obj) {
                $('input:checkbox').attr('checked', false);
                obj.value = "";
            });
        }

        //項目查詢統計
        vm.searchSignList = function () {

            vm.signListOptions.dataSource.read();

        }
    }
})();
