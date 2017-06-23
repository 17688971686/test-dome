(function () {
    'expert strict';

    angular.module('app').controller('expertEditCtrl', expert);

    expert.$inject = ['$location','projectExpeSvc','workExpeSvc','expertSvc','expertOfferSvc','expertTypeSvc','$state'];

    function expert($location,projectExpeSvc,workExpeSvc,expertSvc,expertOfferSvc,expertTypeSvc,$state) {
        var vm = this;
        vm.model = {};
        vm.title = '专家信息录入';
        vm.isuserExist=false;
        vm.isHide=true;
        vm.isUpdate=false;
        vm.expertID = $state.params.expertID;
        vm.expertOfferList = {};    //专家聘书列表
        vm.expertOffer = {};        //专家聘书

        activice();
        function activice(){
        	vm.showSS=true;
        	vm.showSC=false;
        	vm.showWS=true;
        	vm.showWC=false;
        	vm.MW=false;
        	vm.MS=false;
        	projectExpeSvc.initProjectType(vm);
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
        
  		vm.create=function(){
  			expertSvc.createExpert(vm);
  		}
        
        vm.update=function(){
        	expertSvc.updateExpert(vm);
        }
        
        vm.gotoWPage=function(){
        	workExpeSvc.gotoWPage(vm);
        }
        
        vm.updateWorkPage=function(){
        	vm.createWork=false;
        	workExpeSvc.updateWorkPage(vm);
        }
        
        vm.createWork=function(){
        	vm.createWork=true;
        	vm.work.expertID=vm.model.expertID;
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
        	vm.project.expertID=vm.model.expertID;
        	projectExpeSvc.createProject(vm);
        }
        
        vm.updateProjectPage=function(){
        	vm.createProject=false;
        	projectExpeSvc.updateProjectPage(vm);
        }
        
        vm.delertProject=function(){
        	projectExpeSvc.delertProject(vm);
        }

//专家类型-业务处理
        vm.gotoExpertType=function(expertID){
        	vm.isUpdate=false;
        	expertTypeSvc.gotoExpertType(vm);
        }
        
         vm.onETlose=function(){
        	expertTypeSvc.cleanValue();
        	window.parent.$("#addExpertType").data("kendoWindow").close();
        }
        
        vm.createExpertType=function(){
        	vm.createExpertType=true;
        	vm.expertType.expertID=vm.model.expertID;
        	expertTypeSvc.createExpertType(vm)
        }
        
        vm.updateProjectType=function(){
        	vm.isUpdate=true;
        	vm.createExpertType=false;
        	expertTypeSvc.updateExpertType(vm);
        }
        
        vm.saveUpdateExpertType=function(){
        	expertTypeSvc.saveUpdate(vm);
        }
        
        vm.delertProjectType=function(){
        	expertTypeSvc.deleteExpertType(vm);
        }

        //专家聘书弹窗
        vm.gotoOfferPage = function(){
            $("#ep_offer_div").kendoWindow({
                width : "800px",
                height : "600px",
                title : "专家聘书",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
        }

        //保存聘书信息
        vm.saveOffer = function () {
            if(vm.model.expertID){
                expertOfferSvc.saveOffer(vm);
            }else{
                common.alert({
                    vm : vm,
                    msg : "先保存专家信息！"
                })
            }
        }
        //关闭窗口信息
        vm.closeOffer=function(){
            window.parent.$("#ep_offer_div").data("kendoWindow").close();
        }
        //查看专家聘书
        vm.showOffer = function(id){
            vm.expertOffer = {};        //专家聘书
            vm.expertOfferList.forEach(function(o,index){
                if(o.id == id){
                   vm.expertOffer = o;
                   return ;
               }
            });
            vm.gotoOfferPage();
        }
        
        vm.chooseMW=function(){
        	vm.MW=true;
        	vm.showWS=true;
        	vm.showWC=false;
        }
        vm.cancelMW=function(){
        	vm.showWS=false;
        	vm.showWC=true;
        }
        vm.sureMW=function(){
        	if(!vm.majorWork){
        		common.alert({
							vm : vm,
							msg : "您未选择专业，请选择！",
							fn : function() {
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
							}
						})
        	}else{
	        	vm.model.majorWork=vm.majorWork;
	        	vm.showWS=false;
	        	vm.showWC=true;
        	}
        }
        vm.chooseMS=function(){
        	vm.MS=true;
        	vm.showSS=true;
        	vm.showSC=false;
        }
        vm.cancelMS=function(){
        	vm.showSS=false;
        	vm.showSC=true;
        }
       vm.sureMS=function(){
       	if(!vm.majorStudy){
        		common.alert({
							vm : vm,
							msg : "您未选择专业，请选择！",
							fn : function() {
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
							}
						})
        	}else{
	       		vm.model.majorStudy=vm.majorStudy;
	        	vm.showSS=false;
	        	vm.showSC=true;
        	}
       }
        
        
        
        
    }
})();
