(function () {
    'use strict';

    angular.module('app').controller('workdayEditCtrl', workdayEdit);

    workdayEdit.$inject = ['$location', 'workdaySvc','$state'];

    function workdayEdit($location, workdaySvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加工作日';
        vm.id=$state.params.id;
        vm.workday={};
        vm.workday.status="1";//初始化状态
        if(vm.id){
        	vm.isUpdate=true;
        	vm.title="更新工作日";
        }
        
        
        activate();
        function activate() {
        	if(vm.isUpdate){
        		workdaySvc.getWorkdayById(vm);
        	}
        }
        
        vm.create=function(){
        	workdaySvc.createWorkday(vm);
        }
        
        vm.update=function(){
        	workdaySvc.updateWorkday(vm);
        }
    }
})();
