(function () {
    'use strict';

    angular.module('app').controller('suppletterCtrl', suppletter);

    suppletter.$inject = ['$location','suppletterSvc','$state','$http']; 

    function suppletter($location, suppletterSvc,$state,$http) {
        var vm = this;
        vm.showsupp=false;
        vm.title = '待办事项';
        vm.suppletter={};
        activate();
        function activate() {
        	common.initSuppData(vm,{$http:$http,$state:$state});
        }
        vm.addSuppContent=function(){
        	vm.showsupp=true;
        	 var ideaEditWindow = $("#addsuppContent");
       		 ideaEditWindow.kendoWindow({
	            width: "50%",
	            height: "80%",
	            title: "拟补资料函正文",
	            visible: false,
	            modal: true,
	            closable: true,
	            actions: ["Pin", "Minimize", "Maximize", "close"]
	        }).data("kendoWindow").center().open();

        }
    }
})();
