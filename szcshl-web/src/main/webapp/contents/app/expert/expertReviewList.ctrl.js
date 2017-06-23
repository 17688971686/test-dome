(function () {
    'expert strict';

    angular.module('app').controller('expertReviewListCtrl', expertReviewList);

    expertReviewList.$inject = ['$location','expertSvc'];

    function expertReviewList($location, expertSvc) {
    	var vm = this;
    	vm.data={};
    	vm.title = '专家评分一览表';

        activate();
        function activate() {
        }


    }
})();
