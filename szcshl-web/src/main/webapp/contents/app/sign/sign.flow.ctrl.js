(function () {
    'use strict';

    angular.module('app').controller('signFlowCtrl', sign);

    sign.$inject = ['$location','signFlowSvc','$state']; 

    function sign($location,signFlowSvc,$state) {        
        var vm = this;
        vm.title = "项目待处理";
        signFlowSvc.pendingSign(vm);        
    }
})();
