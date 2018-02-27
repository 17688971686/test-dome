(function () {
    'use strict';

    angular.module('app').controller('monthlyNewsletterEditCtrl', monthlyNewsletter);

    monthlyNewsletter.$inject = ['$location', 'monthlyNewsletterSvc', '$state', 'bsWin'];

    function monthlyNewsletter($location, monthlyNewsletterSvc, $state, bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加月报简报';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        vm.monthly = {};

        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新月报简报';
        }
        //报告月份
        vm.selectMonthly = function () {
            vm.monthly.endTheMonths = vm.monthly.theMonths;
            vm.monthly.staerTheMonths = "1";

        }
        //报告年度
        vm.reportYear = function () {
            var reportMultiyear = vm.monthly.reportMultiyear;
            vm.monthly.startMoultiyear = reportMultiyear;
            vm.monthly.endMoultiyear = reportMultiyear;
        }
        //开始月份
        vm.startMonthly = function () {
            var end = parseInt(vm.monthly.endTheMonths);
            var start = parseInt(vm.monthly.staerTheMonths);
            if (end < start) {
                bsWin.alert("开始月份不能大于结束月份");
                vm.monthly.staerTheMonths = " ";
            }
        }

        //添加月报简报
        vm.createMothlyNewsletter = function () {
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                monthlyNewsletterSvc.createMonthlyNewsletter(vm.monthly, function (data) {
                    if (data.flag || data.reCode == "ok") {
                        vm.monthly = data.reObj;
                        bsWin.alert("操作成功！");
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            } else {
                bsWin.alert("缺少部分没有填写，请仔细检查");
            }
        };
        //编辑月报简报
        vm.updateMonthly = function () {
            monthlyNewsletterSvc.updateMonthlyNewsletter(vm);
        };

        /**
         * 生成月报简报
         */
        vm.createMonthReport = function () {
            monthlyNewsletterSvc.createMonthReport(vm);
        }

        /**
         * 初始化月报简报默认生成上个月数据
         */
        function initRepData() {
            var date = new Date();
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            if(month == 1){
                vm.monthly.reportMultiyear = (year - 1)+"";
                vm.monthly.startMoultiyear = (year - 1)+"";
                vm.monthly.endMoultiyear = (year - 1)+"";
                vm.monthly.theMonths = "12";
                vm.monthly.staerTheMonths = "1";
                vm.monthly.endTheMonths = "12";
            }else{
                vm.monthly.reportMultiyear = year + "";
                vm.monthly.startMoultiyear = year + "";
                vm.monthly.endMoultiyear = year + "";
                vm.monthly.theMonths = (month - 1) + "";
                vm.monthly.staerTheMonths = "1";
                vm.monthly.endTheMonths = (month - 1) + "";
            }
        }

        activate();
        function activate() {
            if (vm.isUpdate) {
                monthlyNewsletterSvc.getMonthlyNewsletterById(vm);
            }else{
                initRepData();
            }
        }
    }
})();
