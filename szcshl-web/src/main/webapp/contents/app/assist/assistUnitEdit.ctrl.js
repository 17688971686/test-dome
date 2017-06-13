(function () {
    'use strict';

    angular.module('app').controller('assistUnitEditCtrl', assistUnitEdit);

    assistUnitEdit.$inject = ['$location', 'assistUnitSvc','$state'];

    function assistUnitEdit($location, assistUnitSvc,$state) {
        var vm = this;
        vm.title = '新增协审单位';
        vm.id=$state.params.id;
        vm.isUnitExist=false;
        if(vm.id){
        	vm.isUpdate=true;
        	vm.title='更新协审单位';
        }
        
        vm.create=function(){
          assistUnitSvc.createAssistUnit(vm);
        }
        
        vm.update=function(){
        	assistUnitSvc.updateAssistUnit(vm);
        }
        

        activate();
        function activate() {
        	if(vm.isUpdate){
        		assistUnitSvc.getAssistUnitById(vm);
        	}
        	
        }
    }
})();
