/**
 * Created by Administrator on 2017/11/10.
 */
(function () {
    'use strict';
    angular.module('app').controller('signGetBackCtrl', signGetBack);

    signGetBack.$inject = ['signSvc', 'flowSvc', 'signFlowSvc', 'bsWin','$state','$rootScope'];

    function signGetBack(signSvc, flowSvc, signFlowSvc, bsWin,$state,$rootScope) {
        var vm = this;
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.signGetBackGrid.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});

        }
        vm.getBack = function (taskId, businessKey) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认取回吗？",
                onOk: function () {
                    signSvc.getBack(taskId, businessKey,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            bsWin.alert("取回项目成功",function(){
                                vm.signGetBackGrid.dataSource.read();
                                //项目待办加1
                                var totalCount = parseInt( $('#DO_SIGN_COUNT').html());
                                $('#DO_SIGN_COUNT').html((totalCount + 1));
                            });
                        }else{
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            });
        }

        vm.query=function () {//查找
         vm.signGetBackGrid.dataSource._skip=0;
         vm.signGetBackGrid.dataSource.read();
        }

        active();
        function active() {
            if($rootScope.view[vm.stateName]){
                var preView = $rootScope.view[vm.stateName];
                //恢复grid
                //恢复查询条件
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

                signSvc.signGetBackGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                signSvc.signGetBackGrid(vm);
            }

        }

    }
})();
