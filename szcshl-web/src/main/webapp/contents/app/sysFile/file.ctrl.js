(function () {
    'use strict';

    angular.module('app').controller('fileListCtrl', fileList);

    fileList.$inject = ['$location', 'sysfileSvc','$state'];

    function fileList($location, sysfileSvc,$state) {
        var vm = this;
        vm.model={};
        vm.model.signid=$state.params.id;
        vm.model.type=$state.params.type;
        //附件下载
        vm.commonDownloadSysFile = function(sysFileId){
            sysfileSvc.downloadFile(sysFileId);
        }
        activate();
        function activate() {
            // 初始化上传附件
            sysfileSvc.queryFile(vm.model.signid,vm.model.type,function(data){
                if(data && data.length > 0){
                    //vm.showFlag.tabSysFile = true;
                    vm.sysFileList = data;
                }
            });
        }


    }//E_sysConfig
})();
