(function () {
    'use strict';
    angular.module('app').controller('signworkCtrl', signworkC);

    signworkC.$inject = ['signworkSvc','bsWin' , '$state'];

    function signworkC(signworkSvc,bsWin , $state) {
        var vm = this;
        vm.signId = $state.params.signid;

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
