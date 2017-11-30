(function () {
    'use strict';

    angular.module('app').controller('archivesLibraryViewCtrl', archivesLibraryView);

    archivesLibraryView.$inject = ['archivesLibrarySvc', 'bsWin', '$state'];

    function archivesLibraryView(archivesLibrarySvc, bsWin, $state) {

        var vm = this;
        vm.model = {};
        vm.title = '项目档案借阅查看详情';
        vm.id = $state.params.id;

        activate();
        function activate() {
            if (vm.id) {
                archivesLibrarySvc.initArchivesLibrary(vm.id, function (data) {
                    vm.model = data;
                });
            }
        }
    }
})();
