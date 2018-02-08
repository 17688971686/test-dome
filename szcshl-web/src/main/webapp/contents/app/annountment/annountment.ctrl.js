(function () {
    'use strict';

    angular.module('app').controller('annountmentCtrl', annountment);

    annountment.$inject = ['$location', '$state', 'bsWin', 'annountmentSvc', 'sysfileSvc','$rootScope'];

    function annountment($location, $state, bsWin, annountmentSvc, sysfileSvc,$rootScope) {
        var vm = this;
        vm.title = "通知公告管理";
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.gridOptions.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});

        }
        active();
        function active() {
            if($rootScope.view[vm.stateName]){
                var preView = $rootScope.view[vm.stateName];
                //恢复grid
                if(preView.gridParams){
                    vm.gridParams = preView.gridParams;
                }
                //恢复表单参数
                if(preView.data){
                    vm.model = preView.data.model;
                }
                //恢复数据
                /*vm.project = preView.data.project;*/
                //恢复页数页码
                if(preView.queryParams){
                    vm.queryParams=preView.queryParams;
                }

                annountmentSvc.grid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                annountmentSvc.grid(vm);
            }

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
                                bsWin.alert("操作成功！",function(){
                                    vm.gridOptions.dataSource.read();
                                });
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
                vm.saveView();
                $state.go('annountmentDetail', {id: id});
            }
        }

        //查询
        vm.queryAnnountment = function () {
            vm.gridOptions.dataSource._skip="";
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