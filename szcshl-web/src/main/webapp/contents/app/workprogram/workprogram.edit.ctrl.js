(function () {
    'use strict';

    angular.module('app').controller('workprogramEditCtrl', workprogram);

    workprogram.$inject = ['$location','workprogramSvc','$state']; 

    function workprogram($location,workprogramSvc,$state) {        
        var vm = this;
    	vm.work = {};						//创建一个form对象
        vm.title = '创建评审方案';        	//标题
         
        vm.work.signId = $state.params.signid;		//这个是收文ID
        
        workprogramSvc.initPage(vm);
        workprogramSvc.findCompanys(vm);//查找主管部门
        activate();
        function activate() {
        	workprogramSvc.findAllMeeting(vm);//查找所有会议室地点
        	workprogramSvc.findAllUsers(vm);//查询所有用户
        }
        //会议预定添加弹窗
        vm.addTimeStage = function(){
        	workprogramSvc.addTimeStage(vm);
        }
        //部长处理意见
        vm.ministerSugges = function(vm){
        	workprogramSvc.ministerSugges(vm);
        }
        //会议预定添加
        vm.saveRoom = function(){
        	workprogramSvc.saveRoom(vm);
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
