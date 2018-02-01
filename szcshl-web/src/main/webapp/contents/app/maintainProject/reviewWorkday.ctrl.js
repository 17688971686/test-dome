(function(){
    'use strict';

    angular.module('app').controller('reviewWorkdayCtrl', reviewWorkday);

    reviewWorkday.$inject = ['$state' , 'reviewWorkdaysSvc' , 'bsWin'];

    function reviewWorkday($state , reviewWorkdaysSvc , bsWin){

        var vm = this ;
        vm.signId = $state.params.signid;
        vm.sign = {};

        vm.totalReviewDays = 0;//记录上次的总评审天数
        vm.lengthenDays = 0 ;//记录上次的延长天数
        activate();
        function activate(){
            reviewWorkdaysSvc.initReviewWorkDays(vm , function(data){
                vm.sign = data;
                vm.totalReviewDays = vm.sign.totalReviewdays;
                vm.lengthenDays = vm.sign.lengthenDays;
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

        /**
         * 改变评审时限时，剩余工作日、延长工作日自动更新
         */
        vm.changeTotalReviewDays = function(){

            //1、计算中实际延长工作日   实际延长工作日 =（现总评审天数 - 上次总评审天数 + 上次延长天数）
            vm.sign.lengthenDays = vm.sign.totalReviewdays - vm.totalReviewDays + vm.lengthenDays;

            //2、计算剩余工作日  剩余工作日 = 总评审天数 - 已逝工作日
            vm.sign.surplusdays = vm.sign.totalReviewdays - vm.sign.reviewdays;

        }
    }
})();