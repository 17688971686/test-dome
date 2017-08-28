(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterEditCtrl', addSuppLetter);

    addSuppLetter.$inject = ['$location', 'addSuppLetterSvc', '$state'];

    function addSuppLetter($location, addSuppLetterSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加登记补充资料';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新登记补充资料';
        }

      //根据ID查看拟补充资料函
        vm.findByIdAddSuppLetter = function(id){
        	$state.go('getAddSuppLetterById', {id: id});
        }
        vm.create = function () {
            addSuppLetterSvc.createAddSuppLetter(vm);
        };
        vm.update = function () {
            addSuppLetterSvc.updateAddSuppLetter(vm);
        };

        activate();
        function activate() {
        	//查看补充资料详细信息
            addSuppLetterSvc.getAddSuppLetterById(vm);
        }
    }
})();
