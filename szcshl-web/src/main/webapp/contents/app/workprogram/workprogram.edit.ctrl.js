(function () {
    'use strict';

    angular.module('app').controller('workprogramEditCtrl', workprogram);

    workprogram.$inject = ['$location','workprogramSvc','$state']; 

    function workprogram($location,workprogramSvc,$state) {        
        var vm = this;
    	vm.work = {};						//创建一个form对象
        vm.title = '创建评审方案';        	//标题
        vm.startDateTime = new Date("2006/6/1 08:00");
        vm.endDateTime = new Date("2030/6/1 21:00"); 
        vm.work.signId = $state.params.signid;		//这个是收文ID
                       	
        activate();
        function activate() {
        	workprogramSvc.initPage(vm);
            workprogramSvc.findAllMeeting(vm);//查找所有会议室地
            workprogramSvc.findCompanys(vm);//查找主管部门
            workprogramSvc.waitProjects(vm);//待选项目列表
        }
        //保存合并评审
        vm.mergeAddWork = function(vm){
        	workprogramSvc.mergeAddWork(vm);
        }
        //合并评审
        vm.reviewType = function(){
        	if(vm.work.isSigle=="合并评审"){
        		var isHide=false;
        	}else{
        		var isHide = true;
        	}
        }
        //主项目
        vm.mainIschecked = function(){
        	vm.isHideProject = false;
        }
        //次项目
        vm.subIschecked = function(){
        	vm.isHideProject = true;
        }
        //项目关联页面
        vm.gotoProjcet = function(){
        	workprogramSvc.gotoProjcet(vm);
        }
        //选择项目
        vm.selectworkProject = function(){
        	workprogramSvc.selectworkProject(vm);
        }
        //取消项目
        vm.cancelworkProject = function(){
        	workprogramSvc.cancelworkProject(vm);
        }
        //会议预定添加弹窗
        vm.addTimeStage = function(){
        	 $("#stageWindow").kendoWindow({
				width : "660px",
				height : "550px",
				title : "会议预定添加",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
        }
        
        //部长处理意见
        vm.ministerSugges = function(vm){
        	workprogramSvc.ministerSugges(vm);
        }
        
        //会议预定添加
        vm.saveRoom = function(){
        	workprogramSvc.saveRoom(vm);
        }
        //调整到会议室预定页面
        vm.gotoRoom = function(){
            window.parent.$("#stageWindow").data("kendoWindow").close();
            $state.go("room");
        }
        
        vm.onRoomClose = function(){
        	window.parent.$("#stageWindow").data("kendoWindow").close();
        }
        
        vm.queryRoom = function(){
        	workprogramSvc.queryRoom(vm);
        }
        //查询评估部门
        vm.findUsersByOrgId = function(type){
        	workprogramSvc.findUsersByOrgId(vm,type);
        }
        
        vm.create = function () {  
        	workprogramSvc.createWP(vm);
        };  
        
        vm.selectExpert = function(){
        	workprogramSvc.selectExpert(vm);
        }
            
        vm.findReviewDept = function(){
        
        }
    }
})();
