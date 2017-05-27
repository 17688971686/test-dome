(function () {
    'expert strict';

    angular.module('app').controller('expertEditCtrl', expert);

    expert.$inject = ['$location','projectExpeSvc','workExpeSvc','expertSvc','$state']; 

    function expert($location,projectExpeSvc,workExpeSvc,expertSvc,$state) {
        var vm = this;
        vm.model = {};
        vm.title = '专家信息录入';
        vm.isuserExist=false;
        vm.isHide=true;
        vm.uploadBtnName="选择头像";
        vm.isUpload=false;
        vm.id = $state.params.expertID;     
        
        if (vm.id) {
        	vm.isUpload = true;
            vm.title = '更新专家';
            vm.isHide=false;
            vm.expertID=vm.id;
            vm.uploadBtnName="修改头像";
            expertSvc.getExpertById(vm);
            expertSvc.getPhotoByExpertId(vm);
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
        	expertSvc.initUpload(vm);       	
        }
    }
})();
