(function () {
    'use strict';

    angular.module('app').controller('adminSignListCtrl', admin);

    admin.$inject = ['signSvc', 'adminSvc', 'bsWin', '$state', 'headerSvc', 'pauseProjectSvc', '$rootScope', '$scope'];

    function admin(signSvc, adminSvc, bsWin, $state, headerSvc, pauseProjectSvc, $rootScope, $scope) {
        var vm = this;
        vm.title = '项目查询统计';
        vm.currentAssociateSign = {};
        vm.signList = {};
        vm.headerList = {};
        vm.header = "";
        vm.project = {};
        vm.headerType = "项目类型";
        vm.fileName = "项目统计报表";//报表标题，初始化
        // vm.filters ={};
        vm.signList = [];
        vm.page = 0;
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function () {
            $rootScope.storeView(vm.stateName, {
                gridParams: vm.signListOptions.dataSource.transport.options.read.data(),
                queryParams: vm.queryParams,
                data: vm
            });

        }

        activate();
        function activate() {
            if ($rootScope.view[vm.stateName]) {
                var preView = $rootScope.view[vm.stateName];
                //恢复grid
                if (preView.gridParams) {
                    vm.gridParams = preView.gridParams;
                }
                //恢复表单参数
                if (preView.data) {
                    vm.project = preView.data.project;
                }
                //恢复数据
                /*vm.project = preView.data.project;*/
                //恢复页数页码
                if (preView.queryParams) {
                    vm.queryParams = preView.queryParams;
                }

                adminSvc.getSignList(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            } else {
                adminSvc.getSignList(vm);
            }
            //初始化查询参数
            adminSvc.initSignList(function (data) {
                if (data.flag || data.reCode == 'ok') {
                    vm.orgDeptList = data.reObj;
                }
            });
        }

        //重置
        vm.formReset = function () {
            /* var tab = $("#searchform").find('input,select');
             $.each(tab, function (i, obj) {
             $('input:checkbox').attr('checked', false);
             // obj.value = "";
             });*/
            vm.project = {};

        }

        //項目查詢統計
        vm.searchSignList = function () {
            vm.signListOptions.dataSource._skip = 0;
            vm.signListOptions.dataSource.read();
            vm.filters = vm.project;
            if (vm.filters && vm.filters != undefined) {
                var queryData = JSON.stringify(vm.filters);
                vm.filters = queryData.substring(1, queryData.length - 1);
            }
            vm.signList = [];
            vm.page = 0;
        }

        /**
         * 统计表
         */
        /*vm.statistical = function(){
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

         }*/

        vm.statistical = function () {

        }

        /**
         * 统计表导出
         */
        vm.excelExport = function () {
            var fileName = escape(encodeURIComponent(vm.fileName));
            if (vm.filters && vm.filters != undefined) {
                var filters = JSON.stringify(vm.filters);
                var filterDate = filters.substring(1, filters.length - 1);
            } else {
                filterDate = "";
            }
            window.open(rootPath + "/signView/excelExport?filterData=" + escape(encodeURIComponent(filterDate)) + "&fileName=" + fileName);
        }

        /**
         * 查看项目暂停信息
         */
        vm.ProjectStopInfo = function (signId) {
            $state.go('projectStopInfo', {signId: signId});
        }

        vm.statistics = function () {
            adminSvc.QueryStatistics(vm, function (data) {
                if (data != undefined) {
                    data.forEach(function (obj, x) {
                        vm.signList.push(obj);
                    });
                }
                if (vm.isContinue) {
                    if (data != undefined && data.length != 0) {
                        vm.page++;
                        vm.statistics();
                    } else {

                    }
                }
            });

        }


        //以下是项目查询统计（最新版-2017-12-28）
        vm.QueryStatistics = function () {
            vm.isContinue = true;
            //判断条件是否为空
            if (!vm.filters || vm.filters == undefined) {
                vm.filters = "";
            }
            vm.statistics();
            $("#queryStatisticsWindow").kendoWindow({
                width: "80%",
                height: "700px",
                title: "项目查询统计",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "close"],
                open: function () {
                    //统计平均天数
                    vm.countDay = function () {
                        var isCheck = $("#countSignDayTable input[name='sumSignDay']:checked");
                        vm.averageDay = 0;
                        if (isCheck.length == 0) {
                            bsWin.alert("请选择要统计的数据");
                        } else {
                            var signIds = [];
                            var totalLength = isCheck.length;
                            for (var i = 0; i < totalLength; i++) {
                                signIds.push(isCheck[i].id);
                            }
                            signSvc.sumExistDays(signIds.join(","),function (data) {
                                if(data.flag || data.reCode == 'ok'){
                                    vm.averageDay = (Number(data.reObj)/ totalLength).toFixed(2);
                                    vm.isopens = true;
                                    vm.isDay = true;
                                }else{
                                    bsWin.alert(data.reMsg);
                                }
                            });
                        }
                    }
                    //统计工作日
                    vm.countWork = function () {
                        var isCheck = $("#countSignDayTable input[name='sumSignDay']:checked");
                        vm.averageDay = 0;
                        if (isCheck.length == 0) {
                            bsWin.alert("请选择要统计的数据");
                        } else {
                            var totalDays = 0, daysCount = isCheck.length;
                            for (var i = 0; i < daysCount; i++) {
                                totalDays += Number(isCheck[i].value);
                                console.log(totalDays);
                            }
                            vm.averageDay = (totalDays / daysCount).toFixed(2);
                            vm.isopens = true;
                            vm.isDay = false;
                        }
                    }
                },
                close: function () {
                    vm.isContinue = false;
                    vm.signList = [];
                    vm.page = 0;
                }
            }).data("kendoWindow").center().open();
        }

        /**
         * 自定义报表
         */
        vm.selectHeader = function () {
            headerSvc.selectHeaderWindow(vm, vm.headerType);
        }
        /**
         * 对秘密项目查看执行权限限制
         * @param signid
         * @param processInstanceId
         */
        vm.signDetails = function (signid, processInstanceId) {
            vm.saveView();
            adminSvc.signDetails(signid, function (data) {
                if (data.flag) {
                    $state.go("signDetails", {signid: signid}, {processInstanceId: processInstanceId});
                } else {
                    bsWin.alert(data.reMsg);
                }
            });
        }
    }
})();
