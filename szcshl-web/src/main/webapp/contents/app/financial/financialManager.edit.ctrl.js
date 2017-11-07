/**
 * 停用
 */
(function () {
    'use strict';

    angular.module('app').controller('financialManagerEditCtrl', financialManager);

    financialManager.$inject = ['$location', 'financialManagerSvc', '$state' , 'signSvc' , 'bsWin'
        , 'expertReviewSvc' , 'adminSvc' , 'addCostSvc'];

    function financialManager($location, financialManagerSvc, $state , signSvc , bsWin , expertReviewSvc , adminSvc , addCostSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '评审费统计管理';
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.model = {};
        vm.isuserExist = false;
        vm.id = $state.params.id;
        vm.financial.businessId = $state.params.businessId;
        vm.costType = $state.params.costType;
      
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '评审费统计管理';
        }
        //评审费放表业务对象
        vm.businessFlag = {
       		 expertReviews : [],   	
       }

        /**
         * 评审费录入
         * @param object
         */
        vm.addCostWindow = function(object){
            addCostSvc.initAddCost(vm,vm.costType , object);

        }

        /**
         * 查询
         */
        vm.queryUser = function (){

            activate();
        }

        /**
         * 重置
         */
        vm.resetQuery = function(){
            vm.model = {};
        }

        activate();
        function activate() {
            adminSvc.initSignList(function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.orgDeptList = data.reObj;
                }
            });

            financialManagerSvc.initfinancial(vm , function(data){
                vm.stageCountList = data;
            });

        }
    }
})();
