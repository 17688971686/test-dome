(function () {
    'use strict';

    angular.module('app').controller('adminCtrl', admin);

    admin.$inject = ['$location','adminSvc']; 

    function admin($location, adminSvc) {
        var vm = this;
        vm.title = '待办事项';
        vm.sign={};     
        activate();
        function activate() {
        	adminSvc.gtasksGrid(vm);
        	adminSvc.initAnnountment(vm);
        }
        vm.countWorkday=function(){
        	adminSvc.countWorakday(vm);
        }
        
        //初始化项目查询统计
        adminSvc.initSignList(vm);
        adminSvc.getSignList(vm);
        
         //重置
         vm.formReset=function(){
         		var tab = $("#searchform").find('input,select');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
         }
         
         //項目查詢統計
         vm.searchSignList=function(){
         	adminSvc.searchSignList(vm);
         }
    }
})();
