(function () {
    'use strict';

    angular.module('app').controller('signReserveCtrl', sign);

    sign.$inject = ['$location', 'reserveSignSvc', '$state', '$rootScope', 'signSvc', 'bsWin', 'signFlowSvc'];

    function sign($location, reserveSignSvc, $state, $rootScope, signSvc, bsWin, signFlowSvc) {
        var vm = this;
        vm.model = {};						//创建一个form对象
        vm.title = '预签收列表';        		//标题  
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function () {
            $rootScope.storeView(vm.stateName, {
                gridParams: vm.gridOptions.dataSource.transport.options.read.data(),
                queryParams: vm.queryParams,
                data: vm
            });

        }
        //收文查询
        vm.querySign = function () {
            vm.gridOptions.dataSource._skip = 0;
            vm.gridOptions.dataSource.read();
        }
        vm.del = function (id) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认删除该条项目数据吗？",
                onOk: function () {
                    signSvc.deleteSign(id, function (data) {
                        if (data.flag || data.reCode == 'ok') {
                            bsWin.alert("删除成功！", function () {
                                vm.gridOptions.dataSource.read();
                            })
                        } else {
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            });
        }

        /**
         * 正式签收收文
         * @param signId
         */
        vm.realSign = function (signid) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认正式签收了么？",
                onOk: function () {
                    signSvc.realSign(signid, function (data) {
                        if (data.flag || data.reCode == 'ok') {
                            bsWin.success("操作成功！", function () {
                                vm.gridOptions.dataSource.read();
                            });
                        } else {
                            bsWin.error(data.reMsg);
                        }
                    });
                }
            });
        }

        vm.startNewFlow = function (signid) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认已经完成填写，并且发起流程么？",
                onOk: function () {
                    $('.confirmDialog').modal('hide');
                    signFlowSvc.startFlow(signid, function (data) {
                        if (data.flag || data.reCode == 'ok') {
                            bsWin.success("操作成功！", function () {
                                vm.gridOptions.dataSource.read();
                            });
                        } else {
                            bsWin.error(data.reMsg);
                        }
                    });
                }
            });
        }

        vm.getPreSignInfo = function () {
            if(vm.model.filecode == "" || vm.model.filecode == null){
                bsWin.alert("收文编号不能为空!");
                return ;
            }
            reserveSignSvc.getPreSignInfo(vm.model.filecode,function(data){
                if(data.flag || data.reCode == 'ok'){
                    $state.go('reserveList');
                }else{
                    bsWin.alert("获取委里预签收信息是失败，请核查！!");
                }
            });
        }


        active();
        function active() {
            if ($rootScope.view[vm.stateName]) {
                var preView = $rootScope.view[vm.stateName];
                //恢复grid
                if (preView.gridParams) {
                    vm.gridParams = preView.gridParams;
                }
                //恢复表单参数
                if (preView.data) {
                    vm.model = preView.data.model;
                }
                //恢复数据
                /*vm.project = preView.data.project;*/
                //恢复页数页码
                if (preView.queryParams) {
                    vm.queryParams = preView.queryParams;
                }

                reserveSignSvc.grid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            } else {
                reserveSignSvc.grid(vm);
            }

        }

    }
})();
