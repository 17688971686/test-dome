(function () {
    'use strict';

    angular.module('app').controller('expertReviewEditCtrl', expertReview);

    expertReview.$inject = ['$location', 'expertReviewSvc', '$state'];

    function expertReview($location, expertReviewSvc, $state) {
        var vm = this;
        vm.title = '添加附件';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新附件';
        }

        vm.create = function () {
            expertReviewSvc.createExpertReview(vm);
        };
        vm.update = function () {
            expertReviewSvc.updateExpertReview(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                expertReviewSvc.getExpertReviewById(vm);
            }
        }
    }
})();
