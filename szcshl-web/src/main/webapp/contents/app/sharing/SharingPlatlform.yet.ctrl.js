(function () {
    'use strict';

    angular.module('app').controller('sharingPlatlformYetCtrl', sharingPlatlformYet);

    sharingPlatlformYet.$inject = ['$location', '$state','$http','sharingPlatlformYetSvc'];

    function sharingPlatlformYet($location,$state, $http,sharingPlatlformYetSvc) {
        var vm = this;
        vm.title = '共享管理';
        vm.model = {};

        activate();
        function activate() {
            sharingPlatlformYetSvc.grid(vm);
        }
        
        //查询
        vm.findSharing = function(){
        	vm.gridOptions.dataSource.read();
        }
        //重置
        vm.resetShared = function(){
        	var tab = $("#formSharingPub").find('select');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
        }
    }
})();
