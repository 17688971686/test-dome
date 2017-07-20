(function () {
    'use strict';

    angular.module('app').controller('adminDoingCtrl', admin);

    admin.$inject = ['$location','adminSvc']; 

    function admin($location, adminSvc) {
        var vm = this;
        vm.title = '在办任务';
        vm.model={};
        activate();
        function activate() {
        	vm.showwin=false;
        	adminSvc.dtasksGrid(vm);
        }
         vm.pauseProject=function(signid){
         	if(!signid){
        		return;
        	}
         	vm.model.signid=signid;
         	console.log(vm.model.signid);
         	vm.showwin=true;
        	var WorkeWindow = $("#spwindow");
			WorkeWindow.kendoWindow({
				width : "400px",
				height : "180px",
				title : "暂停项目",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
        }
        
        vm.closewin=function(){
        	window.parent.$("#spwindow").data("kendoWindow").close()
        }
        
        vm.commitpausePro=function(){
        	adminSvc.pauseProject(vm);
        }
        
        vm.startProject=function(signid){
        	if(!signid){
        		return;
        	}
        	vm.model.signid=signid;
        	adminSvc.startProject(vm);
        }
    }
})();
