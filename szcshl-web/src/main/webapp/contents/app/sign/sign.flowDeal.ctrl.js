(function () {
    'use strict';

    angular.module('app').controller('signFlowDealCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc']; 

    function sign($location,signSvc,$state,flowSvc) {        
        var vm = this;
        vm.title = "项目流程处理";
        vm.model = {};
        vm.flow = {};
        vm.model.signid = $state.params.signid;	
        vm.flow.taskId = $state.params.taskId;			//流程任务ID
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
        	
        	signSvc.initFillData(vm);
        	
        	flowSvc.initFlowData(vm);
        	flowSvc.getNextStepInfo(vm);
        }
        
        vm.commitNextStep = function (){
        	flowSvc.commit(vm);
        }
        
        vm.commitBack = function(){
        	alert("流程回退！");
        }
        
        vm.commitOver = function(){
        	alert("流程结束！");
        }               
    }
})();
