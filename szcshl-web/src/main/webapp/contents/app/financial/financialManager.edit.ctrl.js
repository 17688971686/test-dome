(function () {
    'use strict';

    angular.module('app').controller('financialManagerEditCtrl', financialManager);

    financialManager.$inject = ['$location', 'financialManagerSvc', '$state'];

    function financialManager($location, financialManagerSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加财务管理';
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.isuserExist = false;
        vm.id = $state.params.id;
        vm.financial.signid = $state.params.signid;
     
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新财务管理';
        }

        vm.create = function () {
        	
            financialManagerSvc.createFinancialManager(vm);
        };
        vm.update = function () {
            financialManagerSvc.updateFinancialManager(vm);
        };
    
        activate();
        function activate() {
            if (vm.isUpdate) {
                financialManagerSvc.getFinancialManagerById(vm);
            }
        }
    }
})();
