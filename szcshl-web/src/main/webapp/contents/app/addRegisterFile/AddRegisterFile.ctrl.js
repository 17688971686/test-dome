(function () {
    'use strict';

    angular.module('app').controller('addRegisterFileCtrl', addRegisterFile);

    addRegisterFile.$inject = ['$location', 'addRegisterFileSvc','$state'];

    function addRegisterFile($location, addRegisterFileSvc,$state) {
        var vm = this;
        vm.title = '登记补充资料';
       vm.sign = {};//收文对象
       vm.addRegister = {};			//登记补充材料
       vm.addRegisters = new Array;//登记补充材料集合
       vm.addRegister.signid =$state.params.signid;
        //新建登记补充材料
        vm.addRegisterFile = function(){
        	var signid = vm.addRegister.signid;
        	 vm.addRegister = {};	
        	 vm.addRegister.signid = signid;
        	 vm.addRegisters.push(vm.addRegister);
        	 vm.i++;
        }
        
        //保存登记补充材料
        vm.saveRegisterFilel = function(){
        	addRegisterFileSvc.saveRegisterFile(vm);
        }
        //删除登记补充资料
        vm.deleteRegisterFile = function(){
        	var isCheked = $("#addRegistersTable input[name='addRegistersCheck']:checked")
        	if(isCheked.length < 1){
        		 common.alert({
                     vm:vm,
                     msg:"请选择要删除的记录！"
                 })
        	}else{
        		var ids = [];
        		for(var i = 0;i <isCheked.length ;i++){
        			 vm.addRegisters.forEach(function( f , number){
      				   if(isCheked[i].value == f.id || f.id == undefined){
      					   vm.addRegisters.splice(number,1);
      				   }
      				   ids.push(isCheked[i].value);
      			   });
      				var idsStr = ids.join(",");
      				addRegisterFileSvc.deleteAddRegisterFile(vm,idsStr);
        		}
        	}
        }
        
       //份数数字校验
        vm.inputIntegerValue = function(checkValue,idSort){
        	if(addRegisterFileSvc.isUnsignedInteger(checkValue)){
        		$("#errorsUnmber" + idSort).html("");
        	}else{
        		$("#errorsUnmber" + idSort).html("只能输入数字");
        	}
        }

        activate();
        function activate() {
            addRegisterFileSvc.initAddRegisterFile(vm);
        }
    }
})();
