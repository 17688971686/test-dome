(function () {
    'use strict';

    angular.module('app').controller('signFlowDealCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc','signFlowSvc']; 

    function sign($location,signSvc,$state,flowSvc,signFlowSvc) {        
        var vm = this;
        vm.title = "项目流程处理";       
        vm.model = {};
        vm.flow = {};					
        vm.work = {};
        vm.dispatchDoc = {};
        vm.fileRecord = {};
        
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
        	flowSvc.initFlowData(vm);
        	//再初始化业务信息
        	signSvc.initFlowPageData(vm);
        }
        
        vm.commitNextStep = function (){
        	if(signFlowSvc.checkBusinessFill(vm)){
        		flowSvc.commit(vm);
        	}else{
        		common.alert({
					vm:vm,
					msg: "请先完成相应的业务操作才能提交",					
				})
        	}      	
        }
        
        vm.commitBack = function(){
        	flowSvc.rollBack(vm);       	//回退到上一个环节
        }              
        
        vm.deleteFlow = function(){
        	common.confirm({
             	 vm:vm,
             	 title:"",
             	 msg:"终止流程将无法恢复，确认挂起么？",
             	 fn:function () {
                   	$('.confirmDialog').modal('hide');             	
                   	flowSvc.deleteFlow(vm);
                }
             })
        }  
        
        vm.initDealUerByAcitiviId = function(){
        	flowSvc.initDealUerByAcitiviId(vm);
        }
        
        //根据特定的环节隐藏相应的业务按钮
        vm.showBtByAcivitiId = function(acivitiId){
        	return vm.flow.curNodeAcivitiId == acivitiId?true:false;       	
        }
       
        //S_跳转到 工作方案 编辑页面
        vm.addWorkProgram = function(){
        	$state.go('workprogramEdit', {signid:vm.model.signid});
        }//E_跳转到 工作方案 编辑页面               
               
        //S_跳转到 发文 编辑页面
        vm.addDisPatch = function(){
        	$state.go('dispatchEdit', {signid:vm.model.signid});
        }//E_跳转到 发文 编辑页面
                   
        vm.addDoFile = function(){
        	$state.go('fileRecordEdit', {signid:vm.model.signid});
        }
        
        //业务判断
        vm.checkBox = function($event,type,disabletype){
        	var checkbox = $event.target;  
            var checked = checkbox.checked;  
            var checkboxValue = checkbox.value;
            if(checked){  
            	$('.seleteTable input[selectType=\"'+type+'\"]').each(function () {
            		var id = $(this).attr("id");            	
            		var value = $(this).attr("value");           		          		
            		if(id != (type+"_"+checkboxValue)){ 
            			$("#"+disabletype+"_"+value).removeAttr("disabled");  
            			$(this).removeAttr("checked");           			
            		}else{            			
            			$("#"+disabletype+"_"+checkboxValue).attr("disabled","disabled");  
            		}
            	});                 
            }else{  
            	$("#"+disabletype+"_"+checkboxValue).removeAttr("disabled");  
            }  
        }
    }
})();
