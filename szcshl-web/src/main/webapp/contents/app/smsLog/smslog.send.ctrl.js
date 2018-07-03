(function () {
    'use strict';

    angular.module('app').controller('smslogSendCtrl', smslogSend);

    smslogSend.$inject = ['$location','smslogSendSvc','smslogSvc','$http','$state','bsWin'];

    function smslogSend($location, smslogSendSvc,smslogSvc,$http,$state,bsWin) {
        var vm = this;
        vm.model = {};
        vm.title = '发送短信';
        vm.issmslogSendExist=false;
        vm.model.id = $state.params.id;
        if (vm.model.id) {
            vm.isUpdate = true;
            vm.title = '查看短信';
        }
        activate();
        vm.selectsend = function activate() {
            // debugger;
            var httpOptions = {
                method: 'get',
                url: '/szcshl-web/smslog/querySMSLogType',
                params: {
                    type: "send"
                }
            }
            var httpSuccess = function success(response) {
                vm.select = response.data;
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
            smslogSendSvc.initRoleUsers(function(data){
                vm.orgMLeaderUsers = data.DIRECTOR;
                vm.orgSLeaderUser = data.VICE_DIRECTOR;
                vm.OrgDirectorUsers = data.DEPT_LEADER;
            });
            if (vm.isUpdate) {
                orgSvc.getOrgById(vm.model.id,function(data){
                    vm.model = data;
                });
            }
        }

        // vm.create = function () {
        //     common.initJqValidation();
        //     var isValid = $('form').valid();
        //     if(isValid){
        //         $.each(vm.OrgDirectorUsers,function( number , u){
        //             if(u.id == vm.model.orgDirector){
        //                 vm.model.orgDirectorName = u.displayName;
        //             }
        //         })
        //         $.each(vm.orgMLeaderUsers,function( number , u){
        //             if(u.id == vm.model.orgMLeader){
        //                 vm.model.orgMLeaderName = u.displayName;
        //             }
        //         })
        //         $.each(vm.orgSLeaderUser,function( number , u){
        //             if(u.id == vm.model.orgSLeader){
        //                 vm.model.orgSLeaderName = u.displayName;
        //             }
        //         })
        //         orgSvc.createOrg(vm.model,vm.isSubmit,function(data){
        //             vm.isSubmit = false;
        //             if(data.flag || data.reCode == 'ok'){
        //                 if(!vm.model.id){
        //                     vm.model.id = data.idCode;
        //                 }
        //                 bsWin.success("操作成功！");
        //             }else{
        //                 bsWin.error(data.reMsg);
        //             }
        //         });
        //     }else{
        //         bsWin.alert("保存失败，有红色*号的选项是必填项，请按要求填写！");
        //     }
        //
        // };
        //
        // vm.update = function () {
        //     common.initJqValidation();
        //     var isValid = $('form').valid();
        //     if(isValid){
        //         $.each(vm.OrgDirectorUsers,function( number , u){
        //             if(u.id == vm.model.orgDirector){
        //                 vm.model.orgDirectorName = u.displayName;
        //             }
        //         })
        //         $.each(vm.orgMLeaderUsers,function( number , u){
        //             if(u.id == vm.model.orgMLeader){
        //                 vm.model.orgMLeaderName = u.displayName;
        //             }
        //         })
        //         $.each(vm.orgSLeaderUser,function( number , u){
        //             if(u.id == vm.model.orgSLeader){
        //                 vm.model.orgSLeaderName = u.displayName;
        //             }
        //         })
        //         orgSvc.updateOrg(vm.model,vm.isSubmit,function(data){
        //             vm.isSubmit = false;
        //             if(data.flag || data.reCode == 'ok'){
        //                 bsWin.success("操作成功！");
        //             }else{
        //                 bsWin.alert(data.reMsg);
        //             }
        //         });
        //     }else{
        //         bsWin.alert("保存失败，有红色*号的选项是必填项，请按要求填写！");
        //     }
        // };

    }
})();
