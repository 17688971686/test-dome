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
        	//先初始化流程信息        
        	vm.flowDeal = true;
        	flowSvc.initFlowData(vm);
        	flowSvc.getNextStepInfo(vm);
        	
        	//再初始化业务信息
        	signSvc.initFlowPageData(vm);
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
        
        //S_隐藏工作方案按钮判断
        vm.hideWorkBt = function(){
        	if(vm.flow.curNodeAcivitiId == "approval"){
        		if(vm.model.isreviewcompleted > 0 ){
            		vm.showWorkBt = false;
            		return true;
            	}else{
            		vm.showWorkBt = true;
            		return false;
            	}
    		}else{
    			vm.showWorkBt = false;
        		return true;
    		}
        }//E_隐藏工作方案按钮判断
       
        //S_跳转到 工作方案 编辑页面
        vm.addWorkProgram = function(){
        	$state.go('workprogramEdit', {signid:vm.model.signid});
        }//E_跳转到 工作方案 编辑页面
        
        //E_隐藏收文按钮判断
        vm.hideDisPatchBt = function(){
        	var hidden = true;
        	if(vm.flow.curNodeAcivitiId == "dispatch"){
        		if(vm.model.isDispatchCompleted > 0 ){
        			hidden = true;
            	}else{
            		hidden = false;
            	}
    		}        	
        	return hidden;
        }//E_隐藏收文按钮判断
        
        //S_跳转到 发文 编辑页面
        vm.addDisPatch = function(){
        	$state.go('dispatchEdit', {signid:vm.model.signid});
        }//E_跳转到 发文 编辑页面
    }
})();
