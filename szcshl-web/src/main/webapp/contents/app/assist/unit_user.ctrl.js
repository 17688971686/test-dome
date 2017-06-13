(function () {
    'use strict';

    angular.module('app').controller('unitAndUserCtrl', unitAndUser);

    unitAndUser.$inject = ['$location','unitAndUserSvc','$state']; 

    function unitAndUser($location, unitAndUserSvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '人员列表';
        vm.id=$state.params.id;
        
        vm.showAddUserDialog=function(){
        	$('.addUser').modal({
        		backdrop:'static',
        		keyboard: false
        	});
        	 vm.unitAndUserGrid.dataSource.read();
        }
        
         vm.closeAddUserDialog=function(){
        	$('.addUser').modal('hide');		
        	
        }
        
        vm.add=function(assistUnitUserId){
        
      	  unitAndUserSvc.addUser(vm,assistUnitUserId);
        }
        
        vm.remove=function(assistUnitUserId){
        	 unitAndUserSvc.removeUser(vm,assistUnitUserId);
        }
        
        vm.removes=function(){
        	var selectIds=common.getKendoCheckId('.unitAndUserGrid');
        	if(selectIds.length==0){
        		common.alert({
        			vm:vm,
        			msg:"请选择数据"
        		});
        	}else{
        		var ids=[];
        		for(var i=0;i<selectIds.length;i++){
        			ids.push(selectIds[i].value);
        		}
        		var idStr=ids.join(',');
        		vm.remove(idStr);
        	}
        }
        
        
        activate();
        function activate() {
            unitAndUserSvc.unitAndUserGrid(vm);
            unitAndUserSvc.allUserGrid(vm);
        }
    }
})();
