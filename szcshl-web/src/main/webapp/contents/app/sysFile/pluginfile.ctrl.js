(function () {
    'use strict';

    angular.module('app').controller('pluginfileCtrl', pluginfile);

    pluginfile.$inject = ['$location', 'sysfileSvc'];

    function pluginfile($location, sysfileSvc) {
        var vm = this;
        vm.title = '系统安装包列表';

        activate();
        function activate() {
            sysfileSvc.queryPluginfile(vm);
        }

        vm.downLoadPlugin = function(fileName){
            window.open(rootPath + "/file/pluginDownload?fileName=" + fileName);
        }

    }//E_sysConfig
})();
