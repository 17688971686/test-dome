(function () {
    'use strict';

    angular.module('app').controller('bookBorrowCtrl', bookBorrow);

    bookBorrow.$inject = ['$location', 'bookBuySvc'];

    function bookBorrow($location, bookBuySvc) {
        var vm = this;
        vm.title = '借书列表';
        vm.model = {};

        activate();
        function activate() {
            bookBuySvc.bookBorrowGrid(vm);
        }
        //表单查询
        vm.searchForm = function(){
            vm.gridOptions.dataSource.read();
        }
        //重置查询表单
        vm.formReset = function(){
            vm.searchModel = {};
        }
    }
})();
