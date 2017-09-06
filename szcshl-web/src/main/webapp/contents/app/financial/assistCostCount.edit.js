(function () {
    'use strict';

    angular.module('app').controller('assistCostCountSvcEditCtrl', assistCostCount);

    assistCostCount.$inject = ['$location', 'assistCostCountSvc', '$state'];

    function assistCostCount($location, assistCostCountSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '财务管理';
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
        	
            assistCostCountSvc.createassistCostCount(vm);
        };
        vm.update = function () {
            assistCostCountSvc.updateassistCostCount(vm);
        };
    
        activate();
        function activate() {
        	  assistCostCountSvc.grid(vm);
            if (vm.isUpdate) {
                assistCostCountSvc.getassistCostCountById(vm);
            }
        }
    }
})();
