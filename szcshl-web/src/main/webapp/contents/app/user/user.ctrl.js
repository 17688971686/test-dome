(function () {
    'use strict';

    angular.module('app').controller('userCtrl', user);

    user.$inject = ['$location', 'userSvc', 'bsWin'];

    function user($location, userSvc, bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '用户列表';

        vm.del = function (id) {
            bsWin.confirm({
                    title: '温馨提示',
                    message: "确认删除数据吗？",
                    onOk: function () {
                        userSvc.deleteUser(vm, id, function (data) {
                            if (data.flag || data.reCode == 'ok') {
                                bsWin.success("操作成功！", function () {
                                    vm.gridOptions.dataSource.read();
                                });
                            } else {
                                bsWin.error(data.reMsg);
                            }
                        });
                    }
                }
            );
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                bsWin.alert("请选择要删除数据！");
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };
        //查询
        vm.queryUser = function () {
            userSvc.queryUser(vm);
        }

        /**
         * 重置密码
         */
        vm.resetPwd = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                bsWin.alert("请选择数据");
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                bsWin.confirm({
                        title: '温馨提示',
                        message: "确认重置密码？",
                        onOk: function () {
                            userSvc.resetPwd(vm, idStr);
                        }
                    }
                );
            }
        }

        activate();
        function activate() {
            //默认查询在职用户
            if(!vm.model){
                vm.model = {};
            }
            if(!vm.model.jobState){
                vm.model.jobState = "t";
            }
            userSvc.grid(vm);
        }
    }
})();
