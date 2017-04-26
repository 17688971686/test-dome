(function () {
    'use strict';

    angular.module('app').controller('signFlowDealCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location,signSvc,$state) {        
        var vm = this;
        vm.title = "项目流程处理";
        vm.model = {};
        vm.flow = {};
        vm.model.signid = $state.params.signid;	//业务ID
        vm.flow.taskId = $state.params.taskId;	//流程任务ID
        vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID
        
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
        	
        	signSvc.initFlowDeal(vm);   
        }
                
        vm.commitNextStep = function (){
        	signSvc.commitNextStep(vm);
        }
    }
})();
