(function () {
    'use strict';
    angular.module('app').controller('signDeletCtrl', signDelet);

    signDelet.$inject = ['signSvc', 'flowSvc', 'signFlowSvc', 'bsWin'];

    function signDelet(signSvc, flowSvc, signFlowSvc, bsWin) {
        var vm = this;
        vm.title="作废项目列表";
        vm.query=function () {//查找
            vm.signListOptions.dataSource.read();
        }
        
         //恢复项目
        vm.editSignState=function(signid){
            vm.signid=signid;
            bsWin.confirm({
                title: "询问提示",
                message: "确认要恢复项目吗？",
                onOk: function () {
                    signSvc.editSignState(vm,function (data) {
                        console.log(data);
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

        active();
        function active() {
            signSvc.signDeletGrid(vm);
        }

    }
})();