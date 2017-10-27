(function () {
    'expert strict';

    angular.module('app').controller('expertSelectHisCtrl', expertSelectHis);

    expertSelectHis.$inject = ['$location','expertSvc'];
    
    function expertSelectHis($location, expertSvc) {
    	var vm = this;
    	vm.title = '专家抽取统计';
    	vm.selectHis = {};      //专家抽取条件对象
        vm.expSelectList = [];

        /**
         * 查询
         */
        vm.search = function(){
            vm.isSubmit = true;
            expertSvc.expertSelectHis(vm.selectHis,function(data){
                vm.isSubmit = false;
                if(!data || data.length ==0){
                    vm.noData = true;
                }else{
                    vm.noData = false;
                    vm.expScoreList = data;
                }
            });
        }
        /**
         * 重置查询条件
         */
        vm.formReset = function(){
            vm.selectHis = {};
        }
    }
})();
