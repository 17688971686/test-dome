(function () {
    'use strict';
    angular.module('app').controller('signCtrl', sign);

    sign.$inject = ['signSvc','$state','flowSvc','signFlowSvc','bsWin','$rootScope'];

    function sign(signSvc,$state,flowSvc,signFlowSvc,bsWin,$rootScope) {
        var vm = this;
        vm.model = {};						//创建一个form对象
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.gridOptions.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});
        }
        active();
        function active() {
            if($rootScope.view[vm.stateName]){
                var preView = $rootScope.view[vm.stateName];
                //恢复grid
                //恢复查询条件
                if(preView.gridParams){
                    vm.gridParams = preView.gridParams;
                }
                //恢复表单参数
                if(preView.data){
                    vm.model = preView.data.model;
                }
                //恢复数据
                /*vm.project = preView.data.project;*/
                //恢复页数页码
                if(preView.queryParams){
                    vm.queryParams=preView.queryParams;
                }

                signSvc.signGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                signSvc.signGrid(vm);
            }
        }

        //获取委里签收信息
        vm.getSignInfo = function(){
            if(vm.model.filecode == "" || vm.model.filecode == null){
                bsWin.alert("收文编号不能为空!");
                return ;
            }
            signSvc.getSignInfo(vm.model.filecode,'0',function(data){
                if(data.flag || data.reCode == 'ok'){
                    if(data.reMsg!='保存成功！'){
                        bsWin.alert(data.reMsg);
                        return;
                    }else{
                        vm.gridOptions.dataSource.read();
                    }
                }else{
                    bsWin.alert(data.reMsg);
                }
            });
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
