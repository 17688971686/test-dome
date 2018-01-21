(function () {
    'use strict';
    angular.module('app').controller('signworkCtrl', signworkC);

    signworkC.$inject = ['signworkSvc','bsWin'];

    function signworkC(signworkSvc,bsWin) {
        var vm = this;
       
        active();
        function active() {
            vm.signWorkList = [];
            signworkSvc.signWorkList(function(data){
                vm.signWorkList = data;
                if(!vm.signWorkList || vm.signWorkList.length == 0){
                    vm.nodata = true;
                }else{
                    vm.nodata = false;
                }
            });
        }

        //收文查询
        vm.querySignWork = function(){
            active();
        }
        //重置
        vm.resetForm = function () {
            var tab = $("#signworkform").find('input,select');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }
    }
})();
