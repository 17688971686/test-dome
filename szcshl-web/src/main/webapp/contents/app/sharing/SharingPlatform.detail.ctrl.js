(function () {
    'use strict';

    angular.module('app').controller('sharingDetailCtrl', sharingPlatlform);

    sharingPlatlform.$inject = ['$location', '$state', '$http', 'sharingPlatlformSvc'];

    function sharingPlatlform($location, $state, $http, sharingPlatlformSvc) {
        var vm = this;
        vm.title = '资料共享详情页';
        vm.model = {}; //创建资料共享对象
        vm.model.sharId = $state.params.sharId;

        //下载
        vm.downloadSysFile = function (id) {
            sharingPlatlformSvc.downloadSysfile(id);
        }

        activate();
        function activate() {
            sharingPlatlformSvc.getSharingDetailById(vm, vm.model.sharId);
            sharingPlatlformSvc.findFileList(vm);  //附件查询
        }
    }
})();