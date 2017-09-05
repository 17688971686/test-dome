(function () {
    'use strict';

    angular.module('app').controller('topicAddCtrl', topicCtrl);

    topicCtrl.$inject = ['bsWin', '$state', '$http', 'topicSvc'];

    function topicCtrl(bsWin, $state, $http, topicSvc) {
        var vm = this;
        vm.title = '新增课题研究';
        vm.model = {};
        vm.model.fgwlx = 0;

        activate();
        function activate() {
            topicSvc.findOrgUser(function(data){
                vm.principalUsers = data;
            });
        }

        //检查项目负责人
        vm.checkPrincipal = function(){
            var selUserId = $("#mainPriUser").val();
            if(selUserId){
                $('#principalUser_ul input[selectType="assistUser"]').each(
                    function () {
                        var value = $(this).attr("value");
                        if (value == selUserId) {
                            $(this).removeAttr("checked");
                            $(this).attr("disabled", "disabled");
                        } else {
                            $(this).removeAttr("disabled");
                        }
                    }
                );
            }
        }

        //保存
        vm.create = function(){
            common.initJqValidation($('#topicform'));
            var isValid = $('#topicform').valid();
            if (isValid) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认发起流程么？发起流程后，数据将不能再修改！",
                    onOk: function () {
                        dictSvc.deleteDict(id,vm.isSubmit ,function(data){
                            var selUser = []
                            $('#principalUser_ul input[selectType="assistUser"]:checked').each(function () {
                                selUser.push($(this).attr("value"));
                            });
                            vm.model.prinUserIds = selUser.join(",");
                            topicSvc.createTopic(vm.model,vm.isCommit, function (data) {
                                if (data.flag || data.reCode == 'ok') {
                                    bsWin.alert("保存成功！",function(){
                                        window.location.reload();
                                    });
                                } else {
                                    bsWin.alert(data.reMsg);
                                }
                            });
                        });
                    }
                });
            }else{
                bsWin.alert("页面未填报完整或者为正确，请检查！");
            }
        }

        //发起流程
        vm.startFlow = function(){
            common.initJqValidation($('#topicform'));
            var isValid = $('#topicform').valid();
            if (isValid) {
                var selUser = []
                $('#principalUser_ul input[selectType="assistUser"]:checked').each(function () {
                    selUser.push($(this).attr("value"));
                });
                vm.model.prinUserIds = selUser.join(",");
                topicSvc.startFlow(vm.model,vm.isCommit,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        vm.model = data.reObj;
                        bsWin.alert("操作成功！");
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("页面未填报完整或者为正确，请检查！");
            }
        }
    }
})();
