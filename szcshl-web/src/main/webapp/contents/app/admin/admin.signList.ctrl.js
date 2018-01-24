(function () {
    'use strict';

    angular.module('app').controller('adminSignListCtrl', admin);

    admin.$inject = ['signSvc', 'adminSvc', 'bsWin', '$state', 'headerSvc', 'pauseProjectSvc','$rootScope','$scope'];

    function admin(signSvc, adminSvc, bsWin, $state, headerSvc, pauseProjectSvc,$rootScope,$scope) {
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
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.signListOptions.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});

        }

        activate();
        function activate() {
            if($rootScope.view[vm.stateName]){
                var preView = $rootScope.view[vm.stateName];
                //恢复grid
                if(preView.gridParams){
                    vm.gridParams = preView.gridParams;
                }
                //恢复表单参数
                if(preView.data){
                    vm.project = preView.data.project;
                }
                //恢复数据
                /*vm.project = preView.data.project;*/
                //恢复页数页码
                if(preView.queryParams){
                    vm.queryParams=preView.queryParams;
                }

                adminSvc.getSignList(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
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
            vm.signListOptions.dataSource._skip=0;
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

        vm.statistics=function () {
            adminSvc.QueryStatistics(vm, function (data) {
                if (data != undefined) {
                    data.forEach(function (obj, x) {
                        vm.signList.push(obj);
                    });
                }
                if(vm.isContinue){
                if (data != undefined && data.length !=0) {
                    vm.page++;
                    vm.statistics();
                } else {

                }
                }
            });

        }


        //以下是项目查询统计（最新版-2017-12-28）
        vm.QueryStatistics = function () {
            vm.isContinue=true;
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
                            vm.daySum = 0;//定义勾选的评审天数总和
                            vm.WorkSum = 0;//定义勾选的工作日总和
                            vm.days = 0;//定义勾选的数量
                            //全选
                            vm.all = function (m) {
                                for (var i = 0; i < vm.signList.length; i++) {
                                    if (m === true) {
                                        //没勾上的
                                        vm.signList[i].state = true;
                                        vm.daySum = 0;
                                        vm.WorkSum = 0;
                                        vm.days = 0;
                                    } else {
                                        //勾上的
                                        vm.signList[i].state = false;
                                        if (vm.signList[i].reviewdays != undefined) {
                                            vm.daySum += vm.signList[i].reviewdays;
                                            vm.WorkSum += vm.signList[i].reviewdays - vm.signList[i].surplusdays;
                                        }
                                        vm.days++;
                                    }
                                }
                            };
                            //单勾选
                            vm.checks = function (reviewdays, surplusdays, id) {
                                if (reviewdays != undefined) {
                                    if ($("#" + id + "").is(':checked')) {//勾选
                                        vm.daySum += reviewdays; //评审天数相加
                                        vm.WorkSum += reviewdays - surplusdays;//工作日相加;
                                        vm.days++;  //数量相加
                                    } else {//没勾选
                                        vm.daySum -= reviewdays;
                                        vm.WorkSum -= reviewdays - surplusdays;
                                        vm.days--;
                                    }
                                }
                            }
                            //统计平均天数
                            vm.countDay = function () {

                                vm.averageDay = 0;
                                if (vm.days != 0) {
                                    vm.isopens = true;//显示div
                                    vm.isDay = true;//显示统计天数
                                    vm.averageDay = vm.daySum / vm.days;
                                }else{
                                    vm.isDay = false;//显示统计天数
                                    bsWin.alert("请选择要统计的数据");
                                }
                            }
                            //统计工作日
                            vm.countWork = function () {
                                vm.averageDay = 0;
                                if (vm.days != 0) {
                                    vm.isopens = true;//显示div
                                    vm.isDay = false;//显示统计工作日
                                    vm.averageDay = vm.WorkSum / vm.days;
                                }else{
                                    vm.isDay = true;//显示统计工作日
                                    bsWin.alert("请选择要统计的数据");
                                }
                            }

                        },
                        close:function () {
                            vm.isContinue=false;
                            vm.signList=[];
                            vm.page=0;
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
        vm.signDetails = function(signid , processInstanceId){
            vm.saveView();
            adminSvc.signDetails(signid , function(data){
                if(data.flag ){
                    $state.go("signDetails" , {signid : signid} , {processInstanceId : processInstanceId});
                }else{
                    bsWin.alert(data.reMsg);
                }
            });
        }
    }
})();
