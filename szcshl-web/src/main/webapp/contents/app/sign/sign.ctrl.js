(function () {
    'use strict';

    angular.module('app').controller('signCtrl', sign);

    sign.$inject = ['signSvc','$state','flowSvc','signFlowSvc','bsWin'];

    function sign(signSvc,$state,flowSvc,signFlowSvc,bsWin) {
        var vm = this;
        vm.title = "收文列表";

        active();
        function active() {
            signSvc.grid(vm);
        }

        //收文查询
        vm.querySign = function(){
        	signSvc.querySign(vm);
        }
        vm.check=function(){
      	 		vm.isAssociate = vm.ischeck?9:0;
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
         
         //************************** S 以下是新流程处理js **************************//
         vm.startNewFlow = function(signid){
             bsWin.confirm({
                 title: "询问提示",
                 message: "确认已经完成填写，并且发起流程么？",
                 onOk: function () {
                     $('.confirmDialog').modal('hide');
                     signFlowSvc.startFlow(signid,function(data){
                         if(data.flag || data.reCode == 'ok'){
                             vm.gridOptions.dataSource.read();
                             bsWin.success("操作成功！");
                         }else{
                             bsWin.error(data.reMsg);
                         }
                     });
                 }
             });
         }

        /**
         * 正式签收收文
         * @param signId
         */
        vm.realSign = function(signid){
            common.confirm({
                vm:vm,
                title:"",
                msg:"确认正式签收了么？",
                fn:function () {
                    $('.confirmDialog').modal('hide');
                    signSvc.realSign(vm,signid);
                }
            })
        }
         //************************** S 以下是新流程处理js **************************//

    }
})();
