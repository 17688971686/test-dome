(function () {
    'use strict';

    angular.module('app').controller('annountmentListCtrl', annountmentList);

    annountmentList.$inject = ['$location', '$state', '$http', 'annountmentSvc', 'sysfileSvc'];

    function annountmentList($location, $state, $http, annountmentSvc, sysfileSvc) {
        var vm = this;
        vm.title = "通知公告列表";
        active();
        function active() {
            annountmentSvc.grid(vm);
        }

        //查看详情
        vm.detail = function(id){
            if(id){
                $state.go('annountmentDetail', {id: id});
            }
        }

        //查询
        vm.queryAnnountment = function(){
            vm.gridOptions.dataSource.read();
        }

    }
})();