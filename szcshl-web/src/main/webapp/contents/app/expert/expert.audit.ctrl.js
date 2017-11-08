(function () {
    'use strict';

    angular.module('app').controller('expertAuditCtrl', expert);

    expert.$inject = ['$location', 'expertSvc'];

    function expert($location, expertSvc) {   
    	var vm = this;
    	
    	vm.searchAudit = function(){
    		expertSvc.searchAudit(vm);
    	}
        //S 查看专家详细
        vm.findExportDetail = function(id){
            $("#exportDetail").kendoWindow({
                width: "80%",
                height: "800px",
                title: "专家详细信息",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
            if (id) {
                expertSvc.getExpertById(id, function (data) {
                    vm.model = data;
                    vm.showSS = false;
                    vm.showSC = true;
                    vm.showWS = false;
                    vm.showWC = true;
                    // initUpload(vm);
                    $("#expertPhotoSrc").attr("src", rootPath + "/expert/transportImg?expertId=" + vm.model.expertID + "&t=" + Math.random());
                });
            }
        }
        //E 查看专家详细

    	//审核状态去到各状态
        vm.auditToOfficial = function() {
     	  expertSvc.auditTo(vm,2);
	    };
	    
	    vm.auditToAlternative=function() {
	      	expertSvc.auditTo(vm,3);
	    };
	    
	    vm.auditToStop=function() {
	      	expertSvc.auditTo(vm,4);
	    };
	    
	    vm.auditToRemove=function(){
	      	expertSvc.auditTo(vm,5);
	    };
	    
	    //各状态回到审核状态
	    vm.officialToAudit=function(){
	      	expertSvc.toAudit(vm,2);
	    };
	    
	    vm.alternativeToAudit=function(){
	      	expertSvc.toAudit(vm,3);
	    };
	    
	    vm.stopToAudit=function(){
	      	expertSvc.toAudit(vm,4);
	    };
	    
	    vm.temoveToAudit=function(){
	      	expertSvc.toAudit(vm,5);
	    };
          
        activate();
        function activate() {
        	expertSvc.auditGrid(vm);
        }
    }
})();
