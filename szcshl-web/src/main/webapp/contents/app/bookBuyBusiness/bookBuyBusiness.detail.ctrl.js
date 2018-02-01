(function () {
    'use strict';

    angular.module('app').controller('bookBuyBusinessDetailCtrl', bookBuyBusinessDetail);

    bookBuyBusinessDetail.$inject = ['bookBuyBusinessSvc', 'bsWin', '$state'];

    function bookBuyBusinessDetail(bookBuyBusinessSvc, bsWin, $state) {

        var vm = this;
        vm.model = {};
        vm.businessId = $state.params.businessId;
        vm.viewDetail = $state.params.viewDetail;
        vm.showGoBack = true;
        activate();
        function activate() {
            bookBuyBusinessSvc.getBookBuyBusinessById(vm,function(data){
                vm.model = data;
                if(vm.model.bookBuyList){
                    for(var i=0;i<vm.model.bookBuyList.length;i++){
                        vm.model.bookBuyList[i]["totalPrice"] = parseFloat(vm.model.bookBuyList[i].booksPrice)*(vm.model.bookBuyList[i].bookNumber);
                    }
                }

            });
        }
    }
})();
