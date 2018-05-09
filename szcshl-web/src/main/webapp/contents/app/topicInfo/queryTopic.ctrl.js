(function () {
    'use strict';

    angular.module('app').controller('queryTopicCtrl', queryTopic);

    queryTopic.$inject = ['bsWin', '$scope', 'sysfileSvc', 'topicSvc'];

    function queryTopic(bsWin, $scope, sysfileSvc, topicSvc) {
        var vm = this;
        vm.title = '课题查询';

        activate();
        function activate() {
            topicSvc.queryGrid(vm);
        }


        //表单查询
        vm.searchForm = function(){
            vm.queryTopicOptions.dataSource._skip=0;
            vm.queryTopicOptions.dataSource.read();
        }

        //重置查询表单
        vm.formReset = function(){
            vm.searchModel = {};
        }
    }
})();
