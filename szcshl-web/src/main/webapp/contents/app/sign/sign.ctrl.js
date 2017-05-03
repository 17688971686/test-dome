(function () {
    'use strict';

    angular.module('app').controller('signCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location,signSvc,$state) {        
        var vm = this;
        vm.title = "收文列表";
        //initGrid
        signSvc.grid(vm);
        
        vm.querySign = function(){
        	signSvc.querySign(vm);
        }
        vm.updateSign = function(){
        	signSvc.updateSign(vm);
        }
        vm.fillSignTest = function() {
        	
        	$state.go('fillSign', {signid: "8371bfa6-9084-4b0e-b2fd-5ea3aea51385"});
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
          }
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
         }
          //end 收文删除
    }
})();
