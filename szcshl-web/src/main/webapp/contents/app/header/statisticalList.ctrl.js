(function(){
    'use strict';
    angular.module('app').controller('statisticalListCtrl' , statisticalList);
    statisticalList.$inject=['$state','headerSvc', '$scope' , 'adminSvc'];
    function statisticalList($state,headerSvc, $scope , adminSvc){
        var vm = this ;
        vm.selectedHeaderList = {};
        vm.headerType = $state.params.headerType;
        vm.header = false;
        vm.i=1;
        vm.exportData=[];
        vm.fileName ="项目统计报表";//报表标题，初始化
        vm.columns = [
            {
                field: "",
                title: "序号",
                width: 50,
                filterable: false,
                template : function(){
                    return vm.i ++ ;
                }
            }
        ];

        vm.initStat = function(){
            //设置监听
            $scope.$watch("vm.header" , function(newValue , oldValue){
                if(newValue && oldValue == false){
                    for(var i = 0 ; i<vm.selectedHeaderList.length ; i++){
                        var item = vm.selectedHeaderList[i];
                        vm.columns.push(
                            {
                                field: item.headerKey,
                                title: item.headerName,
                                width: 140,
                                filterable: false
                            }
                        )
                    }
                    $("#statisticalGrid").kendoGrid({
                        dataSource: vm.dataSource,
                        sortable: true,
                        selectable: "row",
                        columns:vm.columns
                    });
                }
            })

        }

        /**
         * 统计表的导出
         */

        vm.excelExport = function(){
            vm.exportData = $("#statisticalGrid").data("kendoGrid")._data;
            adminSvc.excelExport(vm,vm.exportData  , vm.fileName);

        }

        activate();
        function activate(){
            headerSvc.findHeaderListSelected(vm);
            headerSvc.statisticalGrid(vm);
            vm.initStat();
        }








    }

})();