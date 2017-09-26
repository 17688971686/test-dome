
(function () {
    'use strict';

    angular.module('app').controller('reviewProjectAppraisingCtrl', reviewProjectAppraising);

    reviewProjectAppraising.$inject = ['$location', 'reviewProjectAppraisingSvc'];

    function reviewProjectAppraising($location, reviewProjectAppraisingSvc) {
        var vm = this;
        vm.title = '优秀评审报告列表';


        activate();
        function activate() {
            reviewProjectAppraisingSvc.appraisingProjectGrid(vm);
        }
    }
})();

