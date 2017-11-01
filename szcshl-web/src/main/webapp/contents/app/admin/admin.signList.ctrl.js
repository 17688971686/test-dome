(function () {
    'use strict';

    angular.module('app').controller('adminSignListCtrl', admin);

    admin.$inject = ['signSvc', 'adminSvc','bsWin','$state' , 'headerSvc' , 'pauseProjectSvc'];

    function admin(signSvc, adminSvc,bsWin , $state , headerSvc , pauseProjectSvc) {
        var vm = this;
        vm.title = '项目查询统计';
        vm.currentAssociateSign = {};
        vm.signList={};
        vm.headerList={};
        vm.header = "";
        vm.project = {};
        vm.headerType = "项目类型";
        vm.fileName ="项目统计报表";//报表标题，初始化
        vm.filters ={};
        activate();
        function activate() {
            //初始化查询参数
            adminSvc.initSignList(function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.orgDeptList = data.reObj;
                }
            });
            adminSvc.getSignList(vm);
            adminSvc.statisticalGrid(vm);

        }

        //重置
        vm.formReset = function () {
            var tab = $("#searchform").find('input,select');
            $.each(tab, function (i, obj) {
                $('input:checkbox').attr('checked', false);
                obj.value = "";
            });
        }

        //項目查詢統計
        vm.searchSignList = function () {
            vm.signListOptions.dataSource.read();
            vm.filters  = vm.project;

        }

        /**
         * 统计表
         */
        vm.statistical = function(){
            var num = 1;
            vm.columns = [
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template : function(){
                        return num ++ ;
                    }
                }
            ];
            headerSvc.findHeaderListSelected(vm , function(data){
                vm.selectedHeaderList = data;
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
                    filterable: common.kendoGridConfig().filterable,
                    sortable: true,
                    selectable: "row",
                    columns:vm.columns
                });
                $("#reportWindow").kendoWindow({
                    width: "70%",
                    height: "70%",
                    title: "项目查询统计",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "close"]
                }).data("kendoWindow").center().open();
            });

        }

        /**
         * 统计表导出
         */
        vm.excelExport = function(){
            var fileName = escape(encodeURIComponent(vm.fileName));
            if(vm.filters && vm.filters !=undefined){
                var filters = JSON.stringify(vm.filters);
                var filterDate = filters.substring(1,filters.length-1);
            }
            window.open(rootPath + "/signView/excelExport?filterData=" + escape(encodeURIComponent(filterDate)) + "&fileName=" +fileName);
        }

        /**
         * 查看项目暂停信息
         */
        vm.ProjectStopInfo = function(signId){
            $state.go('projectStopInfo' , {signId : signId});
        }
    }
})();
