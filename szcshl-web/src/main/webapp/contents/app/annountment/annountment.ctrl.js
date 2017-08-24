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
        vm.bathIssue = function(){
            annountmentSvc.updateIssueState(vm,"9");
        }

        //取消发布
        vm.bathUnissue = function () {
            annountmentSvc.updateIssueState(vm,"0");
        }

        vm.del = function (anId) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认删除数据吗?",
                onOk: function () {
                    annountmentSvc.deleteAnnountment(vm, anId);
                }
            });
        }

        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: "请选择数据"
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        }

        //查看详情
        vm.detail = function(id){
            if(id){
                $state.go('annountmentDetail', {id: id});
            }
        }

        //查询
        vm.queryAnnountment = function(){
            vm.gridOptions.dataSource.read();
        }

        //重置
        vm.resetAnnountment=function(){
        	var tab=$("#annountmentform").find('input,select');
        	$.each(tab,function(i,obj){
        		obj.value="";
        	});
        }
    }
})();