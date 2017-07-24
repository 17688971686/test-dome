(function () {
    'use strict';

    angular.module('app').controller('adminDoingCtrl', admin);

    admin.$inject = ['$location', 'adminSvc', 'flowSvc'];

    function admin($location, adminSvc, flowSvc) {
        var vm = this;
        vm.title = '在办任务';
        vm.model = {};
        activate();
        function activate() {
            vm.showwin = false;
            adminSvc.dtasksGrid(vm);
        }

        /**
         * 项目暂停弹窗
         */
        vm.pauseProject = function (signid) {
            vm.projectStop = {};
            vm.projectStop.signid = signid;
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认暂停项目吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    $("#spwindow").kendoWindow({
                        width: "560px",
                        height: "280px",
                        title: "暂停项目",
                        visible: false,
                        modal: true,
                        closable: true,
                        actions: ["Pin", "Minimize", "Maximize", "Close"]
                    }).data("kendoWindow").center().open();
                }
            })
        }

        /**
         * 确认项目暂停
         */
        vm.commitProjectStop = function () {
            common.initJqValidation($('#pauseform'));
            var isValid = $('#pauseform').valid();
            if(isValid){
                flowSvc.suspendFlow(vm, vm.projectStop.signid);
            }
        }

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
