(function () {
    'use strict';

    angular.module('app').controller('quartzCtrl', quartz);

    quartz.$inject = ['bsWin', 'quartzSvc'];

    function quartz(bsWin, quartzSvc) {
        var vm = this;
        vm.title = '定时器配置';

        activate();
        function activate() {
            quartzSvc.grid(vm);
        }

        //新增定时器
        vm.addQuartz = function () {
            vm.quartz = {};
            $("#quartz_edit_div").kendoWindow({
                width: "680px",
                height: "400px",
                title: "定时器编辑",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //修改定时器
        vm.edit = function (id) {
            $("#quartz_edit_div").kendoWindow({
                width: "600px",
                height: "400px",
                title: "定时器修改",
                visible: false,
                modal: true,
                open :function(){
                    quartzSvc.getQuartzById(id,function(data){
                        vm.quartz = data;
                    });
                },
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }


        //关闭弹窗
        vm.colseQuartz = function () {
            window.parent.$("#quartz_edit_div").data("kendoWindow").close();
        }

        /**
         * 保存定时器
         */
        vm.saveQuartz = function () {
            common.initJqValidation($("#quartz_form"));
            var isValid = $("#quartz_form").valid();
            if (isValid) {
                vm.isSubmit = true;
                quartzSvc.saveQuartz(vm.quartz, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        bsWin.alert("操作成功！", function () {
                            vm.colseQuartz();
                            vm.gridOptions.dataSource.read();
                        });
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            }
        }

        /**
         * 执行定时器
         * @param id
         */
        vm.execute = function (id) {
            quartzSvc.quartzExecute(id, function (data) {
                if (data.flag || data.reCode == 'ok') {
                    bsWin.alert("操作成功！", function () {
                        vm.gridOptions.dataSource.read();
                    });
                } else {
                    bsWin.alert(data.reMsg);
                }
            });
        }


        /**
         * 停止执行定时器
         * @param id
         */
        vm.stop = function (id) {
            quartzSvc.quartzStop(id, function (data) {
                if (data.flag || data.reCode == 'ok') {
                    bsWin.alert("操作成功！", function () {
                        vm.gridOptions.dataSource.read();
                    });
                } else {
                    bsWin.alert(data.reMsg);
                }
            });
        }

        /**
         * 设置定时器为不可用
         * @param id
         */
        vm.del = function (id) {
            bsWin.confirm("确认删除？删除数据无法恢复，请谨慎操作！", function () {
                quartzSvc.deleteQuartz(id,function(data){
                    if (data.flag || data.reCode == 'ok') {
                        bsWin.alert("操作成功！", function () {
                            vm.gridOptions.dataSource.read();
                        });
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            });
        }

    }
})();
