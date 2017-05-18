(function () {
    'use strict';

    angular.module('app').controller('signFillinCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location, signSvc, $state) {        
        var vm = this;
    	vm.model = {};		//创建一个form对象   	
        vm.title = '填写报审登记表';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID
       
        vm.flowDeal = false;		//是否是流程处理标记
        
        signSvc.initFillData(vm);    	
        
        //申报登记编辑
        vm.updateFillin = function (){   	   
    	   signSvc.updateFillin(vm);  	   
        }                  
       
       //根据协办部门查询用户
       vm.findOfficeUsersByDeptId =function(status){
    	   signSvc.findOfficeUsersByDeptId(vm,status);
       }
       //输入资料分数数字校验
       vm.regNumber = function(){
    	   signSvc.regNumber(vm);
       }
       
       //s_项目建设书阶段(一)
       $("#addOriginal").click(function(){
    	   signSvc.add(vm);
       })
       $("#addCopy").click(function(){
    	   signSvc.add(vm);
       })
       
       $("#sugFileDealOriginal").click(function(){
    	   signSvc.sugFileDealOriginal(vm);
       })
       $("#sugFileDealCopy").click(function(){
    	   signSvc.sugFileDealCopy(vm);
       })
       
       $("#sugOrgApplyOriginal").click(function(){
    	   signSvc.sugOrgApplyOriginal(vm);
       })
       $("#sugOrgApplyCopy").click(function(){
    	   signSvc.sugOrgApplyCopy(vm);
       })
       
       $("#sugOrgReqOriginal").click(function(){
    	   signSvc.sugOrgReqOriginal(vm);
       })
       $("#sugOrgReqCopy").click(function(){
    	   signSvc.sugOrgReqCopy(vm);
       })
       
       $("#sugProAdviseOriginal").click(function(){
    	   signSvc.sugProAdviseOriginal(vm);
       })
       $("#sugProAdviseCopy").click(function(){
    	   signSvc.sugProAdviseCopy(vm);
       })
       
       $("#proSugEledocOriginal").click(function(){
    	   signSvc.proSugEledocOriginal(vm);
       })
       $("#proSugEledocCopy").click(function(){
    	   signSvc.proSugEledocCopy(vm);
       })
       
       $("#sugMeetOriginal").click(function(){
    	   signSvc.sugMeetOriginal(vm);
       })
       $("#sugMeetCopy").click(function(){
    	   signSvc.sugMeetCopy(vm);
       })
       //e_项目建设书阶段(一)
       
       //s_项目可研究性阶段(二)
       $("#studyPealOriginal").click(function(){
    	   signSvc.studyPealOriginal(vm);
       })
       $("#studyProDealCopy").click(function(){
    	   signSvc.studyProDealCopy(vm);
       })
       $("#studyFileDealOriginal").click(function(){
    	   signSvc.studyFileDealOriginal(vm);
       })
       $("#studyFileDealCopy").click(function(){
    	   signSvc.studyFileDealCopy(vm);
       })
       $("#studyOrgApplyOriginal").click(function(){
    	   signSvc.studyOrgApplyOriginal(vm);
       })
        $("#studyOrgApplyCopy").click(function(){
    	   signSvc.studyOrgApplyCopy(vm);
       })
       $("#studyOrgReqOriginal").click(function(){
    	   signSvc.studyOrgReqOriginal(vm);
       })
       $("#studyOrgReqCopy").click(function(){
    	   signSvc.studyOrgReqCopy(vm);
       })
        $("#studyProSugOriginal").click(function(){
    	   signSvc.studyProSugOriginal(vm);
       })
        $("#studyProSugCopy").click(function(){
    	   signSvc.studyProSugCopy(vm);
       })
       $("#studyMeetOriginal").click(function(){
    	   signSvc.studyMeetOriginal(vm);
       })
        $("#studyMeetCopy").click(function(){
    	   signSvc.studyMeetCopy(vm);
       })
      //右
      
       $("#envproReplyOriginal").click(function(){
    	   signSvc.envproReplyOriginal(vm);
       })
       $("#envproReplyCopy").click(function(){
    	   signSvc.envproReplyCopy(vm);
       })
       $("#planAddrOriginal").click(function(){
    	   signSvc.planAddrOriginal(vm);
       })
       $("#planAddrCopy").click(function(){
    	   signSvc.planAddrCopy(vm);
       })
       $("#reportOrigin").click(function(){
    	   signSvc.reportOrigin(vm);
       })
       $("#reportCopy").click(function(){
    	   signSvc.reportCopy(vm);
       })
       $("#eledocOriginal").click(function(){
    	   signSvc.eledocOriginal(vm);
       })
       $("#eledocCopy").click(function(){
    	   signSvc.eledocCopy(vm);
       })
        $("#energyOriginal").click(function(){
    	   signSvc.energyOriginal(vm);
       })
       $("#energyCopy").click(function(){
    	   signSvc.energyCopy(vm);
       })
       
     //e_项目可研究性阶段(二)
    	   
    }
})();
