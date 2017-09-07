(function () {
    'use strict';

    angular.module('app').controller('myTopicCtrl', myTopic);

    myTopic.$inject = ['bsWin', '$scope', 'sysfileSvc', 'topicSvc'];

    function myTopic(bsWin, $scope, sysfileSvc, topicSvc) {
        var vm = this;
        vm.title = '我的课题列表';

        activate();
        function activate() {
            topicSvc.initMyGird(vm);
        }
        //表单查询
        vm.searchForm = function(){
            vm.myTopicOptions.dataSource.read();
        }

        //重置查询表单
        vm.formReset = function(){
            vm.searchModel = {};
        }
    }
})();
