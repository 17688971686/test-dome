(function () {
    'dispatch strict';

    angular.module('app').controller('dispatchEditCtrl', dispatch);

    dispatch.$inject = ['$location','dispatchSvc','$state']; 

    function dispatch($location, dispatchSvc, $state) {     
        var vm = this;
        vm.title = '项目发文编辑';

        vm.dispatchDoc = {};
        vm.dispatchDoc.signId = $state.params.signid;
        //alert(vm.dispatchDoc.dispatchWay);
        vm.create = function(){
        	dispatchSvc.saveDispatch(vm);
        }
        
        activate();
        function activate() {
        	 kendo.culture("zh-CN");
             $("#draftDate").kendoDatePicker({
             	 format: "yyyy-MM-dd",
             	 weekNumber: true
             });
             $("#dispatchDate").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
             dispatchSvc.initDispatchData(vm);
        }
    }
})();
