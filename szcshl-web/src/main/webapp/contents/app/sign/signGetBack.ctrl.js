/**
 * Created by Administrator on 2017/11/10.
 */
(function () {
    'use strict';
    angular.module('app').controller('signGetBackCtrl', signGetBack);

    signGetBack.$inject = ['signSvc', 'flowSvc', 'signFlowSvc', 'bsWin'];

    function signGetBack(signSvc, flowSvc, signFlowSvc, bsWin) {
        var vm = this;

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

        active();
        function active() {
            signSvc.signGetBackGrid(vm);
        }

    }
})();
