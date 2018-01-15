(function(){
    'use strict';
    angular.module('app').controller('headerCtrl' , header);
    header.$inject=['headerSvc','bsWin'];
    function header(headerSvc , bsWin){
        var vm = this;
        vm.header ={};
        vm.id ="";
        //新增表头窗口
        vm.create = function(id){
            if(id){
                vm.header.id = id;
                vm.isUpdate = true;
                headerSvc.getHeaderById(vm,id);
            }
            vm.header={};
            $("#addHeaderWindow").kendoWindow({
                width: "500px",
                height: "300px",
                title: "新建表字段",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "close"]
            }).data("kendoWindow").center().open();
        }

        //保存新增表头
        vm.save = function () {
            headerSvc.createHeader(vm);
        }


        vm.queryUser = function(){
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        vm.del = function (id){
            headerSvc.deleteHeader(vm, id);
        }

        vm.dels = function(){
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm:vm,
                    msg:'请选择数据'

                });
            } else {
                var ids=[];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr=ids.join(',');
                vm.del(idStr);
            }
        }

        vm.update = function (){
            headerSvc.updateHeader(vm);
        }
        activate();
        function activate(){
            headerSvc.headerGrid(vm);
        }
    }
})();