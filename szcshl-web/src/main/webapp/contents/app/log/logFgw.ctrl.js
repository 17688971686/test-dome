(function () {
    'use strict';

    angular.module('app').controller('logFgwCtrl', log);

    log.$inject = ['$location','logSvc','reserveSignSvc','bsWin'];

    function log($location, logSvc,reserveSignSvc,bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '委里日志列表';

        activate();
        function activate() {
            logSvc.gridFgw(vm);
        }

        vm.queryLog = function(){
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        /**
         * 重置
         */
        vm.formReset = function(){
            var tab = $("#logform").find('input,select');
            $.each(tab, function(i, obj) {
                obj.value = "";
            });
        }

        vm.getSignInfo = function (){
            var filecode = $("#filecode").val();
            if(filecode == "" || filecode == null){
                bsWin.alert("收文编号不能为空!");
                return ;
            }
            reserveSignSvc.getPreSignInfo(filecode,'0',function(data){
                if(data.flag || data.reCode == 'ok'){
                    if(data.reMsg!='保存成功！'){
                        bsWin.alert(data.reMsg);
                        return ;
                    }else{
                        bsWin.alert("委项目获取成功！");
                        //vm.gridOptions.dataSource.read();
                    }
                }else{
                    bsWin.alert(data.reMsg);
                }
            });
        }
    }
})();
