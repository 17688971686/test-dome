(function(){
    'use strict';
    angular.module('app').controller('headerCtrl' , header);
    header.$inject=['headerSvc','bsWin'];
    function header(headerSvc , bsWin){
        var vm = this;

        //新增表头窗口
        vm.create = function(){
            vm.header={};
            $("#addHeaderWindow").kendoWindow({
                width: "500px",
                height: "300px",
                title: "新建表头",
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
            vm.gridOptions.dataSource.read();
        }

        activate();
        function activate(){
            headerSvc.headerGrid(vm);
        }
    }
})();