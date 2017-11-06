(function () {
    'use strict';

    angular.module('app').controller('monthlyNewsletterEditCtrl', monthlyNewsletter);

    monthlyNewsletter.$inject = ['$location', 'monthlyNewsletterSvc', '$state','bsWin'];

    function monthlyNewsletter($location, monthlyNewsletterSvc, $state,bsWin) {
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
        vm.selectMonthly =function(){
            var theMonths =vm.monthly.theMonths;
            vm.monthly.endTheMonths =theMonths;

        }
        //报告年度
        vm.reportYear = function() {
            var reportMultiyear = vm.monthly.reportMultiyear;
            vm.monthly.startMoultiyear = reportMultiyear;
            vm.monthly.endMoultiyear = reportMultiyear;
        }
        //开始月份
        vm.startMonthly = function(){
            if( vm.monthly.endTheMonths < vm.monthly.staerTheMonths){
                bsWin.alert("开始月份不能大于结束月份");
                vm.monthly.staerTheMonths ="";
                return false;
            }
        }

        //添加月报简报
        vm.createMothlyNewsletter = function () {
            common.initJqValidation();
            var isValid = $('#form').valid();
	          if(isValid){
	        	  monthlyNewsletterSvc.createMonthlyNewsletter(vm.monthly,function(data){
	                   if (data.flag || data.reCode == "ok") {
                           vm.monthly = data.reObj;
                           bsWin.alert("操作成功！");
	                   }else{
	                       bsWin.error(data.reMsg);
	                   }
	               });
	           }else{
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

        activate();
        function activate() {
            if (vm.isUpdate) {
                monthlyNewsletterSvc.getMonthlyNewsletterById(vm);
            }
        }
    }
})();
