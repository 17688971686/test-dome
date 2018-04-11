(function(){
    'use strict';
    angular.module('app').controller('maintainExpertConfirmCtrl' , maintainExpertConfirm);
    maintainExpertConfirm.$inject = ['$state' , 'expertReviewSvc' , 'bsWin' , 'signSvc'];
     function maintainExpertConfirm($state , expertReviewSvc , bsWin , signSvc){
         var vm = this;
         vm.signid = $state.params.signid;
         vm.showExpertConfirm = true;

         activate();
         function activate(){
             expertReviewSvc.initReview(vm.signid,"", function (data) {
                 if(data){
                     vm.expertReviewDto = data;
                     if (vm.expertReviewDto && vm.expertReviewDto.expertSelectedDtoList) {
                         vm.confirmEPList = vm.expertReviewDto.expertSelectedDtoList;
                     }
                 }else{
                     vm.expertReviewDto = {};
                 }
             });
         }

         //未参加改为参加
         vm.updateToJoin = function () {
             var isCheck = $("#notJoinExpertTable input[name='notJoinExpert']:checked");
             if (isCheck.length < 1) {
                 bsWin.alert("请选择要改为参加会议的专家");
             } else {
                 var ids = [];
                 for (var i = 0; i < isCheck.length; i++) {
                     ids.push(isCheck[i].value);
                 }
                 expertReviewSvc.updateJoinState("", "", ids.join(','), '9', vm.isCommit, function (data) {
                     vm.reFleshJoinState(ids, '9');
                     bsWin.success("操作成功！");
                 });
             }
         }

         //参加改为未参加
         vm.updateToNotJoin = function () {
             var isCheck = $("#joinExpertTable input[name='joinExpert']:checked");
             if (isCheck.length < 1) {
                 bsWin.alert("请选择未参加会议的专家");
             } else {
                 var ids = [];
                 for (var i = 0; i < isCheck.length; i++) {
                     ids.push(isCheck[i].value);
                 }
                 expertReviewSvc.updateJoinState("", "", ids.join(','), '0', vm.isCommit, function (data) {
                     //1、更改专家评分和评审费发放的专家
                     vm.reFleshJoinState(ids, '0');
                     //2、
                     bsWin.success("操作成功！");
                 });
             }
         }

         //更新参加未参加状态
         vm.reFleshJoinState = function (ids, state) {
             $.each(ids, function (i, obj) {
                 //1、删除已确认的专家
                 $.each(vm.confirmEPList, function (index, epObj) {
                     if (obj == epObj.id) {
                         epObj.isJoin = state;
                     }
                 })
             })
         }
     }
})();