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

                //记录收文日期
                vm.oldSignDate = vm.sign.signdate;
                //记录上一次总评审天数
                vm.totalReviewDays = vm.sign.totalReviewdays;

                //记录上一次延长的天数
                vm.lengthenDays = vm.sign.lengthenDays;

                //计算已逝工作日  已逝工作日 = 总评审天数 - 剩余工作日
                vm.sign.reviewdays = vm.totalReviewDays - vm.sign.surplusdays;
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

        /**
         * 改变延长工作日时，计算评审天数和剩余工作日
         */
        vm.changeLengthenDays = function(){
            //原来的评审天数 + 延长天数
            vm.sign.surplusdays +=  vm.sign.lengthenDays;

            //原来的剩余工作日 + 延长天数
            vm.sign.totalReviewdays +=  vm.sign.lengthenDays;
        }

        /**
         * 修改收文日期时，重新计算剩余工作日 ，已逝工作日
         * 通过原来的收文日期，与修改后新的收文日期来计算剩余工作日和已逝工作日 ，
         * 按原来日期和现在日期之间有多少个工作日进行加减
         * 如果现在的日期大于原来的日期，则剩余工作日增加，否则减少
         *
         */
        vm.changeSignDate = function(){
            reviewWorkdaysSvc.countWeekDays(vm.oldSignDate , vm.sign.signdate , function(data){
                vm.sign.surplusdays += data.reObj;
                vm.sign.reviewdays -= data.reObj;
            });


        }
    }
})();