(function () {
    'use strict';

    angular.module('app').controller('quartzCtrl', quartz);

    quartz.$inject = ['$location', 'quartzSvc'];

    function quartz($location, quartzSvc) {
        var vm = this;
        vm.title = '定时器配置';

        activate();
        function activate() {
            quartzSvc.grid(vm);
        }

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    quartzSvc.deleteQuartz(vm, id);
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

        //新增定时器
        vm.addQuartz = function () {
        	vm.quartz={};
        	vm.quartz.runWay="自动";
            $("#quartz_edit_div").kendoWindow({
                width : "600px",
                height : "400px",
                title : "定时器编辑",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
        }

          //修改定时器
        vm.edit = function (id) {
        	vm.id=id;
            $("#quartz_edit_div").kendoWindow({
                width : "600px",
                height : "400px",
                title : "定时器修改",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
            quartzSvc.getQuartzById(vm);
            
        }
        
        
        //关闭弹窗
        vm.colseQuartz = function(){
            window.parent.$("#quartz_edit_div").data("kendoWindow").close();
        }

        //保存定时器
        vm.saveQuartz = function(){
            quartzSvc.saveQuartz(vm);
        }
        
        vm.execute=function (id){
        	quartzSvc.quartzExecute(vm,id);
        }
        
        vm.stop=function (id){
        	quartzSvc.quartzStop(vm,id);
        }

    }
})();
