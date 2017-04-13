(function () {
    'use strict';

    angular
        .module('app')
        .controller('orgEditCtrl', org);

    org.$inject = ['$location','orgSvc','$state']; 

    function org($location, orgSvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '新增部门';
        vm.isorgExist=false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新部门';
        }
        
        vm.create = function () {
        	orgSvc.createOrg(vm);
        };
        vm.update = function () {
        	orgSvc.updateOrg(vm);
        };
        

        activate();
        function activate() {
        	if (vm.isUpdate) {
        		orgSvc.getOrgById(vm);
            } 
        }
    }
})();
