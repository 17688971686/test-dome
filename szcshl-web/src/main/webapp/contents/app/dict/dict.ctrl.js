(function () {
    'use strict';

    angular
        .module('app')
        .controller('dictCtrl', dict);

    dict.$inject = ['$location','dictSvc']; 

    function dict($location, dictSvc) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '字典';
        
        vm.model = {};
        vm.del = function (id) {        	
        	 
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                  	dictSvc.deleteDictData(vm,id);
                 }
             })
        }
        vm.dels = function () {     
			var nodes = vm.dictsTree.getCheckedNodes(true);
            var nodes_ids = $linq(nodes).select(function (x) { return { id: x.id};}).toArray();

            if (nodes.length == 0) {
            	common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            } else {
            	var ids=[];
            	var len = nodes.length;
            	
                for (var i = 0; i < len; i++) {
                	ids.push(nodes_ids[i].id);
				}  
               
                var idStr=ids.join(',');
                vm.del(idStr);
            }   
       }
        activate();
        function activate() {
           dictSvc.initDictGroupTree(vm);
        }
    }
    
   
})();
