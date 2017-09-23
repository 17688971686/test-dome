(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterQueryEditCtrl', addSuppLetterQuery);

    addSuppLetterQuery.$inject = ['$location', 'addSuppLetterQuerySvc', '$state'];

    function addSuppLetterQuery($location, addSuppLetterQuerySvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加登记补充资料';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新登记补充资料';
        }
       
        vm.create = function () {
            addSuppLetterQuerySvc.createaddSuppLetterQuery(vm);
        };
        vm.update = function () {
            addSuppLetterQuerySvc.updateaddSuppLetterQuery(vm);
        };
        activate();
        function activate() {
        	//查看补充资料详细信息
            addSuppLetterQuerySvc.getaddSuppLetterQueryById(vm);
        }
    }
})();
