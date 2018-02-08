(function () {
    'use strict';
    angular.module('app').controller('signDeletCtrl', signDelet);

    signDelet.$inject = ['signSvc', 'flowSvc', 'signFlowSvc', 'bsWin','$state','$rootScope','pauseProjectSvc'];

    function signDelet(signSvc, flowSvc, signFlowSvc, bsWin,$state,$rootScope,pauseProjectSvc) {
        var vm = this;
        vm.title = "作废项目列表";

        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.signListOptions.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});

        }

        //查找
        vm.query = function () {
            vm.signListOptions.dataSource._skip=0;
            vm.signListOptions.dataSource.read();
        }

        //恢复项目
        vm.editSignState = function (signid) {
            vm.signid = signid;
            pauseProjectSvc.getProjectStopBySignId(signid,function (data) {//查找作项目是否暂停
                if(data[0].isactive=="9" && data[0].isOverTime=="9"){//根据后台查回来最新数据的第一条来判断<后台已根据创建时间来排序了。保证第一天都是最新的>
                    vm.stateValue="2"//恢复时项目是暂停的
                }else{
                    vm.stateValue="1"//恢复时项目是进行时的
                }
            })
            bsWin.confirm({
                title: "询问提示",
                message: "确认要恢复项目吗？",
                onOk: function () {
                    signSvc.editSignState(vm,function (data) {
                        if(data.flag || data.reCode == 'ok'){
                            bsWin.alert("恢复成功！",function(){
                                vm.signListOptions.dataSource.read();
                            })
                        }else{
                            bsWin.alert(data.reMsg);
                        }

                    })
                }
            });

        }

        /**
         * 重置
         */
        vm.formReset = function () {
            var tab = $("#deletform").find('input,select').not(":submit, :reset, :image, :disabled,:hidden");
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
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
                    vm.project = preView.data.project;
                }
                //恢复数据
                /*vm.project = preView.data.project;*/
                //恢复页数页码
                if(preView.queryParams){
                    vm.queryParams=preView.queryParams;
                }

                signSvc.signDeletGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                signSvc.signDeletGrid(vm);
            }

        }

    }
})();