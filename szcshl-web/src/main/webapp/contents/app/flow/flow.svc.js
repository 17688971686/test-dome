(function () {
    'use strict';

    angular.module('app').factory('flowSvc', flow);

    flow.$inject = ['$http', '$state', 'bsWin'];

    function flow($http, $state, bsWin) {
        var service = {
            initFlowData: initFlowData,         // 初始化流程数据
            getFlowInfo: getFlowInfo,           // 获取流程信息
            commit: commit,                     // 提交
            rollBackToLast: rollBackToLast,     // 回退到上一环节
            rollBack: rollBack,                 // 回退到选定环节
            initBackNode: initBackNode,         // 初始化回退环节信息
            suspendFlow: suspendFlow,           // 流程挂起
            activeFlow: activeFlow,             // 重启流程
            deleteFlow: deleteFlow,             // 流程终止
            historyData: historyData,           // 获取流程处理数据
        };
        return service;

        // S_初始化流程数据
        function initFlowData(vm) {
            var dataSource={};
            if(vm.flow != undefined){
                var processInstanceId = vm.flow.processInstanceId;
                if (angular.isUndefined(vm.flow.hideFlowImg)|| vm.flow.hideFlowImg == false) {
                    vm.picture = rootPath + "/flow/processInstance/img/"+ processInstanceId;
                }
                dataSource = new kendo.data.DataSource({
                    type: 'odata',
                    transport: common.kendoGridConfig().transport(rootPath+ "/flow/processInstance/history/" + processInstanceId),
                    schema: common.kendoGridConfig().schema({
                        id: "id"
                    }),
                    rowNumber: true,
                    headerCenter: true
                });
            }
            var columns = [{
                field: "",
                title: "序号",
                template: "<span class='row-number'></span>",
                width: 50
            }, {
                field: "nodeName",
                title: "环节名称",
                width: 120,
                filterable: false
            }, {
                field: "displayName",
                title: "处理人",
                width: 80,
                filterable: false
            }, {
                field: "startTime",
                title: "开始时间",
                width: 120,
                filterable: false,
                format: "{0: yyyy-MM-dd HH:mm:ss}"
            }, {
                field: "endTime",
                title: "结束时间",
                width: 120,
                filterable: false,
                format: "{0: yyyy-MM-dd HH:mm:ss}"
            }, {
                field: "durationStr",
                title: "处理时长",
                width: 120,
                filterable: false
            }, {
                field: "message",
                title: "处理信息",
                width: 300,
                filterable: false
            }];
            // End:column

            vm.historygrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    $(rows).each(function (i) {
                        if (i == rows.length - 1) {
                            initBackNode(vm);
                        }
                        $(this).find(".row-number").html(i + 1);
                    });
                }
            };
            // vm.historygrid.dataSource.read();
        }// E_初始化流程数据

        // S_获取流程处理记录
        function historyData(vm){
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath+ "/flow/processInstance/history/" + vm.instanceId),
                schema: common.kendoGridConfig().schema({
                    id: "hctId"
                }),
                rowNumber: true,
                headerCenter: true
            });
            var columns = [{
                field: "",
                title: "序号",
                template: "<span class='row-number'></span>",
                width: 40
            }, {
                field: "nodeName",
                title: "环节名称",
                width: 120,
                filterable: false
            }, {
                field: "displayName",
                title: "处理人",
                width: 80,
                filterable: false
            }, {
                field: "startTime",
                title: "开始时间",
                width: 120,
                filterable: false,
                format: "{0: yyyy-MM-dd HH:mm:ss}"
            }, {
                field: "endTime",
                title: "结束时间",
                width: 120,
                filterable: false,
                format: "{0: yyyy-MM-dd HH:mm:ss}"
            }, {
                field: "durationStr",
                title: "处理时长",
                width: 120,
                filterable: false
            }, {
                field: "message",
                title: "处理信息",
                width: 300,
                filterable: false
            }];
            // End:column

            vm.historygrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    $(rows).each(function (i) {
                        $(this).find(".row-number").html(i + 1);
                    });
                }
            };
        }// E_historyData

        // S_getFlowInfo
        function getFlowInfo(taskId,processInstanceId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/processInstance/flowNodeInfo",
                params: {
                    taskId: taskId,
                    processInstanceId:processInstanceId
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
        }// E_getFlowInfo

        // S_提交下一步
        function commit(isCommit,flowObj,callBack) {
            common.initJqValidation($("#flow_form"));
            var isValid = $("#flow_form").valid();
            if (isValid) {
                isCommit = true;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/flow/commit",
                    data: flowObj
                }
                var httpSuccess = function success(response) {
                    isCommit = false;
                    if (callBack != undefined && typeof callBack == 'function') {
                        callBack(response.data);
                    }
                }
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        isCommit = false;
                    }
                });
            }
        }// E_提交下一步

        // S_回退到上一步
        function rollBackToLast(flowModel,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/rollbacklast",
                data: flowModel
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
                onError: function (response) {
                    isCommit = false;
                }
            });
        }// E_回退到上一步

        // S_回退到指定环节
        function rollBack(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/rollbacklast",
                data: vm.flow
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg
                        })
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }// E_回退到指定环节

        // S_初始化回退环节信息
        function initBackNode(vm) {
            vm.backNode = [];
            // 初始化可回退环节
            var datas = vm.historygrid.dataSource.data()
            var totalNumber = datas.length;
            for (var i = 0; i < totalNumber; i++) {
                if (datas[i].assignee && datas[i].endTime) {
                    vm.backNode.push({
                        "activitiId": datas[i].activityId,
                        "activitiName": datas[i].activityName,
                        "assignee": datas[i].assignee
                    });
                }
            }
        }// E_初始化回退环节信息

        // S_流程挂起
        function suspendFlow(vm, businessKey) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/suspend/" + businessKey,
                data : vm.projectStop
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg,
                            closeDialog: true,
                            fn: function () {
                                if (response.data.reCode == "error") {
                                    vm.isCommit = false;
                                } else {
                                    window.parent.$("#spwindow").data("kendoWindow").close();
                                    vm.gridOptions.dataSource.read();
                                }
                            }
                        })
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function () {
                    vm.isCommit = false;
                }
            });
        }// E_流程挂起

        // S_流程激活
        function activeFlow(vm, businessKey) {
            vm.isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/active/" + businessKey
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg,
                            closeDialog: true,
                            fn: function () {
                                if (response.data.reCode == "error") {
                                    vm.isCommit = false;
                                } else {
                                    vm.gridOptions.dataSource.read();
                                }
                            }
                        })
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function () {
                    vm.isCommit = false;
                }
            });
        }// E_流程激活

        // S_终止流程
        function deleteFlow(vm) {
            if (vm.flow.dealOption == null || vm.flow.dealOption == "") {
                common.alert({
                    vm: vm,
                    msg: "请填写处理信息！"
                })
                return;
            }
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/deleteFLow",
                data: vm.flow
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功！"
                        })
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }// E_终止流程

    }
})();
