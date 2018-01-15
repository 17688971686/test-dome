(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiFileCtrl', monthlyMultiFile);

    monthlyMultiFile.$inject = ['$location', 'monthlyMultiyearSvc','$state','bsWin'];

    function monthlyMultiFile($location, monthlyMultiyearSvc,$state,bsWin) {
        var vm = this;
        vm.title = '月报简报查询';

        activate();
        function activate() {
            monthlyMultiyearSvc.monthlyMultiyearGrid(vm);
        }

        //查询
         vm.addSuppQuery = function(){
             vm.multiyearGrid.dataSource._skip=0;
             vm.multiyearGrid.dataSource.read();
         }
         //重置
         vm.resetAddSupp = function(){
        	 var tab = $("#form").find('input,select');
 			$.each(tab, function(i, obj) {
 				obj.value = "";
 			});
         }
    }
})();
