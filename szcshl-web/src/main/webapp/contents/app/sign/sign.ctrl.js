(function () {
    'use strict';

    angular.module('app').controller('signCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc','signFlowSvc']; 

    function sign($location,signSvc,$state,flowSvc,signFlowSvc) {        
        var vm = this;
        vm.title = "收文列表";
        
        //initGrid
        signSvc.grid(vm);
                
        vm.querySign = function(){
        	signSvc.querySign(vm);
        }       
       
        //start 收文删除
        vm.del = function (signid) {       	   
             common.confirm({
              	 vm:vm,
              	 title:"",
              	 msg:"确认删除数据吗？",
              	 fn:function () {
                    	$('.confirmDialog').modal('hide');             	
                    	signSvc.deleteSign(vm,signid);
                 }
              })
         }//end 收文删除
        
         //start 收文删除
         vm.dels = function () {         	 
        	 var selectIds = common.getKendoCheckId('.grid');         	
             if (selectIds.length == 0) {
              	common.alert({
                  	vm:vm,
                  	msg:'请选择数据'                 	
                  });
              } else {
            	  var ids=[];
                  for (var i = 0; i < selectIds.length; i++) {
                  	ids.push(selectIds[i].value);
    			  }  
                  var idStr=ids.join(',');
                  vm.del(idStr);
              }   
         }//end 收文删除
         
         //start 发起流程
         vm.startFlow = function(signid){
        	 common.confirm({
              	 vm:vm,
              	 title:"",
              	 msg:"发起流程后，将不能对信息进行修改，确认发起流程么？",
              	 fn:function () {
                    	$('.confirmDialog').modal('hide');             	
                    	signSvc.startFlow(vm,signid);
                 }
              })
         }//end 发起流程
         
         //start 停止流程
         vm.stopFlow = function(signid){
        	 common.confirm({
              	 vm:vm,
              	 title:"",
              	 msg:"停止流程后，将无法对流程环节进行操作，确认停止么？",
              	 fn:function () {
                    $('.confirmDialog').modal('hide');             	
                    flowSvc.suspend(vm,signid);
                 }
              })
         }//end 停止流程
         
         //start 重启流程
         vm.restartFlow = function(signid){
        	 common.confirm({
              	 vm:vm,
              	 title:"",
              	 msg:"确认重启流程么？",
              	 fn:function () {
                    $('.confirmDialog').modal('hide');             	
                    flowSvc.activeFlow(vm,signid);
                 }
              })
         }//end 重启流程
         
         //************************** S 以下是新流程处理js **************************//
         vm.startNewFlow = function(signid){
        	 common.confirm({
              	 vm:vm,
              	 title:"",
              	 msg:"确认签收完成了么？",
              	 fn:function () {
                    	$('.confirmDialog').modal('hide');             	
                    	signFlowSvc.startFlow(vm,signid);
                 }
              })
         }
         //************************** S 以下是新流程处理js **************************//
    }
})();
