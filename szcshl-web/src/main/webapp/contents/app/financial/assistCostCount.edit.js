(function () {
    'use strict';

    angular.module('app').controller('assistCostCountSvcEditCtrl', assistCostCount);

    assistCostCount.$inject = ['$location', 'assistCostCountSvc', '$state','$http'];

    function assistCostCount($location, assistCostCountSvc, $state,$http) {
        /* jshint validthis:true */
        var vm = this;
        vm.model={};
        vm.title = '财务管理';
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.isuserExist = false;
        vm.id = $state.params.id;
        vm.financial.businessId = $state.params.businessId;
     
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新财务管理';
        }

        //查询
        vm.queryAssistCost = function(){
        	assistCostCountSvc.assistCostCountList(vm ,function(data){
        		 vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
        	});
        }
        //重置
        vm.assistCostReset = function(){
        	vm.model = {};
        }
        
        vm.create = function () {
            assistCostCountSvc.createassistCostCount(vm);
        };
        vm.update = function () {
            assistCostCountSvc.updateassistCostCount(vm);
        };
    
        activate();
        function activate() {
        	  assistCostCountSvc.grid(vm);
            if (vm.isUpdate) {
                assistCostCountSvc.getassistCostCountById(vm);
            }
            //协审费统计列表
            assistCostCountSvc.assistCostCountList(vm,function(data){
            	vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
            });
            
            //协审费录入列表
            assistCostCountSvc.assistCostList(vm,function(data){
            	vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
            });
        }
    }
})();
