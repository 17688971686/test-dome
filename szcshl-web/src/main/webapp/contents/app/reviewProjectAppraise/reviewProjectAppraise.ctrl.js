
(function () {
    'use strict';

    angular.module('app').controller('reviewProjectAppraiseCtrl', reviewProjectAppraise);

    reviewProjectAppraise.$inject = ['$location', 'reviewProjectAppraiseSvc'];

    function reviewProjectAppraise($location, reviewProjectAppraiseSvc) {
        var vm = this;
        vm.title = '优秀评审报告列表';


        activate();
        function activate() {
            reviewProjectAppraiseSvc.appraisingProjectGrid(vm);
        }
    }
})();

