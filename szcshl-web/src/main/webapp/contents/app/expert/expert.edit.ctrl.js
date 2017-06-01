(function () {
    'expert strict';

    angular.module('app').controller('expertEditCtrl', expert);

    expert.$inject = ['$location','projectExpeSvc','workExpeSvc','expertGlorySvc','expertSvc','$state']; 

    function expert($location,projectExpeSvc,workExpeSvc,expertGlorySvc,expertSvc,$state) {
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
        
        //start工作经验
        vm.createWPage=function(){
        	workExpeSvc.createPage(vm);
        }
        
        vm.saveWork=function(){
        	workExpeSvc.saveWork(vm);
        } 
        
        vm.deleteWork=function(){
        	workExpeSvc.deleteWork(vm);
        }
        
        vm.updateWPage=function(){
        	workExpeSvc.updatePage(vm);
        }
        
        vm.updateWork=function(){
        	workExpeSvc.updateWork(vm);
        }
        
        
        
        vm.onWClose=function(){
        	vm.isSaveWork=false;
        	vm.isUpdateWork=false;
        	workExpeSvc.cleanValue();
        	window.parent.$("#wrwindow").data("kendoWindow").close();
        }
        //end工作经验
        
        //start项目经验
        vm.onPClose=function(){
        	vm.isSaveProject=false;
        	vm.isUpdateProject=false;
        	projectExpeSvc.cleanValue();
        	window.parent.$("#pjwindow").data("kendoWindow").close();
        }
        
        vm.createPPage=function(){
        	projectExpeSvc.createPage(vm);
        }
        
        vm.saveProject=function(){
        	projectExpeSvc.saveProject(vm);
        } 
        
        vm.deleteProject=function(){
        	projectExpeSvc.deleteProject(vm);
        }
        
        vm.updatePPage=function(){
        	projectExpeSvc.updatePage(vm);
        }
        
        vm.updateProject=function(){
        	projectExpeSvc.updateProject(vm);
        }
        
        //end项目经验
        
        //satart专家聘书
        vm.createGloryPage=function(){
        	expertGlorySvc.createPage(vm);
        }
        
        vm.saveGlory=function(){
        	expertGlorySvc.saveGlory(vm);
        } 
        
        vm.deleteGlory=function(){
        	expertGlorySvc.deleteGlory(vm);
        }
        
        vm.updateGPage=function(){
        	expertGlorySvc.updatePage(vm);
        }
        
        vm.updateGlory=function(){
        	expertGlorySvc.updateGlory(vm);
        }
        
        vm.onGClose=function(){
        	vm.isSaveGlory=false;
        	vm.isUpdateGlory=false;
        	expertGlorySvc.cleanValue();
        	window.parent.$("#grwindow").data("kendoWindow").close();
        }
      //end专家聘书
        activate();
        function activate() {
        	expertSvc.initUpload(vm);       	
        }
    }
})();
