(function () {
    'use strict';

    angular.module('app').controller('annountmentCtrl', annountment);

    annountment.$inject = ['$location','$state','$http','annountmentSvc','sysfileSvc'];

    function annountment($location,$state,$http,annountmentSvc,sysfileSvc) {
        var vm = this;
        vm.title="通知公告列表";
        
         active();
        function active(){
        	annountmentSvc.grid(vm);
//        	sysfileSvc.initUploadOptions(vm);
        }
        
        vm.del=function (anId){
			 common.confirm({
			 	vm : vm,
			 	title :"",
			 	msg :"确认删除数据吗？",
			 	fn :function(){
			 		$('.confirmDialog').modal('hide');
			 		annountmentSvc.deleteAnnountment(vm,anId);
			 	}
			 })       
        }
        
        vm.dels= function (){
        	var selectIds = common.getKendoCheckId('.grid');
        	if(selectIds.length==0){
        		common.alert({
        			vm : vm,
        			msg :"请选择数据"
        		});
        	}else{
        		var ids=[];
        		for(var i=0;i<selectIds.length;i++){
        			ids.push(selectIds[i].value);
        		}
        		var idStr=ids.join(',');
        		vm.del(idStr);
        	}
        }
        
        //附件上传
        vm.upload=function (){
        
        }
        
    }
})();