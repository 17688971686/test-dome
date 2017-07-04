(function () {
    'use strict';

    angular.module('app').controller('workdayCtrl', workday);

    workday.$inject = ['$location', 'workdaySvc'];

    function workday($location, workdaySvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '工作日列表';
        
        activate();
        function activate() {
        	workdaySvc.grid(vm);
        }
        
        vm.del=function(id){
        	common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    workdaySvc.deleteWorkday(vm, id);
                }
            });
        }
        
         vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };
        
        
        vm.queryWorkday=function(){
        	workdaySvc.queryWorkday(vm);
        }
        
        vm.ResetWorkday=function(){
        	workdaySvc.clearValue(vm);
        }
    }
})();
