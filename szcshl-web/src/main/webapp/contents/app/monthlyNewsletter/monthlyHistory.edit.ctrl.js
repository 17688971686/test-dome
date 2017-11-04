(function () {
    'use strict';

    angular.module('app').controller('monthlyHistoryEditCtrl', monthlyHistory);

    monthlyHistory.$inject = ['$location', 'monthlyHistorySvc', '$state','bsWin'];

    function monthlyHistory($location, monthlyHistorySvc, $state,bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加月报简报历史数据';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        vm.monthly = {};
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新月报简报';
        }

        //创建月报简报历史数据
        vm.createHistory = function () {
            common.initJqValidation();
            var isValid = $('form').valid();
	          if(isValid){
	        	  monthlyHistorySvc.createmonthlyHistory(vm.monthly,function(data){
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
        vm.updateMonthly = function () {
            monthlyHistorySvc.updatemonthlyHistory(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                monthlyHistorySvc.getmonthlyHistoryById(vm);
            }
        }
    }
})();
