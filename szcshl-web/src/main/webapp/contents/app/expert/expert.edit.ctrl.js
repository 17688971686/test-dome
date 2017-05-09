(function () {
    'expert strict';

    angular
        .module('app')
        .controller('expertEditCtrl', expert);

    expert.$inject = ['$location','projectExpeSvc','workExpeSvc','expertSvc','$state']; 

    function expert($location,projectExpeSvc,workExpeSvc,expertSvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '专家录入';
        vm.isuserExist=false;
        vm.isHide=true;
        vm.id = $state.params.expertID;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新专家';
            vm.isHide=false;
            vm.expertID=vm.id;
            expertSvc.getExpertById(vm);
        }
        vm.create=function(){
        	expertSvc.createExpert(vm);
        }
        vm.update=function(){
        	expertSvc.updateExpert(vm);
        }
        vm.gotoWPage=function(){
        	workExpeSvc.gotoWPage(vm);
        }
        vm.updateWork=function(){
        	workExpeSvc.updateWork(vm);
        }
        vm.createWork=function(){
        	workExpeSvc.createWork(vm);
        }
        vm.saveWork=function(){
        	workExpeSvc.saveWork(vm);
        }
        vm.deleteWork=function(){
        	workExpeSvc.deleteWork(vm);
        }
        vm.onWClose=function(){
        	window.parent.$("#wrwindow").data("kendoWindow").close();
        }
        vm.onPClose=function(){
        	window.parent.$("#pjwindow").data("kendoWindow").close();
        }
        vm.gotoJPage=function(){
        	projectExpeSvc.gotoJPage(vm);
        }
        vm.updateProject=function(){
        	projectExpeSvc.updateProject(vm);
        }
        
        vm.createProject=function(){
        	projectExpeSvc.createProject(vm);
        }
        vm.saveProject=function(){
        	projectExpeSvc.saveProject(vm);
        }
        vm.delertProject=function(){
        	projectExpeSvc.delertProject(vm);
        }
        activate();
        function activate() {
        	 kendo.culture("zh-CN");
             $("#birthDay").kendoDatePicker({
             	 format: "yyyy-MM-dd",
             	 weekNumber: true
             });
             $("#createDate").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
             $("#beginTime").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
             $("#endTime").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
             $("#projectendTime").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
             $("#projectbeginTime").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
        	if (vm.isUpdate) {
        		//userSvc.getUserById(vm);
            } else {
            	//userSvc.initZtreeClient(vm);
            	//userSvc.getDict(vm);
            }
        	//userSvc.getOrg(vm);
        }
    }
})();
