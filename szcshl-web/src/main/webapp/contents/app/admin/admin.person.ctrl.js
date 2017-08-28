(function () {
    'use strict';

    angular.module('app').controller('adminPersonDoingCtrl', adminPersonDoing);

    adminPersonDoing.$inject = ['$location', 'adminSvc', 'flowSvc','pauseProjectSvc'];

    function adminPersonDoing($location, adminSvc, flowSvc,pauseProjectSvc) {
        var vm = this;
        vm.title = '个人在办任务';
        vm.model = {};
        activate();
        function activate() {
            vm.showwin = false;
            adminSvc.persontasksGrid(vm);
        }

        /**
         * 项目暂停弹窗
         */
        vm.pauseProject = function (signid) {
            pauseProjectSvc.findPausingProject(vm,signid,"");
            // pauseProjectSvc.pauseProjectWindow(vm,signid,"");
        }

        vm.Checked=function(){
            if($("#fileNo").is(":checked")){
                $("#file1").prop("checked",false);
                $("#file2").prop("checked",false);
            }
        }
        vm.Checked2=function(){
            $("#fileNo").prop("checked",false);
        }


        /**
         * 保存项目暂停
         */
        vm.commitProjectStop = function () {
            pauseProjectSvc.pauseProject(vm);
        }
        /**
         * 取消项目暂停窗口
         */
        vm.closewin = function () {
            window.parent.$("#spwindow").data("kendoWindow").close()
        }

        /**
         * 流程激活
         * @param signid
         */
        vm.startProject = function (signid) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认激活吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    flowSvc.activeFlow(vm, signid);
                }
            })
        }
    }
})();