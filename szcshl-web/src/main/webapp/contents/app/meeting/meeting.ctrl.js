(function () {
    'use strict';

    angular.module('app').controller('meetingCtrl', meeting);

    meeting.$inject = ['bsWin', 'meetingSvc'];

    function meeting(bsWin, meetingSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '会议室列表';
        vm.model = {};
        vm.del = function (id) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认删除数据吗？",
                onOk: function () {
                    meetingSvc.deleteMeeting(id, vm.isSubmit, function (data) {
                        vm.isSubmit = false;
                        if (data.flag || data.reCode == "ok") {
                            bsWin.alert("消息提示", "操作成功！", function () {
                                vm.gridOptions.dataSource.read();
                            });
                        } else {
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            });
        }

        vm.used = function (id) {
            vm.model.id = id;
            vm.model.mrStatus = "1";//显示停用标志
            meetingSvc.roomUseState(vm.model,function(data){
                bsWin.alert("操作成功！",function(){
                    vm.gridOptions.dataSource.read();
                });
            });
        }

        //停用会议室
        vm.stoped = function (id) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认停用么？",
                onOk: function () {
                    vm.model.id = id;
                    vm.model.mrStatus = "2";//显示启用标志
                    meetingSvc.roomUseState(vm.model,function(data){
                        bsWin.alert("操作成功！",function(){
                            vm.gridOptions.dataSource.read();
                        });
                    });
                }
            });
        }

        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                bsWin.alert("请选择数据");
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        }

        //会议室查询
        vm.queryMeeting = function () {
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        /**
         * 重置
         */
        vm.formReset = function(){
            var tab = $("#meetingForm").find('input,select');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        activate();
        function activate() {
            meetingSvc.grid(vm);
        }
    }
})();
