(function () {
    'use strict';

    angular.module('app').controller('dispatchEditCtrl', dispatch);

    dispatch.$inject = ['$location','dispatchSvc','$state']; 

    function dispatch($location, dispatchSvc, $state) {     
        var vm = this;
        vm.title = '项目发文编辑';

        vm.dispatchDoc = {};
        vm.dispatchDoc.signId = $state.params.signid;
        
        dispatchSvc.initDispatchData();
        
        vm.create = function(){
        	dispatchSvc.saveDispatch(vm);
        }
    }
})();
