(function () {
    'use strict';

    angular.module('app').controller('bookBuyEditCtrl', bookBuy);

    bookBuy.$inject = ['$location', 'bookBuySvc', '$state'];

    function bookBuy($location, bookBuySvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加图书管理信息';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新图书管理信息';
        }

        vm.create = function () {
            bookBuySvc.createBookBuy(vm);
        };
        vm.update = function () {
            bookBuySvc.updateBookBuy(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                bookBuySvc.getBookBuyById(vm);
            }
        }
    }
})();
