(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiyViewCtrl', monthlyMultiyView);

    monthlyMultiyView.$inject = ['monthlyMultiyearSvc', 'sysfileSvc', '$state', 'bsWin', '$scope'];

    function monthlyMultiyView(monthlyMultiyearSvc, sysfileSvc, $state, bsWin, $scope) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '月报详情页面';
        vm.suppletter = {};//文件稿纸对象
        vm.id = $state.params.id;

        activate();
        function activate() {
            monthlyMultiyearSvc.getmonthlyMultiyearById(vm.id, function (data) {
                vm.suppletter = data;
                //初始化附件
                sysfileSvc.findByBusinessId(vm.id , function(data){
                    vm.sysFilelists = data;
                    vm.initFileUpload();
                });

            });
        }

        //初始化附件上传控件
        vm.initFileUpload = function () {
            if (!vm.suppletter.id) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.suppletter.id", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        console.log(newValue);
                        console.log(oldValue);
                        vm.initFileUpload();
                    }
                });
            }
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
