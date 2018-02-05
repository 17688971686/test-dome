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
                if(!vm.expScoreList || vm.expScoreList.length == 0){
                    vm.noData = true;
                }
            });
        }
        /**
         * 查询
         */
        vm.search = function(){
            //把两个数放到一个变量中，因为url是传实体类的。到service在拆分
            if(vm.selectHis.scoreBegin){
                if(vm.selectHis.scoreEnd){
                    vm.selectHis.score=vm.selectHis.scoreBegin+vm.selectHis.scoreEnd;
                }else{
                    vm.selectHis.score=vm.selectHis.scoreBegin+5;//如果最大的选项没有写时。就到最大
                }
            }else{
                vm.selectHis.score=1+vm.selectHis.scoreEnd;//如歌最小的选项没有写时，就到最小
            }

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
