(function () {
    'use strict';

    angular.module('app').controller('monthlyNewsletterEditCtrl', monthlyNewsletter);

    monthlyNewsletter.$inject = ['$location', 'monthlyNewsletterSvc', '$state'];

    function monthlyNewsletter($location, monthlyNewsletterSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加月报简报';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新月报简报';
        }

        //添加月报简报
        vm.createMothlyNewsletter = function () {
            monthlyNewsletterSvc.createMonthlyNewsletter(vm);
        };
        vm.update = function () {
            monthlyNewsletterSvc.updateMonthlyNewsletter(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                monthlyNewsletterSvc.getMonthlyNewsletterById(vm);
            }
        }
    }
})();
