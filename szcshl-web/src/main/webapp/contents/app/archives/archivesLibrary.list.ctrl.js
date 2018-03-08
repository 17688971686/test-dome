(function () {
    'use strict';

    angular.module('app').controller('archivesLibraryListCtrl', archivesLibrary);

    archivesLibrary.$inject = ['$location', 'archivesLibrarySvc', '$state','bsWin'];

    function archivesLibrary($location, archivesLibrarySvc, $state,bsWin) {
        var vm = this;
        vm.title = '档案借阅管理';

        activate();
        function activate() {
            archivesLibrarySvc.grid(vm);
        }

        //查询
        vm.search = function(){
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        //重置
        vm.formReset = function(){
            $("#searchform")[0].reset();
        }

    }
})();
