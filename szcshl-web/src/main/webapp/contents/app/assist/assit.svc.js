(function () {
    'use strict';

    angular.module('app').factory('assistSvc', assist);

    assist.$inject = ['$http', 'bsWin'];

    function assist($http, bsWin) {
        var service = {
            initPlanPage: initPlanPage,						    //初始化计划方案表
            initPlanGrid: initPlanGrid,                         //舒适化表格
            saveAssistPlan: saveAssistPlan,                     //保存协审计划
            deletePlan: deletePlan,                             //删除协审计划包
            findPlanSign: findPlanSign,                         //根据计划ID查找收文选择的收文信息
            cancelPlanSign: cancelPlanSign,                     //取消挑选项目
            saveLowPlanSign: saveLowPlanSign,                   //保存挑选的次项目
            cancelLowPlanSign: cancelLowPlanSign,               //取消次项目
            initSelPlan: initSelPlan,                           //初始化选择的计划信息
            showPickLowSign: showPickLowSign,                   //初始化选择的次项目信息
            queryPlan: queryPlan,                               //查询协审计划信息
            getPlanSignByPlanId: getPlanSignByPlanId,			//通过协审计划id或取协审项目信息
            savePlanSign: savePlanSign,						    //保存协审项目信息(停用)
            savePlan: savePlan,								    //保存协审计划
            initPlanByPlanId: initPlanByPlanId,				    //初始化协审计划
            chooseAssistUnit: chooseAssistUnit,				    //选择协审单位
            saveDrawAssistUnit: saveDrawAssistUnit,              //保存协审计划抽签
            getUnitUser: getUnitUser,
            getAllUnit: getAllUnit,			                    //获取所有的协审单位
            saveAddChooleUnit: saveAddChooleUnit,		        //保存手动选择的协审单位
            initAssistUnitByPlanId: initAssistUnitByPlanId,	    //初始化计划项目的协审单位
            findAssistPlanSignById : findAssistPlanSignById,    //根据收文Id获取协审单位和协审费用——主要用于发文阶段编辑和详情页

        };
        return service;
        //begin findAssistPlanSignById
        function findAssistPlanSignById(signId , callBack){

            var httpOptions = {
                method : 'post',
                url : rootPath + '/assistPlanSign/findAssistPlanSignById',
                params : {signId : signId}
            }
            var httpSuccess = function success(response){
                if(callBack != undefined && typeof  callBack == "function"){
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });

        }
        //end findAssistPlanSignById

        function getPlanColumns() {
            var columns = [
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    template: "<span class='row-number'></span>",
                    filterable: false
                },
                {
                    field: "planName",
                    title: "协审计划名称",
                    width: "15%",
                    filterable: false
                },
                {
                    field: "reportTime",
                    title: "报审时间",
                    width: "15%",
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "drawTime",
                    title: "抽签时间",
                    width: "25%",
                    filterable: false
                },
                {
                    field: "createdBy",
                    title: "创建人员",
                    width: "15%",
                    filterable: false
                },
                {
                    field: "createdDate",
                    title: "记录生成时间",
                    width: "15%",
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "操作",
                    width: "10%",
                    filterable: false,
                    template: function (item) {
                        return '<button class="btn btn-xs btn-primary"  ng-click="vm.showPlanDetail(\'' + item.id + '\')"><span class="glyphicon glyphicon-edit"></span>详情</button>';
                    }
                }
            ];
            return columns;
        }

        //S_initPlanGrid
        function initPlanGrid(vm) {
            //2、初始化grid
            var dataSource = common.kendoGridDataSource(rootPath + "/assistPlan/findByOData", $("#searchform"));
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }

            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: getPlanColumns(),
                dataBound: dataBound,
                resizable: true
            };

        }//E_initPlanGrid

        //S_initPlanPage
        function initPlanPage(vm) {
            //1、查找正在办理的项目概算流程
            var httpOptions = {
                method: 'get',
                url: rootPath + "/assistPlan/initPlanManager",
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {

                        vm.planList = new Array();
                        if (response.data.signList && response.data.signList.length > 0) {
                            vm.assistSign = response.data.signList;
                        }
                        if (response.data.planList && response.data.planList.length > 0) {
                            vm.planList = response.data.planList;
                            //如果之前有选择，则默认显示选择的协审计划，否则默认显示第一个
                            if (!vm.selectPlanId) {
                                vm.showPlan = response.data.planList[0];
                                vm.selectPlanId = vm.showPlan.id;
                            }
                            //初始化显示的协审计划信息
                            initSelPlan(vm);
                        }
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_initPlanPage

        //S_saveAssistPlan
        function saveAssistPlan(planModel, isCommit, callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/assistPlan",
                data: planModel
            }
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function () {
                    isCommit = false;
                }
            });
        }//E_saveAssistPlan

        //S_deletePlan
        function deletePlan(showPlanId, isCommit, callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/assistPlan",
                data: showPlanId,
            }
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function () {
                    isCommit = false;
                }
            });
        }//E_deletePlan

        //S_findPlanSign
        function findPlanSign(vm, planId) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/findByPlanId",
                params: {planId: planId},
            }
            var httpSuccess = function success(response) {
                $('#planName').val(vm.selectPlanId);
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.pickSign = response.data;             //已选项目列表
                        vm.pickMainSign = new Array();          //主项目对象全部清空
                        vm.lowerSign = new Array();             //次项目对象
                        //挑选主项目
                        if (vm.showPlan.assistPlanSignDtoList) {
                            vm.pickSign.forEach(function (ps, index) {
                                vm.showPlan.assistPlanSignDtoList.forEach(function (apsl, number) {
                                    if (apsl.isMain == '9' && apsl.signId == ps.signid) {
                                        //添加评审类型属性
                                        ps.assistType = apsl.assistType;
                                        vm.pickMainSign.push(ps);
                                    }
                                });
                            });
                        }

                        if (vm.initPickLowSign == true) {
                            showPickLowSign(vm);
                        }
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function () {
                    vm.isCommit = false;
                }
            });
        }//E_findPlanSign

        //S_cancelPlanSign
        function cancelPlanSign(vm, signIds) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/assistPlan/cancelPlanSign",
                params: {
                    planId: vm.selectPlanId,
                    signIds: signIds
                },
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        initPlanPage(vm);
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        })
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function () {
                    vm.isCommit = false;
                }
            });
        }//E_cancelPlanSign

        //S_saveLowPlanSign
        function saveLowPlanSign(vm, signIdArr) {
            var saveLowSignArr = new Array();
            vm.assistSign.forEach(function (asts, index) {
                for (var i = 0, l = signIdArr.length; i < l; i++) {
                    if (asts.signid == signIdArr[i]) {
                        var LowSign = {};
                        LowSign.signId = asts.signid;
                        LowSign.projectName = asts.projectname;
                        LowSign.assistType = '合并项目';
                        LowSign.isMain = '0';
                        LowSign.mainSignId = vm.selectMainSignId;
                        saveLowSignArr.push(LowSign);
                    }
                }
            });

            vm.model = vm.showPlan;
            vm.model.assistPlanSignDtoList = saveLowSignArr;
            vm.isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/assistPlan/saveLowPlanSign",
                data: vm.model,
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isCommit = false;
                        vm.initPickLowSign = true;
                        initPlanPage(vm);
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        })
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function () {
                    vm.isCommit = false;
                }
            });
        }//E_saveLowPlanSign

        //S_cancelLowPlanSign
        function cancelLowPlanSign(vm, signIds) {
            vm.isCommit = true;
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/assistPlan/cancelLowPlanSign",
                params: {
                    planId: vm.showPlan.id,
                    signIds: signIds
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isCommit = false;
                        vm.initPickLowSign = true;
                        initPlanPage(vm);
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        })
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function () {
                    vm.isCommit = false;
                }
            });
        }//E_cancelLowPlanSign

        //S_initSelPlan
        function initSelPlan(vm) {
            if (vm.selectPlanId) {
                vm.planList.forEach(function (ps, number) {
                    if (ps.id == vm.selectPlanId) {
                        vm.showPlan = ps;
                        vm.drawType = vm.showPlan.drawType;
                    }
                });
                findPlanSign(vm, vm.selectPlanId);
            } else {
                //全部初始化
                vm.showPlan = {};                       //显示协审计划信息
                vm.pickSign = new Array();              //协审计划已选的项目列表
                vm.pickMainSign = new Array();          //主项目对象
                vm.lowerSign = new Array();             //次项目对象
                vm.selectMainSignId = "";               //查看的主项目ID
                vm.initPickLowSign = false;             //是否初始化选择的次项目信息
            }
        }//E_initSelPlan

        //S_showPickLowSign
        function showPickLowSign(vm) {
            vm.lowerSign = new Array();
            vm.pickSign.forEach(function (ps, number) {
                vm.showPlan.assistPlanSignDtoList.forEach(function (lps, index) {
                    if (lps.isMain == '0' && lps.mainSignId == vm.selectMainSignId && lps.signId == ps.signid) {
                        vm.lowerSign.push(ps);
                    }
                });
            });
        }//E_showPickLowSign

        //S_queryPlan
        function queryPlan(vm) {
            vm.gridOptions.dataSource.read();
        }//E_queryPlan

        //begin getPlanSignByPlan
        function getPlanSignByPlanId(planId, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + '/assistPlanSign/getPlanSignByPlanId',
                params: {planId: planId}
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end 

        //begin 保存协审项目信息（停用）
        function savePlanSign(assistPlanSign,callBack) {
            var httpOptions = {
                method: "put",
                url: rootPath + "/assistPlanSign/savePlanSign",
                headers: {
                    "contentType": "application/json;charset=utf-8"  //设置请求头信息
                },
                dataType: "json",
                data: angular.toJson(assistPlanSign)
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //end savePlanSign

        //begin 保存计划信息和协审信息
        function savePlan(assistPlan,callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/assistPlan/savePlanAndSign",
                data: assistPlan
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //end savePlan

        //begin initPlanByPlanId
        function initPlanByPlanId(planId, callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + '/assistPlan/html/findById',
                params: {id: planId}
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end initPlanByPlanId

        //begin chooseAssistUnit
        function chooseAssistUnit(planId,number,drawType,callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + '/assistUnit/chooseAssistUnit',
                params: {
                    planId: planId,
                    number: number,
                    drawType: drawType
                }
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end chooseAssistUnit


        // begin  getUnitUser
        function getUnitUser(vm) {
            var httpOptions = {
                method: "post",
                url: rootPath + '/assistUnitUser/findByOData'
            }

            var httpSuccess = function success(response) {
                vm.unitUserList = response.data.value;

            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //end getUnitUser

        //begin getAllUnit
        function getAllUnit(vm) {
            var httpOptions = {
                method: "post",
                url: rootPath + '/assistUnit/fingByOData'
            }

            var httpSuccess = function success(response) {
                vm.allUnitList = response.data.value;

            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end  getAllUnit

        //begin 保存抽签结果
        function saveDrawAssistUnit(vm) {
            var ids = '';
            var length = vm.assistPlanSign.length;
            vm.assistPlanSign.forEach(function (t, n) {
                if (n > 0) {
                    ids += ',';
                }
                //格式,AssistPlanSign.id|AssistUnit.id,,,
                ids += (t.id + '|' + t.assistUnit.id);
            });

            var unSelectedIds = '';
            if (vm.drawAssistUnits.length > 0) {
                var dauLength = vm.drawAssistUnits.length;
                vm.drawAssistUnits.forEach(function (t, n) {
                    if (n  > 0) {
                        unSelectedIds += ',';
                    }
                    //格式,AssistPlanSign.id|AssistUnit.id,,,
                    unSelectedIds += t.id;
                });
            }

            vm.isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/assistPlan/saveDrawAssistUnit",
                params: {
                    planId: vm.assistPlan.id,
                    drawAssitUnitIds: ids,
                    unSelectedIds: unSelectedIds
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isCommit = false;
                        bsWin.alert("操作成功！");
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    vm.isCommit = false;
                }
            });
        }

        //end saveDrawAssistUnit

        //begin saveAddChooleUnit
        function saveAddChooleUnit(vm, unitObject) {
            var needNum = vm.assistPlanSign.length;
            if(vm.assistPlan.drawType == '1'){      //轮空一家单位
                needNum = needNum + 1;
            }
            if (vm.unitList.length < needNum) {
                var i = 0;
                vm.unitList.forEach(function (x) {
                    if (unitObject.id == x.id) {
                        i = -1;
                        bsWin.alert("该协审单位已被选中！");
                        return;
                    }
                });
                if (i != -1) {
                    var httpOptions = {
                        method: "post",
                        url: rootPath + "/assistPlan/saveChooleUnit",
                        params: {planId: vm.assistPlan.id, unitId: unitObject.id}
                    }
                    var httpSuccess = function success(response) {
                        bsWin.alert("添加成功！");
                    }

                    common.http({
                        vm: vm,
                        $http: $http,
                        httpOptions: httpOptions,
                        success: httpSuccess
                    });
                }
            } else {
                bsWin.alert("当前只能" +needNum + "家单位参与抽签");
            }
        }

        //end saveAddChooleUnit

        //begin initAssistUnitByPlanId
        function initAssistUnitByPlanId(planId, callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/assistPlan/initAssistUnit",
                params: {planId: planId}
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //end  initAssistUnitByPlanId

    }
})();