(function () {
    'use strict';

    angular.module('app').controller('signFlowCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location,signSvc,$state) {        
        var vm = this;
        vm.title = "项目待处理";
        signSvc.flowgrid(vm);        
    }
})();
