(function () {
    'use strict';

    angular.module('app').controller('yearMonthlyNewsletterCtrl', yearMonthlyNewsletter);

    yearMonthlyNewsletter.$inject = ['$location', 'monthlyNewsletterSvc'];

    function yearMonthlyNewsletter($location, monthlyNewsletterSvc) {
        var vm = this;
        vm.title = '年度月报简报列表';

        activate();
        function activate() {
            monthlyNewsletterSvc.theMonthGrid(vm);
        }
    }
})();
