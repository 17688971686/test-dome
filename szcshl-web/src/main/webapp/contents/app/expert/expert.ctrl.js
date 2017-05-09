(function () {
    'expert strict';

    angular
        .module('app')
        .controller('expertCtrl', expert);
    expert.$inject = ['$location','expertSvc']; 
    function expert($location, expertSvc) {
        /* jshint validthis:true */
    	var vm = this;
    	vm.data={};
        vm.titles = '专家列表';
        
        vm.normalExpert = function() {
        	expertSvc.forward(1);
        };
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
       
        
      /*  vm.optionExpert=function() {
        	expertSvc.forward (2);
        }
        vm.blockExpert=function() {
        	expertSvc.forward (3);
        }
        vm.deletedExpert=function(){
        	expertSvc.forward (4);
        }
        vm.unnormalExpert=function(){
        	expertSvc.back(2);
        }
        vm.unoptionExpert=function(){
        	expertSvc.back(3);
        }
        vm.unblockExpert=function(){
        	expertSvc.back(4);
        }
        vm.undeletedExpert=function(){
        	expertSvc.back(5);
        }*/
        activate();
        function activate() {
        	//expertSvc.getDict(vm,"SEX,QUALIFICATIONS,DEGREE,JOB,TITLE,EXPERTTYPE,PROCOSTTYPE,PROTECHTYPE,EXPERTRANGE");
        	expertSvc.grid(vm,rootPath + "/expert");
        	//expertSvc.gridWork(vm,"");
        	expertSvc.gridAudit(vm,rootPath + "/expert?$filter=state eq '1'",1);
        	expertSvc.gridAudit(vm,rootPath + "/expert?$filter=state eq '2'",2);
        	expertSvc.gridAudit(vm,rootPath + "/expert?$filter=state eq '3'",3);
        	expertSvc.gridAudit(vm,rootPath + "/expert?$filter=state eq '4'",4);
        	expertSvc.gridAudit(vm,rootPath + "/expert?$filter=state eq '5'",5);
        	//vm.gridOptions1=vm.data;
        	//expertSvc.getAudit(vm,"state=3");
        	//expertSvc.getAudit(vm,"state=4");
        	//expertSvc.getAudit(vm,"expeRttype=1");
        	//expertSvc.getAudit(vm,"expeRttype=2");
        }
    }
})();
