(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiyViewCtrl', monthlyMultiyView);

    monthlyMultiyView.$inject = ['monthlyMultiyearSvc', 'sysfileSvc', '$state', 'bsWin', '$scope'];

    function monthlyMultiyView(monthlyMultiyearSvc, sysfileSvc, $state, bsWin, $scope) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '月报简报管理';
        vm.suppletter = {};//文件稿纸对象
        vm.id = $state.params.id;


        vm.businessFlag = {
            isInitFileOption: false,   //是否已经初始化附件上传控件
        }

        activate();
        function activate() {
                monthlyMultiyearSvc.getmonthlyMultiyearById(vm.id, function (data) {
                    vm.suppletter = data;
                    vm.initFileUpload();
                });

        }

        //初始化附件上传控件
        vm.initFileUpload = function () {
            vm.sysFile = {
                businessId: vm.suppletter.id,
                mainId: vm.suppletter.id,
                mainType: sysfileSvc.mainTypeValue().SIGN,
                sysfileType: sysfileSvc.mainTypeValue().MONTH_FILE,
                sysBusiType: sysfileSvc.mainTypeValue().MONTH_FILE,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm
            });
        }

    }
})();
