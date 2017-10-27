(function () {
    'expert strict';

    angular.module('app').controller('expertScoreCtrl', expertScore);

    expertScore.$inject = ['$location','expertSvc'];

    function expertScore($location, expertSvc) {
    	var vm = this;
    	vm.data={};
    	vm.title = '专家评分统计';

        vm.selectHis = {};      //专家抽取条件对象
        vm.expScoreList = [];

        /**
         * 查询
         */
        vm.search = function(){
            vm.isSubmit = true;
            expertSvc.expertScoreHis(vm.selectHis,function(data){
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
