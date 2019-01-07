(function () {
    'use strict';

    angular.module('app').controller('financialManagerEditCtrl', financialManager);

    financialManager.$inject = ['$location', 'financialManagerSvc', '$state' , 'signSvc' , 'bsWin', 'expertReviewSvc' , 'adminSvc' , 'addCostSvc'];

    function financialManager($location, financialManagerSvc, $state , signSvc , bsWin , expertReviewSvc , adminSvc , addCostSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '评审费录入';
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        //初始化查询时间范围
        vm.model = {};
        vm.model.beginTime = (new Date()).halfYearAgo();
        vm.model.endTime = (new Date()).Format("yyyy-MM-dd");

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

        activate();
        function activate() {
            if(!vm.exist){
                vm.page = lgx.page.init({
                    id: "demo5", get: function (o) {
                        var skip ;
                        vm.price ={};

                        //oracle的分页不一样。
                        if (o.skip != 0) {
                            skip = o.skip + 1
                        } else {
                            skip = o.skip
                        }
                        vm.price.skip = skip;//页码
                        vm.price.size = parseInt(o.size) + parseInt(o.skip);//页数

                        financialManagerSvc.initfinancial(vm, function (data) {
                            vm.stageCountList = [];
                            if(data){
                                vm.stageCountList = data.value;
                                vm.page.callback(data.count);//请求回调时传入总记录数
                            }

                        });
                    }
                });
                vm.exist = true;
            }else{
                vm.page.selPage(1);
            }
            //查询评审部门
            adminSvc.initSignList(function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.orgDeptList = data.reObj;
                }
            });
        }

        /**
         * 评审费录入
         * @param object
         */
        vm.showCostWindow = function(object,id){
            addCostSvc.initAddCost(vm,vm.costType,object,id);
        }

        /**
         * 查询
         */
        vm.queryFinancl = function (){
            //统计评审费信息
            financialManagerSvc.initfinancial(vm, function (data) {
                vm.stageCountList = [];
                if (data) {
                    vm.stageCountList = data.value;
                    vm.page.callback(data.count);//请求回调时传入总记录数
                    vm.page.selPage(1);          //跳回第一页
                }
            });
        }

        /**
         * 重置
         */
        vm.resetQuery = function(){
            vm.model = {};
        }

    }
})();
