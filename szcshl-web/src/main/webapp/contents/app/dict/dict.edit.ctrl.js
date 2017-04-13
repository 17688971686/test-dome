(function () {
    'use strict';

    angular
        .module('app')
        .controller('dictEditCtrl', dict);

    dict.$inject = ['$scope','$location','dictSvc','$state']; 
    function dict($scope,$location, dictSvc,$state) {

    	var vm = this;
        vm.title = '增加字典';
        vm.model = {};
        vm.model.dictTypes = [{value:'0',name:'字典类型'},{value:'1',name:'字典数据项'}];
        vm.isdictGroupExist=false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '编辑字典';
        }
        
 
        vm.createDictGroup = function(){
        	dictSvc.createDictGroup(vm);
        };
        
        vm.updateDictGroup = function(){
        	dictSvc.updateDictGroup(vm);
        }
    	vm.dictTypeChange = function(){
    		if(vm.model.dictType){
    			vm.model.dictKey = '';
    		}
    		
    	};
    	
    	vm.apply = function(){
    		$scope.$apply();
    	}
    	
        activate();
        function activate() {
        	
        	if (vm.isUpdate) {
        		dictSvc.getDictById(vm)
        		//userSvc.getUserById(vm);
            } else {
            	vm.model.dictCode = '';
            	dictSvc.initpZtreeClient(vm);
            	//userSvc.initZtreeClient(vm);
            }
        }
     
        
    }
    
    
    
})();
