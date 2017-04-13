(function () {
    'use strict';

    angular
        .module('app')
        .controller('roleEditCtrl', role);

    role.$inject = ['$location','roleSvc','$state']; 

    function role($location, roleSvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加角色';
        vm.isRoleExist=false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新角色';
        }
        
        vm.create = function () {
        	roleSvc.createRole(vm);
        };
        vm.update = function () {
        	roleSvc.updateRole(vm);
        };
        vm.checkRole = function () {
        	roleSvc.checkRole(vm);
        };

        activate();
        function activate() {
        	if (vm.isUpdate) {
        		roleSvc.getRoleById(vm);
            } else {
            	roleSvc.initZtreeClient(vm);
            }
        }
    }
})();
