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

        //打开增加窗口
        vm.addWorkDay=function(){
            $("#workDay").kendoWindow({
                width: "45%",
                height: "auto",
                title: "系统工作日编辑",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        vm.create=function(){
            common.initJqValidation($('#form'));
            var isValid = $('#form').valid();
            if (isValid) {
                workdaySvc.createWorkday(vm);
            }
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
