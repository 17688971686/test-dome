(function () {
    'use strict';

    angular.module('myApp').factory("operatorLogSvc", function ($http, bsWin) {
        var operatorLog_url = util.formatUrl("sys/operatorLog");
        return {
            /**
             * 构建操作日志登录列表配置项
             * @param vm    作用域
             */
            bsTableControl: function (vm) {
                vm.bsTableControl = {
                    options: util.getTableFilterOption({
                        url: operatorLog_url,
                        columns: [{
                            title: '行号',
                            switchable: false,
                            width: 50,
                            align: "left",
                            formatter: function (value, row, index) {
                                var state = vm.bsTableControl.state;
                                if (state.pageNumber && state.pageSize) {
                                    return index + 1 + (state.pageNumber - 1) * state.pageSize;
                                } else {
                                    return index + 1
                                }
                            }
                        }, {
                            field: 'createdDate',
                            title: '操作时间',
                            width: 100,
                            sortable: true,
                            filterControl: "datepicker",
                            filterOperator: "lte"
                        }, {
                            field: 'createdBy',
                            title: '操作人',
                            width: 100,
                            sortable: true,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'operateName',
                            title: '操作名',
                            width: 100,
                            sortable: true,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'operateTime',
                            title: '耗时',
                            width: 80,
                            sortable: true,
                            align: "right",
                            filterControl: "number"
                        }, {
                            field: 'ipAddress',
                            title: 'IP地址',
                            width: 100,
                            align: "center",
                            filterControl: "input",
                            filterOperator: "like"
                        },  {
                            field: 'sucessFlag',
                            title: '结果',
                            width: 60,
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: "<span ng-show='row.sucessFlag==1' class='bg-green'>成功</span><span ng-show='row.sucessFlag!=1' class='bg-red'>失败</span>"
                        }, {
                            field: 'message',
                            title: '结果消息',
                            width: 120
                        },{
                            field: 'oldInfo',
                            title: '原数据',
                            width: 200,
                            formatter: function (value, row, index) {
                                if(value){
                                    return "<textarea style='width: 100%' rows='4'>"+value+"</textarea>";
                                }else{
                                    return "<textarea style='width: 100%' rows='4'> </textarea>";
                                }
                            }
                        },{
                            field: 'newInfo',
                            title: '更新数据',
                            width: 200,
                            formatter: function (value, row, index) {
                                if(value){
                                    return "<textarea style='width: 100%' rows='4'>"+value+"</textarea>";
                                }else{
                                    return "<textarea style='width: 100%' rows='4'> </textarea>";
                                }
                            }
                        },{
                            field: 'updateInfo',
                            title: '更新信息',
                            width: 200,
                            formatter: function (value, row, index) {
                                if(value){
                                    return "<textarea style='width: 100%' rows='4'>"+value+"</textarea>";
                                }else{
                                    return "<textarea style='width: 100%' rows='4'> </textarea>";
                                }
                            }
                        }]
                    })
                };
            },
            /**
             * 获取操作日志数据
             * @param vm        作用域
             * @param params    查询参数
             */
            getOperatorLogList: function (vm, params) {
                $http.get(operatorLog_url, {
                    params: params
                }).success(function (data) {
                    vm.operatorLogList = data || {};
                });
            },
            /**
             * 删除操作日志数据
             * @param vm    作用域
             * @param days  删除指定天数之前的数据
             */
            deleteLog: function (vm, days) {
                vm.isSubmit = true;
                $http['delete'](operatorLog_url, {params: {"days": days || ""}}).then(function () {
                    bsWin.alert("删除成功");
                    vm.isSubmit = false;
                    $("#editTable").bootstrapTable('refresh', "");//刷新表格数据
                });
            }

        };


    });

})();