(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiyearEditCtrl', monthlyMultiyear);

    monthlyMultiyear.$inject = ['$location', 'monthlyMultiyearSvc', '$state','bsWin'];

    function monthlyMultiyear($location, monthlyMultiyearSvc, $state,bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加中心文件稿纸';
        vm.isuserExist = false;
        vm.suppletter ={};//文件稿纸对象
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新月报简报';
        }

        //保存中心文件（稿纸）
        vm.saveAddSuppletter = function () {
           // monthlyMultiyearSvc.createmonthlyMultiyear(vm);
            common.initJqValidation();
            var isValid = $('form').valid();
	          if(isValid){
	        	  monthlyMultiyearSvc.createmonthlyMultiyear(vm.suppletter,function(data){
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
            monthlyMultiyearSvc.updatemonthlyMultiyear(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                monthlyMultiyearSvc.getmonthlyMultiyearById(vm);
            }
            monthlyMultiyearSvc.initMonthlyMultiyear(vm);
            
        }
    }
})();
