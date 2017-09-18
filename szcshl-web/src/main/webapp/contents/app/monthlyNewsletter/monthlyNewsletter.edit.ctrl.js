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

        //添加月报简报
        vm.createMothlyNewsletter = function () {
        	 common.initJqValidation();
             var isValid = $('form').valid();
	          if(isValid){
	        	  monthlyNewsletterSvc.createMonthlyNewsletter(vm.monthly,function(data){
	                   if (data.flag || data.reCode == "ok") {
	                           bsWin.alert("操作成功！");
	                   }else{
	                       bsWin.error(data.reMsg);
	                   }
	               });
	           }else{
	        	   bsWin.alert("缺少部分没有填写，请仔细检查");
	           }
        };
        vm.update = function () {
            monthlyNewsletterSvc.updateMonthlyNewsletter(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                monthlyNewsletterSvc.getMonthlyNewsletterById(vm);
            }
        }
    }
})();
