(function () {
    'expert strict';

    angular.module('app').controller('expertCtrl', expert);
    
    expert.$inject = ['$location','expertSvc']; 
    
    function expert($location, expertSvc) {
    	var vm = this;
    	vm.data={};
    	vm.title = '专家列表';

        activate();
        function activate() {
            expertSvc.grid(vm);
        }

        vm.search = function () {
        	expertSvc.searchMuti(vm);
        };
        
        vm.searchAudit = function () {
        	expertSvc.searchMAudit(vm);
        };
        
        vm.del = function (id) {        	
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');  
                  	expertSvc.deleteExpert(vm,id);
                 }
             })
        };
        
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
       };

    }
})();
