(function () {
    'use strict';

    angular.module('app').controller('annountmentCtrl', annountment);

    annountment.$inject = ['$location', '$state', 'bsWin', 'annountmentSvc', 'sysfileSvc'];

    function annountment($location, $state, bsWin, annountmentSvc, sysfileSvc) {
        var vm = this;
        vm.title = "通知公告管理";

        active();
        function active() {
            annountmentSvc.grid(vm);
        }

        //批量发布
        vm.bathIssue = function () {
            annountmentSvc.updateIssueState(vm, "9");
        }

        //取消发布
        vm.bathUnissue = function () {
            annountmentSvc.updateIssueState(vm, "0");
        }

        vm.del = function (id) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认删除数据吗?",
                onOk: function () {
                    annountmentSvc.deleteAnnountment(id, function (data) {
                        if (data.flag || data.reCode == 'ok') {
                            vm.gridOptions.dataSource.read();
                            bsWin.alert("操作成功！");
                        } else {
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            });
        }

        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                bsWin.alert("请选择要删除的记录");
            } else {
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认删除数据吗?",
                    onOk: function () {
                        var ids = [];
                        for (var i = 0; i < selectIds.length; i++) {
                            ids.push(selectIds[i].value);
                        }
                        var idStr = ids.join(',');
                        annountmentSvc.deleteAnnountment(idStr, function (data) {
                            if (data.flag || data.reCode == 'ok') {
                                vm.gridOptions.dataSource.read();
                                bsWin.alert("操作成功！");
                            } else {
                                bsWin.alert(data.reMsg);
                            }
                        });
                    }
                });
            }
        }

        //查看详情
        vm.detail = function (id) {
            if (id) {
                $state.go('annountmentDetail', {id: id});
            }
        }

        //查询
        vm.queryAnnountment = function () {
            vm.gridOptions.dataSource.read();
        }

        //重置
        vm.resetAnnountment = function () {
            var tab = $("#annountmentform").find('input,select');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }
    }
})();