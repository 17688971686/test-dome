(function () {
    'use strict';
    angular.module('app').controller('signCtrl', sign);

    sign.$inject = ['signSvc','$state','flowSvc','signFlowSvc','bsWin'];

    function sign(signSvc,$state,flowSvc,signFlowSvc,bsWin) {
        var vm = this;
       
        active();
        function active() {
            signSvc.signGrid(vm);
        }

        //收文查询
        vm.querySign = function(){
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        vm.check=function(){
            vm.isAssociate = vm.ischeck?9:0;
      	 }
        
        //start 收文删除
        vm.del = function (signid) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认删除该条项目数据吗？",
                onOk: function () {
                    $('.confirmDialog').modal('hide');
                    signSvc.deleteSign(signid,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            bsWin.alert("删除成功！",function(){
                                vm.gridOptions.dataSource.read();
                            })
                        }else{
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            });
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
                             bsWin.success("操作成功！",function(){
                                 vm.gridOptions.dataSource.read();
                             });
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
            bsWin.confirm({
                title: "询问提示",
                message: "确认正式签收了么？",
                onOk: function () {
                    $('.confirmDialog').modal('hide');
                    signSvc.realSign(signid,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            bsWin.success("操作成功！",function(){
                                vm.gridOptions.dataSource.read();
                            });
                        }else{
                            bsWin.error(data.reMsg);
                        }
                    });
                }
            });
        }

    }
})();
