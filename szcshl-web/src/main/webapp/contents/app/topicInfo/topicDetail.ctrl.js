(function () {
    'use strict';

    angular.module('app').controller('topicDetailCtrl', topicDetail);

    topicDetail.$inject = ['bsWin', '$scope' , '$state' , 'flowSvc', 'topicSvc'];

    function topicDetail(bsWin, $scope, $state , flowSvc, topicSvc) {
        var vm = this;

        vm.businessKey = $state.params.businessId;
        vm.flow = {};
        vm.flow.processInstanceId = $state.params.processInstanceId;
        activate();
        function activate() {
            $('#myTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
            })

            //流程图和流程处理记录信息
            if ($state.params.processInstanceId) {
                flowSvc.initFlowData(vm);
            }

            topicSvc.initFlowDeal(vm);
        }
    }
})();
