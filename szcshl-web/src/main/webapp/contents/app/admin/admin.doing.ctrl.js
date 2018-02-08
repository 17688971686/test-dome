(function () {
    'use strict';

    angular.module('app').controller('adminDoingCtrl', admin);

    admin.$inject = ['$location', 'adminSvc', 'flowSvc','pauseProjectSvc','bsWin','signSvc','$state','$rootScope'];

    function admin($location, adminSvc, flowSvc,pauseProjectSvc,bsWin,signSvc,$state,$rootScope) {
        var vm = this;
        vm.title = '在办项目';
        vm.model = {};
        vm.page = 0;
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.gridOptions.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});

        }
        activate();
        function activate() {
            vm.showwin = false;
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
                //恢复页数页码
                if(preView.queryParams){
                    vm.queryParams=preView.queryParams;
                }
                //恢复数据
                /*vm.project = preView.data.project;*/
                adminSvc.dtasksGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
                /*$scope.$apply()*/
            }else {
                adminSvc.dtasksGrid(vm);
            }

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
         * 查询
         */
        vm.querySign = function(){
            vm.gridOptions.dataSource._skip="";
            vm.gridOptions.dataSource.read();
        }
        /**
         * 重置查询条件
         */
        vm.formReset = function(){
            vm.model = {};
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
        /**
         * 删除项目（作废项目）
         * @param signid
         */
        vm.deletProject= function (signid) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认删除该条项目吗？",
                onOk: function () {
                    $('.confirmDialog').modal('hide');
                    signSvc.deleteSign(signid,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            bsWin.alert("删除成功！",function(){
                                vm.gridOptions.dataSource.read();
                            })
                        }else{
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            });
        }
    }
})();
