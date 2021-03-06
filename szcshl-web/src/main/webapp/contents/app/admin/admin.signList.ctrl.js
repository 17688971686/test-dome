(function () {
    'use strict';

    angular.module('app').controller('adminSignListCtrl', admin);

    admin.$inject = ['signSvc', 'adminSvc', 'bsWin', '$state', 'headerSvc', 'pauseProjectSvc', '$rootScope', '$scope','roomCountSvc'];

    function admin(signSvc, adminSvc, bsWin, $state, headerSvc, pauseProjectSvc, $rootScope, $scope,roomCountSvc) {
        var vm = this;
        vm.title = '项目查询统计';
        vm.currentAssociateSign = {};
        vm.signList = {};
        vm.headerList = {};
        vm.header = "";
        vm.project = {};
        vm.headerType = "项目类型";
        vm.fileName = "项目统计报表";//报表标题，初始化
        vm.formatErrorCount = 0; //统计日期不规范个数
        // vm.filters ={};
        vm.signList = [];
        vm.page = 0;
        vm.template = '1';//模板参数
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

            //用户
            /*roomCountSvc.findAllUsers(vm,function (data) {
                vm.userlist = {};
                vm.userlist = data;
            });*/
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

        /**
         * 查询统计
         */
        vm.searchSignList = function () {
            vm.signListOptions.dataSource._skip = 0;
            vm.signListOptions.dataSource.read();

            //对项目负责人查询统计进行处理
            var mUserName = $("input[name='mUserName']").val();
            var aUserName = $("input[name='aUserName']").val();
            var allPriUser = $("input[name='allPriUser']").val();
            if(mUserName){
                vm.project.mUserName = mUserName;
            }else if(aUserName){
                vm.project.aUserName = aUserName;
            }else if(allPriUser){
                vm.project.allPriUser = allPriUser;
            }

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
            var ids = [];
            for (var i = 0; i < vm.signList.length; i++) {
                ids.push(vm.signList[i].signid);
            }
            var idStr = ids.join(',');
            if(vm.template == '1'){
                signSvc.excelExport2(idStr);
            }else if(vm.template == '2'){
                signSvc.excelExport(idStr);
            }
        }

        /**
         * 自定义导出
         */
        vm.excelDefineExport = function () {
            var ids = [];
            for (var i = 0; i < vm.signList.length; i++) {
                ids.push(vm.signList[i].signid);
            }
            var idStr = ids.join(',');
            signSvc.excelExport(idStr);

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
                        if(obj.receivedate == undefined){
                            vm.formatErrorCount ++;
                        }
                    });
                }
                if (vm.isContinue) {
                    if (data != undefined && data.length != 0) {
                        vm.page++;
                        vm.statistics();
                    } else {

                    }
                }
                vm.countProject = vm.signList.length ; // 列表项目总数
            });

        }


        //以下是项目查询统计（最新版-2017-12-28）
        vm.QueryStatistics = function () {
            //重置值
            vm.formatErrorCount = 0 ;
            vm.countReviewDay = 0 ;
            vm.notSelectedProject = 0;
            vm.selectedProject = 0 ;
            vm.totalReviewDas = 0;
            vm.averageDay = 0;
            vm.avgWorkDay = 0;

            vm.countProject = 0;
            vm.selectProject = 0;
            vm.averageDay = 0;

            vm.isopens = false;
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
                        vm.countReviewDay = 0;//总共评审天数
                        vm.stopDay = 0 ;//暂停天数
                        var isCheck = $("#countSignDayTable input[name='sumSignDay']:checked");
                        vm.selectedProject = isCheck.length; //统计个数
                        vm.notSelectedProject = vm.countProject - vm.selectedProject; //未统计个数
                        vm.averageDay = 0;
                        if (isCheck.length == 0) {
                            bsWin.alert("请选择要统计的数据");
                        } else {
                            var signIds = [];
                            var totalLength = isCheck.length;
                            for (var i = 0; i < totalLength; i++) {
                                signIds.push(isCheck[i].id);
                            }
                            /* signSvc.sumExistDays(signIds.join(","),function (data) {
                             console.log(data);
                             if(data.flag || data.reCode == 'ok'){
                             vm.averageDay = (Number(data.reObj)/ totalLength).toFixed(2);
                             vm.isopens = true;
                             vm.isDay = true;
                             }else{
                             bsWin.alert(data.reMsg);
                             }
                             });*/

                            signSvc.findAVGDayById(signIds , function(data){
                                vm.isopens = true;
                                vm.isDay = true;
                                vm.totalReviewDas = data.reObj[0];
                                vm.averageDay = data.reObj[1];
                                vm.avgWorkDay = data.reObj[2];

                            })

                        }
                    }
                    //统计工作日
                    vm.countWork = function () {
                        var isCheck = $("#countSignDayTable input[name='sumSignDay']:checked");
                        vm.selectProject = isCheck.length;
                        vm.averageDay = 0;
                        if (isCheck.length == 0) {
                            bsWin.alert("请选择要统计的数据");
                        } else {
                            var totalDays = 0, daysCount = isCheck.length;
                            for (var i = 0; i < daysCount; i++) {
                                totalDays += Number(isCheck[i].value);
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
                    $state.go("signDetails", {signid:signid,processInstanceId:processInstanceId});
                } else {
                    bsWin.alert(data.reMsg);
                }
            });
        }
    }
})();
