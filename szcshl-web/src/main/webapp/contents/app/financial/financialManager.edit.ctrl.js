(function () {
    'use strict';

    angular.module('app').controller('financialManagerEditCtrl', financialManager);

    financialManager.$inject = ['$location', 'financialManagerSvc', '$state'];

    function financialManager($location, financialManagerSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '评审费统计管理';
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.isuserExist = false;
        vm.id = $state.params.id;
        vm.financial.signid = $state.params.signid;
      
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '评审费统计管理';
        }
        //评审费放表业务对象
        vm.businessFlag = {
       		 expertReviews : [],   	
       }
        
        vm.create = function () {
        	
            financialManagerSvc.createFinancialManager(vm);
        };
        vm.update = function () {
            financialManagerSvc.updateFinancialManager(vm);
        };
    
        activate();
        function activate() {
        	  financialManagerSvc.grid(vm);
        	  if(vm.financial.signid){
        		  financialManagerSvc.findStageCostTableList(vm.financial.signid,function(data){
                      vm.businessFlag.expertReviews = data.value;
                      console.log(vm.businessFlag.expertReviews);
                  });
        	  }
        	 
        	  //financialManagerSvc.stageCostCountList(vm);
        	//  financialManagerSvc.initFinancialProject(vm);
          
        }
    }
})();
