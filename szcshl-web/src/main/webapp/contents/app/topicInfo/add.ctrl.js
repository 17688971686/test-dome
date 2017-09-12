(function () {
    'use strict';

    angular.module('app').controller('topicAddCtrl', topicCtrl);

    topicCtrl.$inject = ['bsWin', '$scope', 'sysfileSvc', 'topicSvc','$state'];

    function topicCtrl(bsWin, $scope, sysfileSvc, topicSvc,$state) {
        var vm = this;
        vm.title = '新增课题研究';
        vm.model = {};
        vm.model.fgwlx = 0;             //默认不是发改委立项
        vm.model.sendFgw = 0;           //默认不报发改委审批
        if($state.params.id){
            vm.model.id = $state.params.id;
        }

        //初始化附件上传控件
        vm.initFileUpload = function(){
            if(!vm.model.id){
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.model.id",function (newValue, oldValue) {
                    if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                        vm.initFileUpload();
                    }
                });
            }
            //创建附件对象
            vm.sysFile = {
                businessId : vm.model.id,
                mainId : vm.model.id,
                mainType : sysfileSvc.mainTypeValue().TOPIC,
                sysfileType:sysfileSvc.mainTypeValue().TOPIC_PLAN,
                sysBusiType:sysfileSvc.mainTypeValue().TOPIC_PLAN,
                showBusiType : false,
            };
            sysfileSvc.initUploadOptions({
                inputId:"sysfileinput",
                vm:vm,
            });
        }
        activate();
        function activate() {
            if(vm.model.id){
                topicSvc.initDetail(vm.model.id,function(data){
                    vm.model = data;
                });
            }
            topicSvc.findOrgUser(function(data){
                vm.principalUsers = data;
            });
            //初始化上传附件
            vm.initFileUpload();
        }

        //检查项目负责人
        vm.checkPrincipal = function(){
            var selUserId = $("#mainPrinUserId").val();
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
                var selUser = []
                $('#principalUser_ul input[selectType="assistUser"]:checked').each(function () {
                    selUser.push($(this).attr("value"));
                });
                vm.model.prinUserIds = selUser.join(",");
                topicSvc.createTopic(vm.model,vm.isCommit, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        vm.model = data.reObj;
                        bsWin.alert("操作成功！");
                    } else {
                        bsWin.alert(data.reMsg);
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
                bsWin.confirm({
                    title: "询问提示",
                    message: "发起流程后，当前页面数据将不能再修改！确认发起流程么？",
                    onOk: function () {
                        var selUser = []
                        $('#principalUser_ul input[selectType="assistUser"]:checked').each(function () {
                            selUser.push($(this).attr("value"));
                        });
                        vm.model.prinUserIds = selUser.join(",");
                        topicSvc.startFlow(vm.model,vm.isCommit,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                bsWin.alert("保存成功！",function(){
                                    $state.go('myTopic');
                                });
                            }else{
                                bsWin.alert(data.reMsg);
                            }
                        });
                    }
                });
            }else{
                bsWin.alert("页面未填报完整或者为正确，请检查！");
            }
        }
    }
})();
