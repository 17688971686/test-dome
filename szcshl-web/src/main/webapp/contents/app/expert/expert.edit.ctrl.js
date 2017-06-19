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
        vm.isUpdate=false;
        vm.expertID = $state.params.expertID;
        activice();
        function activice(){
            if (vm.expertID) {
                vm.title = '更新专家';
                vm.isHide=false;
                vm.isUpdate=true;
                expertSvc.getExpertById(vm);
            }
        }

        vm.showUploadWin = function(){
            if(vm.model.expertID){
                $("#uploadWin").kendoWindow({
                    width : "660px",
                    height : "360px",
                    title : "上传头像",
                    visible : false,
                    modal : true,
                    closable : true,
                    actions : [ "Pin", "Minimize", "Maximize", "Close" ]
                }).data("kendoWindow").center().open();
            }else{
                common.alert({
                    vm : vm,
                    msg : "先保存数据在执行操作！"
                })
            }

        }
        
        vm.model={};
        vm.model.expertType='';
        vm.expertTypes=[];
		vm.expertTypeList=[];
        vm.choole=function(typeName,index){
        	vm.expertType={};
        	vm.expertType.expertType='';
        	vm.expertType.maJorBig='';
        	vm.expertType.maJorSmall='';
        	vm.index=index;
        	if(!vm.expertTypes[index]){
        	var isCheck=$("#expertType input[name='typeCheck']" );
        	vm.expertType.expertType=isCheck[index].value;
        	 $("#jor").kendoWindow({
                    width : "660px",
                    height : "360px",
                    title : typeName+"的突出专业",
                    visible : false,
                    modal : true,
                    closable : true,
                    actions : [ "Pin", "Minimize", "Maximize", "Close" ]
                }).data("kendoWindow").center().open();
        	}else{
        		vm.expertTypeList.splice(index,1);
        	}
        	
        }
        
        vm.saveChooleMajor=function(index){
        	vm.expertType.maJorBig=vm.type.maJorBig;
        	vm.expertType.maJorSmall=vm.type.maJorBig;
        	vm.expertTypeList[index]=vm.expertType;
        	window.parent.$("#jor").data("kendoWindow").close();
        	
        
        }
        vm.create=function(){
        	console.log(vm.expertTypeList);
        	vm.model.expertTypeDtoList=vm.expertTypeList;
        	console.log(vm.model);
        	expertSvc.createExpert(vm);
        }
        
        vm.update=function(){
        	expertSvc.updateExpert(vm);
        }
        
        vm.gotoWPage=function(){
        	vm.createWork=true;
        	workExpeSvc.gotoWPage(vm);
        }
        
        vm.updateWorkPage=function(){
        	vm.createWork=false;
        	workExpeSvc.updateWorkPage(vm);
        }
        
        vm.createWork=function(){
        	workExpeSvc.createWork(vm);
        }
        
        vm.updateWork=function(){
        	workExpeSvc.updateWork(vm);
        }
        
        vm.deleteWork=function(){
        	workExpeSvc.deleteWork(vm);
        }
        
        vm.onWClose=function(){
        	workExpeSvc.cleanValue();
        	window.parent.$("#wrwindow").data("kendoWindow").close();
        }
        
        vm.onPClose=function(){
        	projectExpeSvc.cleanValue();
        	window.parent.$("#pjwindow").data("kendoWindow").close();
        }
        
        vm.gotoJPage=function(){
        	projectExpeSvc.gotoJPage(vm);
        }
        
        vm.updateProject=function(){
        	projectExpeSvc.updateProject(vm);
        }
        
        vm.createProject=function(){
        	vm.createProject=true;
        	projectExpeSvc.createProject(vm);
        }
        
        vm.updateProjectPage=function(){
        	vm.createProject=false;
        	projectExpeSvc.updateProjectPage(vm);
        }
        
        vm.delertProject=function(){
        	projectExpeSvc.delertProject(vm);
        }
    }
})();
