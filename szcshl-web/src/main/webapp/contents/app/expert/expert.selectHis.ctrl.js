(function () {
    'expert strict';

    angular.module('app').controller('expertSelectHisCtrl', expertSelectHis);

    expertSelectHis.$inject = ['$location','expertSvc'];
    
    function expertSelectHis($location, expertSvc) {
    	var vm = this;
    	vm.title = '专家抽取统计';
    	vm.selectHis = {};      //专家抽取条件对象
        vm.selectHis.beginTime = (new Date()).yearAgo(1);
        vm.selectHis.endTime = (new Date()).Format("yyyy-MM-dd");

        vm.expSelectList = [];

        activate();
        function activate() {
            vm.isSubmit = true;
            vm.noData = false;
            expertSvc.expertSelectHis(vm.selectHis,function(data){
                vm.isSubmit = false;
                vm.expSelectList = data;
                if(!data || data.length ==0){
                    vm.noData = true;
                }
            });
        }

        /**
         * 查询
         */
        vm.search = function(){
            activate();
        }
        /**
         * 重置查询条件
         */
        vm.formReset = function(){
            vm.selectHis = {};
        }
    }
})();
