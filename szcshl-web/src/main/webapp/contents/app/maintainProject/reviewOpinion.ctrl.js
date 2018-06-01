(function () {
    'use strict';

    angular.module('app').controller('reviewOpinionCtrl', reviewOpinion);

    reviewOpinion.$inject = ['bsWin', 'signSvc','$state','dispatchSvc','workprogramSvc'];

    function reviewOpinion(bsWin, signSvc,$state,dispatchSvc,workprogramSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '修改意见';
        vm.model = {};
        vm.dispatchDoc={};
        vm.model.signid = $state.params.signid;   //业务ID
        vm.model.processInstanceId = $state.params.processInstanceId;	//流程实例ID
        activate();
        function activate() {
            // 初始化业务信息
            signSvc.initFlowPageData(vm.model.signid, function (data) {
                vm.model = data;
                //审批表，部长意见的转换
                vm.model.ministerhandlesug=vm.model.ministerhandlesug.replace(/<br>/g,"\n");
                vm.dispatchDoc=vm.model.dispatchDocDto;
                if(vm.dispatchDoc){
                    //有发文时在转换
                    if(vm.dispatchDoc.ministerSuggesttion){
                        vm.dispatchDoc.ministerSuggesttion=vm.dispatchDoc.ministerSuggesttion.replace(/<br>/g,"\n");; //IE7-8
                    }
                   if(vm.dispatchDoc.viceDirectorSuggesttion){
                       vm.dispatchDoc.viceDirectorSuggesttion=vm.dispatchDoc.viceDirectorSuggesttion.replace(/<br>/g,"\n");; //IE7-8
                   }

                }
                if(vm.model.workProgramDtoList){
                    vm.workList=vm.model.workProgramDtoList;
                    //找出主方案，取出部门
                    for(var i=0;i<vm.model.workProgramDtoList.length;i++){
                        if(vm.model.workProgramDtoList[i].branchId=="1"){
                            vm.work=vm.model.workProgramDtoList[i];//主的工作方案
                            //初始化部门，得到数组
                            if(vm.work.reviewOrgName){
                                vm.reviewOrgName=vm.work.reviewOrgName.split(",")
                            }
                        }
                    }
                     //重新赋值个各个工作方案的所属部门
                    for(var i=0;i< vm.workList.length;i++){
                            vm.workList[i].orgName=vm.reviewOrgName[vm.model.workProgramDtoList[i].branchId-1];
                    }
                }
            });

        }

        vm.goBackMain = function(){
            $state.go('MaintainProjectEdit',{
                signid: vm.model.signid,
                processInstanceId:vm.model.processInstanceId
            });
        }
         //保存
        vm.create=function () {
            //项目登记表意见部长意见
            vm.model.ministerhandlesug=vm.model.ministerhandlesug.replace(/\n/g, '<br>'); //IE7-8
         //保存项目登记表意见
            signSvc.updateFillin(vm.model, function (data) {
                vm.model.ministerhandlesug=vm.model.ministerhandlesug.replace(/<br>/g,"\n");
            });
            //保存工作方案的意见
            if(vm.workList!=undefined && vm.workList.length!=0){
                for (var i=0;i<vm.workList.length;i++){
                    workprogramSvc.createWP(vm.workList[i], false, vm.iscommit);
                }
            }
             if(vm.dispatchDoc){
                 //保存发文意见
                 vm.dispatchDoc.signid=vm.model.signid;
                 //保存时对格式的转换
                 if(vm.dispatchDoc.ministerSuggesttion) {
                     vm.dispatchDoc.ministerSuggesttion = vm.dispatchDoc.ministerSuggesttion.replace(/\n/g, '<br>'); //IE7-8
                 }
                 if(vm.dispatchDoc.viceDirectorSuggesttion) {
                     vm.dispatchDoc.viceDirectorSuggesttion = vm.dispatchDoc.viceDirectorSuggesttion.replace(/\n/g, '<br>'); //IE7-8
                 }
                 dispatchSvc.saveDispatch(vm,function(data){
                     //更新完后再重新转换
                     if(vm.dispatchDoc.ministerSuggesttion) {
                         vm.dispatchDoc.ministerSuggesttion = vm.dispatchDoc.ministerSuggesttion.replace(/<br>/g, "\n");
                         ; //IE7-8
                     }
                     if(vm.dispatchDoc.viceDirectorSuggesttion) {
                         vm.dispatchDoc.viceDirectorSuggesttion = vm.dispatchDoc.viceDirectorSuggesttion.replace(/<br>/g, "\n");
                         ; //IE7-8
                     }
                     bsWin.alert(data.reMsg);
                 });
             }else{
                 bsWin.alert("操作成功")
             }


        }
    }
})();
