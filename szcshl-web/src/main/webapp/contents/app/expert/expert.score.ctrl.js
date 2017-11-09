(function () {
    'expert strict';

    angular.module('app').controller('expertScoreCtrl', expertScore);

    expertScore.$inject = ['$location','expertSvc'];

    function expertScore($location, expertSvc) {
    	var vm = this;
    	vm.data={};
    	vm.title = '专家评分统计';

        vm.selectHis = {};      //专家抽取条件对象
        vm.selectHis.beginTime = (new Date()).yearAgo(1);
        vm.selectHis.endTime = (new Date()).Format("yyyy-MM-dd");

        vm.expScoreList = [];

        activate();
        function activate() {
            vm.isSubmit = true;
            vm.noData = false;
            expertSvc.expertScoreHis(vm.selectHis,function(data){
                vm.isSubmit = false;
                vm.expScoreList = data;
                console.log(vm.expScoreList);
                if(!vm.expScoreList || vm.expScoreList.length == 0){
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
