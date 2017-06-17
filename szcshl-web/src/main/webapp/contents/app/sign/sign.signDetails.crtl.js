(function () {
    'use strict';

    angular.module('app').controller('signDetailsCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc'];

    function sign($location, signSvc,$state,flowSvc) {
        var vm = this;
    	vm.model = {};							//创建一个form对象   	
        vm.title = '查看详情信息';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID


        active();
        function active(){
            $('#myTab li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
            })

            signSvc.initFlowPageData(vm);

            signSvc.initAssociateSigns(vm,vm.model.signid);
            if($state.params.processInstanceId){
                vm.flow = {}
                vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID
                flowSvc.initFlowData(vm);
            }
        }

    }
})();
