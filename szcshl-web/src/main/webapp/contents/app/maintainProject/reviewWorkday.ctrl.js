(function(){
    'use strict';

    angular.module('app').controller('reviewWorkdayCtrl', reviewWorkday);

    reviewWorkday.$inject = ['$state' , 'reviewWorkdaysSvc' , 'bsWin'];

    function reviewWorkday($state , reviewWorkdaysSvc , bsWin){

        var vm = this ;
        vm.signId = $state.params.signid;
        vm.sign = {};

        activate();
        function activate(){
            reviewWorkdaysSvc.initReviewWorkDays(vm , function(data){
                vm.sign = data;
            });
        }

        vm.saveReview = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
            reviewWorkdaysSvc.saveReview(vm , function(data){
                if(data.flag || data.reCode == 'ok'){
                    bsWin.success("操作成功！");
                }
            });
            }
        }
    }
})();