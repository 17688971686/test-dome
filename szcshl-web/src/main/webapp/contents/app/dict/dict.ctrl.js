(function () {
    'use strict';

    angular
        .module('app')
        .controller('dictCtrl', dict);

    dict.$inject = ['$location','dictSvc','$scope']; 

    function dict($location, dictSvc,$scope) {
    	  /* jshint validthis:true */
    	var vm = this;
        vm.title = '字典';
        
        vm.model = {};
        vm.del = function (id) {        	
        	 
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"删除字典将会连下级字典一起删除，确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                  	dictSvc.deleteDict(vm,id);
                 }
             })
        }
        vm.dels = function () {     

            var nodes = vm.dictsTree.getSelectedNodes();
            
            if (nodes&&nodes.length >0) {
            	 vm.del(nodes[0].id)
            } else {
            	common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            }   
       }
        
        activate();
        function activate() {
            dictSvc.initDictTree(vm);
        }
               
        
    }
    
   
})();
