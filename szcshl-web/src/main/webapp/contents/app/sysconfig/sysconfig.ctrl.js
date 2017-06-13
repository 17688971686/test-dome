(function () {
    'use strict';

    angular.module('app').controller('sysConfigCtrl', sysConfig);

    sysConfig.$inject = ['$location', 'sysConfigSvc'];

    function sysConfig($location, sysConfigSvc) {
        var vm = this;
        vm.model = {};      // 参数对象
        vm.title = '系统配置';

        activate();
        function activate() {
            sysConfigSvc.queryList(vm);
        }

        //新增参数
        vm.addConfig = function () {
            vm.model = {};
            //显示次项目窗口
            $("#configdiv").kendoWindow({
                width: "700px",
                height: "440px",
                title: "参数编辑",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //关闭窗口
        vm.closeWin = function () {
            window.parent.$("#configdiv").data("kendoWindow").close();
        }

        //保存参数
        vm.doCommit = function () {
            common.initJqValidation();
            var isValid = $('#configForm').valid();
            if (isValid) {
                sysConfigSvc.saveConfig(vm);
            }
        }

        //编辑参数
        vm.editConfig = function (id) {
            vm.configList.forEach(function (c, index) {
                if (c.id == id) {
                    vm.model = c;
                }
            });
            //显示次项目窗口
            $("#configdiv").kendoWindow({
                width: "700px",
                height: "440px",
                title: "参数编辑",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //删除参数
        vm.del = function (ids) {
            var checkSign = $("input[name='configid']:checked");
            if (checkSign.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择删除的参数"
                })
            } else {
                common.confirm({
                    vm: vm,
                    title: "",
                    msg: "确认删除数据吗？",
                    fn: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < checkSign.length; i++) {
                            ids.push(checkSign[i].value);
                        }
                        sysConfigSvc.deleteConfig(vm, ids.join(","));
                    }
                })
            }
        }

    }//E_sysConfig
})();
